package de.lmu.treeapp.cms;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import de.lmu.treeapp.database.entities.TreeModel;

public class treeParser {

    List<TreeModel> trees;
    private TreeModel tree;
    private String text;

    public treeParser() {
        trees = new ArrayList<TreeModel>();
    }

    public List<TreeModel> getTrees() {
        return trees;
    }

    public List<TreeModel> parse(XmlPullParser parser) {
        //XmlPullParserFactory factory = null;
        //XmlPullParser parser = null;
        try {
            //factory = XmlPullParserFactory.newInstance();
            //factory.setNamespaceAware(true);
            //parser = factory.newPullParser();

            //parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("tree")) {
                            // create a new instance of employee
                            tree = new TreeModel();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("tree")) {
                            trees.add(tree);
                        } else if (tagname.equalsIgnoreCase("name")) {
                            tree.name = text;
                        } else if (tagname.equalsIgnoreCase("id")) {
                            tree.uid = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("profileId")) {
                            tree.profileId = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("leafGames")) {
                            tree.setGameIds(TreeModel.GameCategories.leaf, getGamesIds(text));
                        } else if (tagname.equalsIgnoreCase("fruitGames")) {
                            tree.setGameIds(TreeModel.GameCategories.fruit, getGamesIds(text));
                        } else if (tagname.equalsIgnoreCase("trunkGames")) {
                            tree.setGameIds(TreeModel.GameCategories.trunk, getGamesIds(text));
                        } else if (tagname.equalsIgnoreCase("otherGames")) {
                            tree.setGameIds(TreeModel.GameCategories.other, getGamesIds(text));
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return trees;
    }


    private int[] getGamesIds(String _text){
        String[] nums = _text.split(",");
        int[] gameIds = new int[nums.length];
        for (int i = 0; i < nums.length; i++){
            gameIds[i] = Integer.parseInt(nums[i]);
        }
        return gameIds;
    }
}
