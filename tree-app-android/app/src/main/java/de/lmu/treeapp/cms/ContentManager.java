package de.lmu.treeapp.cms;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.database.entities.TreeModel;
import de.lmu.treeapp.database.entities.TreeProfileModel;


public class ContentManager {
    private  static ContentManager INSTANCE;
    private static final Object sLock = new Object();
    private Context context;
    private List<TreeModel> trees;
    private List<TreeProfileModel> treeProfiles;

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

    private void init(){
        List<TreeModel> _trees = null;
        treeParser parser = new treeParser();
        _trees = parser.parse(context.getResources().getXml(R.xml.trees));
        this.trees = _trees;

        List<TreeProfileModel> _treeProfiles = null;
        treeProfileParser parser2 = new treeProfileParser();
        _treeProfiles = parser2.parse(context.getResources().getXml(R.xml.treeprofiles));
        this.treeProfiles = _treeProfiles;
    }

    public List<TreeModel> getTrees(){
        return trees;
    }
    public List<TreeProfileModel> getTreeProfiles(){
        return treeProfiles;
    }

}
