package de.lmu.treeapp.contentData;

import android.content.Context;
import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeProfile;
import de.lmu.treeapp.contentData.cms.ContentManager;
import de.lmu.treeapp.contentData.database.AppDatabase;
import de.lmu.treeapp.contentData.database.entities.PlayerModel;
import de.lmu.treeapp.contentData.database.entities.TreeModel;

public class DataManager {
    private  static DataManager INSTANCE;
    private static final Object sLock = new Object();
    private Context context;

    public Boolean loaded = false;
    public List<Tree> trees;
    public List<TreeProfile> treeProfiles;
    public List<Minigame_Base> miniGames;
    public PlayerModel player;

    public static DataManager getInstance(Context _context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                DataManager newCM = new DataManager();
                newCM.context = _context;
                newCM.Init();
                INSTANCE = newCM;
            }
            return INSTANCE;
        }
    }

    private void SetData(List<Tree> _trees, List<TreeProfile> _treeProfiles, List<Minigame_Base> _minigames, PlayerModel _player){
        this.trees = _trees;
        this.treeProfiles = _treeProfiles;
        this.miniGames = _minigames;
        this.player = _player;
        this.loaded = true;
    }

    private void Init(){
        new Thread(new Runnable() {
            @Override
            public void run(){
                // Saved Name
                PlayerModel DB_player = AppDatabase.getInstance(context).playerDao().get();
                if (DB_player == null){
                    DB_player = new PlayerModel();
                    AppDatabase.getInstance(context).playerDao().InsertOne(DB_player);
                }
                List<Tree> CMS_trees = ContentManager.getInstance(context).getTrees();
                List<TreeProfile> CMS_treeProfiles = ContentManager.getInstance(context).getTreeProfiles();
                List<Minigame_Base> CMS_miniGames = ContentManager.getInstance(context).getMinigames();
                List<TreeModel> DB_trees = AppDatabase.getInstance(context).treeDao().getAll();
                for (int i = 0; i < CMS_trees.size(); i++) {
                    Tree cmsTree = CMS_trees.get(i);
                    Boolean initByDB = false;
                    for (int j = 0; j < DB_trees.size(); j++) {
                        TreeModel dbTree = DB_trees.get(j);
                        if (cmsTree.uid == dbTree.uid) {
                            cmsTree.InitFromDB(dbTree);
                            initByDB = true;
                            break;
                        }
                    }
                    if (initByDB == false) {
                        TreeModel newDBTree = new TreeModel();
                        newDBTree.InitDefault(cmsTree.uid);
                        cmsTree.InitFromDB(newDBTree);
                        AppDatabase.getInstance(context).treeDao().InsertOne(newDBTree);
                    }
                }
                DataManager.getInstance(context).SetData(CMS_trees, CMS_treeProfiles, CMS_miniGames, DB_player);
            }
        }).start();
    }


    // Player-Stuff
    public String GetPlayerName(){
        return player.name;
    }
    public String SetPlayerName(String _name){
        player.name = _name;
        new Thread(new Runnable() {
            @Override
            public void run(){
                AppDatabase.getInstance(context).playerDao().UpdateOne(player);
            }
        }).start();
        return player.name;
    }

    // Get something
    public Minigame_Base GetMinigame(int id){
        if (miniGames == null) return null;
        for (int i = 0; i < miniGames.size(); i++){
            if (miniGames.get(i).uid == id){
                return miniGames.get(i);
            }
        }
        return null;
    }
    public Tree GetTreeByQR(String _qrCode){
        _qrCode = _qrCode.trim();
        if (trees == null) return null;
        for (int i = 0; i < trees.size(); i++){
            if (_qrCode.equalsIgnoreCase(trees.get(i).qrCode)){
                return trees.get(i);
            }
        }
        return null;
    }
    public Tree GetTree(int id){
        if (trees == null) return null;
        for (int i = 0; i < trees.size(); i++){
            if (trees.get(i).uid == id){
                return trees.get(i);
            }
        }
        return null;
    }
    public TreeProfile GetTreeProfile(int id){
        if (treeProfiles == null) return null;
        for (int i = 0; i < treeProfiles.size(); i++){
            if (treeProfiles.get(i).uid == id){
                return treeProfiles.get(i);
            }
        }
        return null;
    }

    // Unlocked a Tree
    public void UnlockTree(Tree _tree){
        final TreeModel model = _tree.changeable;
        model.unlocked = true;
        new Thread(new Runnable() {
            @Override
            public void run(){
                AppDatabase.getInstance(context).treeDao().Update(model);
            }
        }).start();
    }

    // GameCompleted overloaded Functions
    public void GameCompleted(Tree.GameCategories _category, Minigame_Base _game, Tree _tree){
        GameCompleted(_category, _game.uid, _tree);
    }
    public void GameCompleted(Tree.GameCategories _category, Minigame_Base _game, int _treeId){
        if (trees.isEmpty() || trees == null) return;
        for (int i = 0; i < trees.size(); i++){
            if (_treeId == trees.get(i).uid){
                GameCompleted(_category,_game.uid,trees.get(i));
            }
        }
    }
    public void GameCompleted(Tree.GameCategories _category, int _gameId, int _treeId){
        if (trees.isEmpty() || trees == null) return;
        for (int i = 0; i < trees.size(); i++){
            if (_treeId == trees.get(i).uid){
                GameCompleted(_category,_gameId,trees.get(i));
            }
        }
    }
    public void GameCompleted(Tree.GameCategories _category, int _gameId, Tree _tree){
        final TreeModel model = _tree.changeable;
        switch (_category){
            case leaf:
                model.leafGamesCompleted.add(_gameId);
                break;
            case fruit:
                model.fruitGamesCompleted.add(_gameId);
                break;
            case trunk:
                model.trunkGamesCompleted.add(_gameId);
                break;
            case other:
                model.otherGamesCompleted.add(_gameId);
                break;
            default:
                break;
        }
        new Thread(new Runnable() {
            @Override
            public void run(){
                AppDatabase.getInstance(context).treeDao().Update(model);
            }
        }).start();
    }
}
