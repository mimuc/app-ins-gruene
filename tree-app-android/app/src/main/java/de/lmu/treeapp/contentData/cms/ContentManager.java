package de.lmu.treeapp.contentData.cms;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.IGameBase;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeProfile;
import de.lmu.treeapp.contentClasses.trees.WantedPosterImageList;
import de.lmu.treeapp.contentClasses.trees.WantedPosterTextList;
import de.lmu.treeapp.contentData.database.ContentDatabase;
import de.lmu.treeapp.contentData.database.entities.content.*;
import io.reactivex.rxjava3.core.Single;


public class ContentManager {
    private static ContentManager INSTANCE;
    private Context context;
    private final List<Tree> trees = new ArrayList<>();
    private final List<TreeProfile> treeProfiles = new ArrayList<>();
    private final List<WantedPosterTextList> allWantedPosters = new ArrayList<>();
    private final List<WantedPosterImageList> allWantedPosterImages = new ArrayList<>();
    private final List<IGameBase> minigames = new ArrayList<>();
    private final List<Tree_x_Game> tree_x_games = new ArrayList<>();

    public static ContentManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ContentManager.class) {
                if (INSTANCE == null) { // double checked locking
                    ContentManager newCM = new ContentManager();
                    newCM.context = context;
                    newCM.init();
                    INSTANCE = newCM;
                }
            }
        }
        return INSTANCE;
    }

    private void init() {
        ContentDatabase contentDb = ContentDatabase.getInstance(context);
        // Load tree (images + games)
        List<TreeRelations> treeRelations = contentDb.treeDao().getAll();
        for (TreeRelations treeRelation : treeRelations) {
            Tree tree = new Tree();
            tree.initContentData(treeRelation);
            this.trees.add(tree);
        }

        // Load wanted poster
        for (Tree tree : this.trees) {
            List<WantedPoster> wantedPosterTexts = contentDb.wantedPosterTextDao().getTextsByTreeId(tree.getId());
            List<WantedPosterImage> wantedPosterImgs = contentDb.wantedPosterImageDao().getImagesByTreeId(tree.getId());

            WantedPosterTextList textList = new WantedPosterTextList();
            WantedPosterImageList imageList = new WantedPosterImageList();
            textList.uid = tree.getId();
            imageList.uid = tree.getId();
            textList.wantedPosters = wantedPosterTexts;
            imageList.wantedPosterImages = wantedPosterImgs;
            this.allWantedPosters.add(textList);
            this.allWantedPosterImages.add(imageList);
        }

        this.tree_x_games.addAll(contentDb.tree_x_gameDao().getAll());

        // Load games from database
        this.minigames.addAll(contentDb.gameChooseAnswerDao().getAll());
        this.minigames.addAll(contentDb.gameCatchFruitsDao().getAll());
        this.minigames.addAll(contentDb.gameBaumoryDao().getAll());
        this.minigames.addAll(contentDb.gameDragDropDao().getAll());
        this.minigames.addAll(contentDb.gameOnlyDescriptionDao().getAll());
        this.minigames.addAll(contentDb.gameTakePictureDao().getAll());
        this.minigames.addAll(contentDb.gameOrderWordsDao().getAll());
        this.minigames.addAll(contentDb.gamePuzzleDao().getAll());
        this.minigames.addAll(contentDb.gameInputStringDao().getAll());
        this.minigames.addAll(contentDb.gameDescriptionDao().getAll());
    }

    public List<Tree> getTrees() {
        return trees;
    }

    public Single<Tree_x_Game> getTxgByCompositeKey(int treeId, int gameId, Tree.GameCategories gameCategory) {
        return ContentDatabase.getInstance(context).tree_x_gameDao().getByCompositeKey(treeId, gameId, gameCategory);
    }


    public List<Tree_x_Game> getTxg() {
        return tree_x_games;
    }

    public List<de.lmu.treeapp.contentClasses.trees.TreeProfile> getTreeProfiles() {
        return treeProfiles;
    }

    public List<de.lmu.treeapp.contentClasses.trees.WantedPosterTextList> getAllWantedPosters() {return allWantedPosters; }

    public List<de.lmu.treeapp.contentClasses.trees.WantedPosterImageList> getAllWantedPosterImages() {return allWantedPosterImages; }

    public List<IGameBase> getMinigames() {
        return minigames;
    }
}
