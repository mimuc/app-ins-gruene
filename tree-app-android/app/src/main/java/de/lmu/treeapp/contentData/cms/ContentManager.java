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


public class ContentManager {
    private static final Object sLock = new Object();
    private static ContentManager INSTANCE;
    private Context context;
    private List<Tree> trees = new ArrayList<>();
    private List<TreeProfile> treeProfiles = new ArrayList<>();
    private List<IGameBase> minigames = new ArrayList<>();

    public static ContentManager getInstance(Context _context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                ContentManager newCM = new ContentManager();
                newCM.context = _context;
                newCM.init();
                INSTANCE = newCM;
            }
            return INSTANCE;
        }
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

        miniGameParser parser = new miniGameParser();
        this.minigames.addAll(parser.getMiniGames(context));
    }


    public List<Tree> getTrees() {
        return trees;
    }

    public List<de.lmu.treeapp.contentClasses.trees.TreeProfile> getTreeProfiles() {
        return treeProfiles;
    }

    public List<IGameBase> getMinigames() {
        return minigames;
    }


}
