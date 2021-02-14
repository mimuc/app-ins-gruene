package de.lmu.treeapp.contentData;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.IGameBase;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.WantedPosterImageList;
import de.lmu.treeapp.contentClasses.trees.WantedPosterTextList;
import de.lmu.treeapp.contentData.cms.ContentManager;
import de.lmu.treeapp.contentData.database.AppDatabase;
import de.lmu.treeapp.contentData.database.daos.app.AbstractGameStateDao;
import de.lmu.treeapp.contentData.database.daos.app.PlayerStateDao;
import de.lmu.treeapp.contentData.database.daos.app.TreeStateDao;
import de.lmu.treeapp.contentData.database.entities.app.AbstractGameState;
import de.lmu.treeapp.contentData.database.entities.app.PlayerState;
import de.lmu.treeapp.contentData.database.entities.app.TreeState;
import de.lmu.treeapp.contentData.database.entities.app.TreeStateRelations;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DataManager {
    private static DataManager INSTANCE;
    private Context context;

    public List<Tree> trees;
    public List<WantedPosterTextList> allWantedPosters;
    public List<WantedPosterImageList> allWantedPosterImages;
    public List<IGameBase> miniGames;
    public PlayerState player;

    public static DataManager getInstance(Context context) {
        return DataManager.getInstanceAsync(context).blockingGet();
    }

    public static Single<DataManager> getInstanceAsync(Context context) {
        if (INSTANCE == null) {
            synchronized (DataManager.class) {
                if (INSTANCE == null) { // double checked locking
                    DataManager dataManager = new DataManager();
                    dataManager.context = context;
                    return dataManager.init().andThen(Completable.defer(() -> {
                        INSTANCE = dataManager;
                        return Completable.complete();
                    })).andThen(Single.just(dataManager));
                }
            }
        }
        return Single.just(INSTANCE);
    }

    private Completable init() {
        return ContentManager.getInstanceAsync(context).flatMapCompletable(contentManager -> {
            List<Tree> CMS_trees = contentManager.getTrees();
            List<WantedPosterTextList> CMS_allWantedPosters = contentManager.getAllWantedPosters();
            List<WantedPosterImageList> CMS_allWantedPosterImages = contentManager.getAllWantedPosterImages();
            List<IGameBase> CMS_miniGames = contentManager.getMinigames();

            // Saved Name
            PlayerStateDao playerStateDao = AppDatabase.getInstance(context).playerStateDao();
            TreeStateDao treeStateDao = AppDatabase.getInstance(context).treeStateDao();

            return playerStateDao.getFirst().onErrorResumeNext(s -> {
                        PlayerState playerState = new PlayerState();
                        return playerStateDao.insertOne(playerState).flatMap(id -> {
                            playerState.id = id.intValue();
                            return Single.just(playerState);
                        });
                    }
            ).flatMapCompletable(playerState -> {
                this.setData(CMS_trees, CMS_allWantedPosters, CMS_allWantedPosterImages, CMS_miniGames, playerState);

                // Get or create treeStates
                return Observable.fromIterable(CMS_trees).flatMap(tree -> treeStateDao.getById(tree.getId()).onErrorResumeNext((s -> {
                    // Set volatile default values for tree, if not done yet
                    TreeState treeState = new TreeState(tree.getId());
                    TreeStateRelations treeStateRelations = new TreeStateRelations(treeState);
                    return treeStateDao.insertOne(treeState).flatMap(id -> Single.just(treeStateRelations));
                })).flatMap(treeStateRelations -> {
                    tree.initAppData(treeStateRelations);
                    return Single.just(treeStateRelations);
                }).toObservable()).ignoreElements();
            });
        }).subscribeOn(Schedulers.io());
    }

    private void setData(List<Tree> trees,
                         List<WantedPosterTextList> allWantedPosters,
                         List<WantedPosterImageList> allWantedPosterImages,
                         List<IGameBase> minigames,
                         PlayerState player) {
        this.trees = trees;
        this.allWantedPosters = allWantedPosters;
        this.allWantedPosterImages = allWantedPosterImages;
        this.miniGames = minigames;
        this.player = player;
    }

    // Player-Stuff
    public String getPlayerName() {
        return player.name;
    }

    public Completable setPlayerName(String name) {
        player.name = name;
        return AppDatabase.getInstance(context).playerStateDao().updateOne(player).subscribeOn(Schedulers.io());
    }

    // Get something
    public IGameBase getMinigame(int id) {
        if (miniGames == null) return null;
        for (int i = 0; i < miniGames.size(); i++) {
            if (miniGames.get(i).getId() == id) {
                return miniGames.get(i);
            }
        }
        return null;
    }

    // Get next quiz game which is always the current id + 10 (see minigames_chooseanswer.xml)
    public IGameBase getNextQuiz(int id) {
        if (miniGames == null) return null;
        for (int i = 0; i < miniGames.size(); i++) {
            if (miniGames.get(i).getId() == id) {
                return miniGames.get(i + 10);
            }
        }
        return null;
    }

    public Tree getTreeByQR(String qrCode) {
        qrCode = qrCode.trim();
        if (trees == null) return null;
        for (int i = 0; i < trees.size(); i++) {
            if (qrCode.equalsIgnoreCase(trees.get(i).qrCode)) {
                return trees.get(i);
            }
        }
        return null;
    }

    public Tree getTree(int id) {
        if (trees == null) return null;
        for (int i = 0; i < trees.size(); i++) {
            if (trees.get(i).getId() == id) {
                return trees.get(i);
            }
        }
        return null;
    }

    public WantedPosterTextList getAllWantedPosters(int id) {
        if (allWantedPosters == null) return null;
        for (int i = 0; i < allWantedPosters.size(); i++) {
            if (allWantedPosters.get(i).uid == id) {
                return allWantedPosters.get(i);
            }
        }
        return null;
    }

    public WantedPosterImageList getAllWantedPosterImages(int id) {
        if (allWantedPosterImages == null) return null;
        for (int i = 0; i < allWantedPosterImages.size(); i++) {
            if (allWantedPosterImages.get(i).uid == id) {
                return allWantedPosterImages.get(i);
            }
        }
        return null;
    }

    // Unlocked a Tree
    public Completable unlockTree(Tree tree) {
        final TreeState model = tree.appData.treeState;
        model.isUnlocked = true;
        return AppDatabase.getInstance(context).treeStateDao().updateOne(model).subscribeOn(Schedulers.io());
    }

    public boolean isGameCompleted(Tree.GameCategories category, int gameId, Tree tree) {
        final TreeState model = tree.appData.treeState;
        boolean gameCompleted = false;
        switch (category) {
            case leaf:
                if (model.leafGamesCompleted.contains(gameId))
                    gameCompleted = true;
                break;
            case fruit:
                if (model.fruitGamesCompleted.contains(gameId))
                    gameCompleted = true;
                break;
            case trunk:
                if (!model.trunkGamesCompleted.contains(gameId))
                    gameCompleted = true;
                break;
            case other:
                if (!model.otherGamesCompleted.contains(gameId))
                    gameCompleted = true;
                break;
            default:
                break;
        }
        return gameCompleted;
    }

    // GameCompleted overloaded Functions
    public Completable setGameCompleted(Tree.GameCategories category, Minigame_Base game, Tree tree) {
        return setGameCompleted(category, game.uid, tree);
    }

    public Completable setGameCompleted(Tree.GameCategories category, Minigame_Base game, int treeId) {
        if (trees != null && !trees.isEmpty()) {
            for (int i = 0; i < trees.size(); i++) {
                if (treeId == trees.get(i).getId()) {
                    return setGameCompleted(category, game.uid, trees.get(i));
                }
            }
        }
        return Completable.complete();
    }

    public Completable setGameCompleted(Tree.GameCategories category, int gameId, int treeId) {
        if (trees != null && !trees.isEmpty()) {
            for (int i = 0; i < trees.size(); i++) {
                if (treeId == trees.get(i).getId()) {
                    return setGameCompleted(category, gameId, trees.get(i));
                }
            }
        }
        return Completable.complete();
    }

    public Completable setGameCompleted(Tree.GameCategories category, int gameId, Tree tree) {
        final TreeState model = tree.appData.treeState;
        switch (category) {
            case leaf:
                if (!model.leafGamesCompleted.contains(gameId))
                    model.leafGamesCompleted.add(gameId);
                break;
            case fruit:
                if (!model.fruitGamesCompleted.contains(gameId))
                    model.fruitGamesCompleted.add(gameId);
                break;
            case trunk:
                if (!model.trunkGamesCompleted.contains(gameId))
                    model.trunkGamesCompleted.add(gameId);
                break;
            case other:
                if (!model.otherGamesCompleted.contains(gameId))
                    model.otherGamesCompleted.add(gameId);
                break;
            default:
                break;
        }
        return AppDatabase.getInstance(context).treeStateDao().updateOne(model).subscribeOn(Schedulers.io());
    }

    /**
     * Return Score of Game by treeId and gameId
     */
    public <T extends AbstractGameState, S extends AbstractGameStateDao<T>> Single<T> getOrCreateGameStateSingle(int treeId, int gameId, Tree.GameCategories gameCategory, Class<S> clazz) {
        S gameStateDao = AppDatabase.getInstance(context).gameStateDao(clazz);
        return gameStateDao.getSingle(treeId, gameId, gameCategory)
                .onErrorResumeNext(s -> {
                    // If score state didn't exist, create and return it with its id.
                    T gameState = gameStateDao.getEntityClass().getConstructor(int.class, int.class, Tree.GameCategories.class).newInstance(treeId, gameId, gameCategory);
                    return insertGameState(gameState, clazz).flatMap(id -> Single.just(gameState));
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * Insert Game state.
     */
    public <T extends AbstractGameState, S extends AbstractGameStateDao<T>> Single<Long> insertGameState(T gameState, Class<S> clazz) {
        try {
            return AppDatabase.getInstance(context).gameStateDao(clazz).insertOne(gameState).flatMap(id -> {
                gameState.id = id.intValue();
                return Single.just(id);
            }).subscribeOn(Schedulers.io());
        } catch (SQLiteException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Update Game state.
     **/
    public <T extends AbstractGameState, S extends AbstractGameStateDao<T>> Completable updateGameState(T gameState, Class<S> clazz) {
        return AppDatabase.getInstance(context).gameStateDao(clazz).updateOne(gameState).subscribeOn(Schedulers.io());
    }
}
