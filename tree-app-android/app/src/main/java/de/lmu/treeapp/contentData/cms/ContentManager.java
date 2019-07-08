package de.lmu.treeapp.contentData.cms;

import android.content.Context;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeProfile;


public class ContentManager {
    private  static ContentManager INSTANCE;
    private static final Object sLock = new Object();
    private Context context;
    private List<Tree> trees;
    private List<TreeProfile> treeProfiles;

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
        List<Tree> _trees = null;
        treeParser parser = new treeParser();
        _trees = parser.parse(context.getResources().getXml(R.xml.trees));
        this.trees = _trees;

        List<TreeProfile> _treeProfiles = null;
        treeProfileParser parser2 = new treeProfileParser();
        _treeProfiles = parser2.parse(context.getResources().getXml(R.xml.treeprofiles));
        this.treeProfiles = _treeProfiles;
    }

    public List<Tree> getTrees(){
        return trees;
    }
    public List<TreeProfile> getTreeProfiles(){
        return treeProfiles;
    }

}
