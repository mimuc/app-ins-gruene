package de.lmu.treeapp.contentData.cms;

import android.content.Context;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeProfile;


public class ContentManager {
    private  static ContentManager INSTANCE;
    private static final Object sLock = new Object();
    private Context context;
    private List<Tree> trees;
    private List<TreeProfile> treeProfiles;
    private List<Minigame_Base> minigames;

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
        treeParser parser = new treeParser();
        this.trees = parser.parse(context.getResources().getXml(R.xml.trees));

        treeProfileParser parser2 = new treeProfileParser();
        this.treeProfiles = parser2.parse(context.getResources().getXml(R.xml.treeprofiles));

        this.minigames = parseAllMinigames();
    }

    private List<Minigame_Base> parseAllMinigames(){
        miniGameParser gameParser = new miniGameParser();
        gameParser.parse_ChooseAnswer(context.getResources().getXml(R.xml.minigames_chooseanswer));
        gameParser.parse_InputString(context.getResources().getXml(R.xml.minigames_inputstring));
        return gameParser.getMiniGames();
    }

    public List<Tree> getTrees(){
        return trees;
    }
    public List<TreeProfile> getTreeProfiles(){
        return treeProfiles;
    }
    public List<Minigame_Base> getMinigames() {return minigames;}

}
