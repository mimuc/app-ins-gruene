package de.lmu.treeapp.contentData;

import android.content.Context;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeProfile;
import de.lmu.treeapp.contentData.cms.ContentManager;
import de.lmu.treeapp.contentData.database.AppDatabase;
import de.lmu.treeapp.contentData.database.entities.TreeModel;

public class DataManager {
    private  static DataManager INSTANCE;
    private static final Object sLock = new Object();
    private Context context;

    public List<Tree> trees;

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

    private void SetData(List<Tree> _trees){
        this.trees = _trees;
    }

    private void Init(){
        new Thread(new Runnable() {
            @Override
            public void run(){
                List<Tree> CMS_trees = ContentManager.getInstance(context).getTrees();
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
                DataManager.getInstance(context).SetData(CMS_trees);
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
        TreeModel model = _tree.changeable;
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
        AppDatabase.getInstance(context).treeDao().Update(model);
    }
}
