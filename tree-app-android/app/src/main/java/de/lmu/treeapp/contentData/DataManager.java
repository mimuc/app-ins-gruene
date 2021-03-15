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
import de.lmu.treeapp.contentData.database.ContentDatabase;
import de.lmu.treeapp.contentData.database.daos.app.AbstractGameStateDao;
import de.lmu.treeapp.contentData.database.daos.app.AbstractGameStateRelationsDao;
import de.lmu.treeapp.contentData.database.daos.app.TreeStateDao;
import de.lmu.treeapp.contentData.database.entities.app.AbstractGameState;
import de.lmu.treeapp.contentData.database.entities.app.IGameState;
import de.lmu.treeapp.contentData.database.entities.app.TreeState;
import de.lmu.treeapp.contentData.database.entities.app.TreeStateRelations;
import de.lmu.treeapp.contentData.database.entities.content.UserProfileOption;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DataManager {
    private static Single<DataManager> INSTANCE;
    private Context context;

    public List<Tree> trees;
    public List<WantedPosterTextList> allWantedPosters;
    public List<WantedPosterImageList> allWantedPosterImages;
    public List<IGameBase> miniGames;
    protected Single<List<UserProfileOption>> userProfileOptions;

    public static DataManager getInstance(Context context) {
        return DataManager.getInstanceAsync(context).blockingGet();
    }

    public static Single<DataManager> getInstanceAsync(Context context) {
        if (INSTANCE == null) {
            // Use own task to not block UI
            Single<DataManager> objectSingle = Single.create(emitter -> {
                // Wait for previous calls
                synchronized (DataManager.class) {
                    // double checked locking
                    if (INSTANCE == null) {
                        DataManager dataManager = new DataManager();
                        dataManager.context = context;
                        // Block Thread while processing in order to stay synchronized
                        dataManager.init().blockingSubscribe();
                        // Instantiate instance with fast and simple "just"
                        INSTANCE = Single.just(dataManager);
                    }
                    // Send success to subscriber
                    emitter.onSuccess(INSTANCE.blockingGet());
                }
            });
            return objectSingle.subscribeOn(Schedulers.io());
        }
        return INSTANCE;
    }

    private Completable init() {
        return ContentManager.getInstanceAsync(context).flatMapCompletable(contentManager -> {
            List<Tree> CMS_trees = contentManager.getTrees();
            List<WantedPosterTextList> CMS_allWantedPosters = contentManager.getAllWantedPosters();
            List<WantedPosterImageList> CMS_allWantedPosterImages = contentManager.getAllWantedPosterImages();
            List<IGameBase> CMS_miniGames = contentManager.getMinigames();

            // Saved Name
            TreeStateDao treeStateDao = AppDatabase.getInstance(context).treeStateDao();

            this.setData(CMS_trees, CMS_allWantedPosters, CMS_allWantedPosterImages, CMS_miniGames);

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
        }).subscribeOn(Schedulers.io());
    }

    private void setData(List<Tree> trees,
                         List<WantedPosterTextList> allWantedPosters,
                         List<WantedPosterImageList> allWantedPosterImages,
                         List<IGameBase> minigames) {
        this.trees = trees;
        this.allWantedPosters = allWantedPosters;
        this.allWantedPosterImages = allWantedPosterImages;
        this.miniGames = minigames;
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
        return AppDatabase.getInstance(context).treeStateDao().updateOne(model).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
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
        return AppDatabase.getInstance(context).treeStateDao().updateOne(model).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Return single state of Game by treeId and gameId
     */
    public <T extends IGameState, U extends IGameState, S extends AbstractGameStateRelationsDao<T, U>> Single<U> getOrCreateGameStateSingle(int treeId, int gameId, Tree.GameCategories gameCategory, Class<S> clazz) {
        S gameStateDao = AppDatabase.getInstance(context).gameStateDao(clazz);
        return gameStateDao.getSingle(treeId, gameId, gameCategory)
                .onErrorResumeNext(s -> {
                    // If score state didn't exist, create and return it with its id.
                    T gameState = gameStateDao.getEntityClass().getConstructor(int.class, int.class, Tree.GameCategories.class).newInstance(treeId, gameId, gameCategory);
                    return insertGameState(gameState, clazz).flatMap(id -> {
                        if (gameStateDao.getEntityClass() == gameStateDao.getResultClass()) {
                            return Single.just((U) gameState);
                        } else {
                            // If requested result is different from insertion type, get inserted object from database. (e.g. StateRelations vs. State)
                            return getOrCreateGameStateSingle(treeId, gameId, gameCategory, clazz);
                        }
                    });
                })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Return list of GameStates by treeId and gameId
     */
    public <T extends IGameState, U extends IGameState, S extends AbstractGameStateRelationsDao<T, U>> Single<List<U>> getGameStateList(int treeId, int gameId, Tree.GameCategories gameCategory, Class<S> clazz) {
        S gameStateDao = AppDatabase.getInstance(context).gameStateDao(clazz);
        return gameStateDao.getList(treeId, gameId, gameCategory).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Get game states for a specific tree.
     */
    public <T extends IGameState, U extends IGameState, S extends AbstractGameStateRelationsDao<T, U>> Single<List<U>> getGameStateList(int treeId, Class<S> clazz) {
        S gameStateDao = AppDatabase.getInstance(context).gameStateDao(clazz);
        return gameStateDao.getList(treeId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Insert Game state.
     */
    public <T extends IGameState, U extends IGameState, S extends AbstractGameStateRelationsDao<T, U>> Single<Long> insertGameState(T gameState, Class<S> clazz) {
        try {
            return AppDatabase.getInstance(context).gameStateDao(clazz).insertOne(gameState).flatMap(id -> {
                gameState.setId(id.intValue());
                return Single.just(id);
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        } catch (SQLiteException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Update Game state.
     **/
    public <T extends AbstractGameState, S extends AbstractGameStateDao<T>> Completable updateGameState(T gameState, Class<S> clazz) {
        return AppDatabase.getInstance(context).gameStateDao(clazz).updateOne(gameState).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Asynchronously load user profile options.
     */
    public Single<List<UserProfileOption>> getUserProfileOptions() {
        if (this.userProfileOptions == null) {
            Single<List<UserProfileOption>> objectSingle = Single.create(emitter -> {
                synchronized (UserProfileOption.class) {
                    if (this.userProfileOptions == null) {
                        userProfileOptions = Single.just(ContentDatabase.getInstance(context).userProfileDao().getAll().blockingGet());
                    }
                    emitter.onSuccess(userProfileOptions.blockingGet());
                }
            });
            return objectSingle.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }
        return userProfileOptions;
    }
}
