package de.lmu.treeapp.contentData.cms;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.IGameBase;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeProfile;
import de.lmu.treeapp.contentData.database.ContentDatabase;
import de.lmu.treeapp.contentData.database.entities.content.TreeProfileCard;
import de.lmu.treeapp.contentData.database.entities.content.TreeRelations;
import de.lmu.treeapp.contentData.database.entities.content.Tree_x_Game;
import io.reactivex.rxjava3.core.Single;


public class ContentManager {
    private static ContentManager INSTANCE;
    private Context context;
    private final List<Tree> trees = new ArrayList<>();
    private final List<TreeProfile> treeProfiles = new ArrayList<>();
    private final List<IGameBase> minigames = new ArrayList<>();

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

        // Load tree profile
        for (Tree tree : this.trees) {
            List<TreeProfileCard> treeProfileCards = contentDb.treeProfileDao().getCardsByTreeId(tree.getId());

            TreeProfile treeProfile = new TreeProfile();
            treeProfile.cards = treeProfileCards;
            this.treeProfiles.add(treeProfile);
        }

        // Load games from database
        this.minigames.addAll(contentDb.gameChooseAnswerDao().getAll());
        this.minigames.addAll(contentDb.gameBaumoryDao().getAll());
        this.minigames.addAll(contentDb.gameDragDropDao().getAll());
        this.minigames.addAll(contentDb.gameOnlyDescriptionDao().getAll());
        this.minigames.addAll(contentDb.gameTakePictureDao().getAll());
        this.minigames.addAll(contentDb.gameRhymeDao().getAll());
        this.minigames.addAll(contentDb.gamePuzzleDao().getAll());
    }

    public List<Tree> getTrees() {
        return trees;
    }

    public Single<Tree_x_Game> getTxgByCompositeKey(int treeId, int gameId, Tree.GameCategories gameCategory) {
        return ContentDatabase.getInstance(context).tree_x_gameDao().getByCompositeKey(treeId, gameId, gameCategory);
    }

    public List<de.lmu.treeapp.contentClasses.trees.TreeProfile> getTreeProfiles() {
        return treeProfiles;
    }

    public List<IGameBase> getMinigames() {
        return minigames;
    }
}
