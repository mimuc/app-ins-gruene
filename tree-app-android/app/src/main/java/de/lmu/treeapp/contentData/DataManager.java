package de.lmu.treeapp.contentData;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.IGameBase;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeProfile;
import de.lmu.treeapp.contentClasses.trees.WantedPosterImageList;
import de.lmu.treeapp.contentClasses.trees.WantedPosterTextList;
import de.lmu.treeapp.contentData.cms.ContentManager;
import de.lmu.treeapp.contentData.database.AppDatabase;
import de.lmu.treeapp.contentData.database.entities.app.GameStateScore;
import de.lmu.treeapp.contentData.database.entities.app.PlayerState;
import de.lmu.treeapp.contentData.database.entities.app.TreeState;
import de.lmu.treeapp.contentData.database.entities.app.TreeStateRelations;
import de.lmu.treeapp.contentData.database.entities.content.Tree_x_Game;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DataManager {
    private static DataManager INSTANCE;
    private Context context;

    public Boolean loaded = false;
    public List<Tree> trees;
    public List<TreeProfile> treeProfiles;
    public List<WantedPosterTextList> allWantedPosters;
    public List<WantedPosterImageList> allWantedPosterImages;
    public List<IGameBase> miniGames;
    public PlayerState player;
    public List<Tree_x_Game> tree_x_games;

    public static DataManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DataManager.class) {
                if (INSTANCE == null) { // double checked locking
                    DataManager dataManager = new DataManager();
                    dataManager.context = context;
                    dataManager.init().subscribe();
                    INSTANCE = dataManager;
                }
            }
        }
        return INSTANCE;
    }


    private Completable init() {
        return Completable.fromAction(() -> {
            // Saved Name
            PlayerState DB_player = AppDatabase.getInstance(context).playerDao().get();
            if (DB_player == null) {
                DB_player = new PlayerState();
                AppDatabase.getInstance(context).playerDao().insertOne(DB_player);
            }

            List<Tree> CMS_trees = ContentManager.getInstance(context).getTrees();
            List<TreeProfile> CMS_treeProfiles = ContentManager.getInstance(context).getTreeProfiles();
            List<WantedPosterTextList> CMS_allWantedPosters = ContentManager.getInstance(context).getAllWantedPosters();
            List<WantedPosterImageList> CMS_allWantedPosterImages = ContentManager.getInstance(context).getAllWantedPosterImages();
            List<IGameBase> CMS_miniGames = ContentManager.getInstance(context).getMinigames();
            List<Tree_x_Game> CMS_treexgames = ContentManager.getInstance(context).getTxg();
            List<TreeStateRelations> DB_trees = AppDatabase.getInstance(context).treeDao().getAll();

            for (int i = 0; i < CMS_trees.size(); i++) {
                Tree cmsTree = CMS_trees.get(i);
                boolean initByDB = false;
                for (int j = 0; j < DB_trees.size(); j++) {
                    TreeStateRelations dbTree = DB_trees.get(j);
                    if (cmsTree.getId() == dbTree.treeState.id) {
                        cmsTree.initAppData(dbTree);
                        initByDB = true;
                        break;
                    }
                }
                if (!initByDB) {
                    // Set volatile default values for every tree, if not done yet
                    TreeState treeState = new TreeState(cmsTree.getId());
                    TreeStateRelations treeStateRelations = new TreeStateRelations(treeState);
                    cmsTree.initAppData(treeStateRelations);
                    AppDatabase.getInstance(context).treeDao().insertOne(treeState);
                }
            }
            DataManager.getInstance(context).setData(CMS_trees, CMS_treeProfiles, CMS_allWantedPosters, CMS_allWantedPosterImages, CMS_miniGames, DB_player, CMS_treexgames);
        }).subscribeOn(Schedulers.io());
    }

    private void setData(List<Tree> trees,
                         List<TreeProfile> treeProfiles,
                         List<WantedPosterTextList> allWantedPosters,
                         List<WantedPosterImageList> allWantedPosterImages,
                         List<IGameBase> minigames,
                         PlayerState player,
                         List<Tree_x_Game> tree_x_games) {
        this.trees = trees;
        this.treeProfiles = treeProfiles;
        this.allWantedPosters = allWantedPosters;
        this.allWantedPosterImages = allWantedPosterImages;
        this.miniGames = minigames;
        this.player = player;
        this.tree_x_games = tree_x_games;
        this.loaded = true;
    }

    // Player-Stuff
    public String getPlayerName() {
        return player.name;
    }

    public Completable setPlayerName(String name) {
        player.name = name;
        return AppDatabase.getInstance(context).playerDao().updateOne(player).subscribeOn(Schedulers.io());
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

    /***
     * Return Score of Game by treeId and gameId
     * ***/
    public Single<GameStateScore> getOrCreateGameStateScore(int treeId, int gameId, Tree.GameCategories gameCategory) {
        for (Tree_x_Game tree_x_game : tree_x_games) {
            if (tree_x_game.gameId == gameId && tree_x_game.treeId == treeId && tree_x_game.gameCategory == gameCategory) {
                return AppDatabase.getInstance(context).gameStateScoresDao().getById(tree_x_game.id)
                        .onErrorResumeNext(s -> {
                            // If score state didn't exist, create and return it with its id.
                            GameStateScore gameStateScore = new GameStateScore(tree_x_game.id, 0);
                            return insertGameState(gameStateScore).flatMap(id -> {
                                gameStateScore.tree_x_gameId = id.intValue();
                                return Single.just(gameStateScore);
                            });
                        })
                        .subscribeOn(Schedulers.io());
            }
        }
        // Game does not exist
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

    public TreeProfile getTreeProfile(int id) {
        if (treeProfiles == null) return null;
        for (int i = 0; i < treeProfiles.size(); i++) {
            if (treeProfiles.get(i).uid == id) {
                return treeProfiles.get(i);
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
        return AppDatabase.getInstance(context).treeDao().update(model).subscribeOn(Schedulers.io());
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
        return AppDatabase.getInstance(context).treeDao().update(model).subscribeOn(Schedulers.io());
    }

    /***
     * Insert Items into GameStateScores-Table if they don't exist yet.
     * **/
    public Single<Long> insertGameState(int tree_x_gameId) {
        return insertGameState(new GameStateScore(tree_x_gameId, 0));
    }

    public Single<Long> insertGameState(GameStateScore gameStateScore) {
        try {
            return AppDatabase.getInstance(context).gameStateScoresDao().insertOne(gameStateScore);
        } catch (SQLiteException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Set new (High)Score
     **/
    public Completable updateGameState(GameStateScore gameStateScore) {
        return AppDatabase.getInstance(context).gameStateScoresDao().update(gameStateScore).subscribeOn(Schedulers.io());
    }
}
