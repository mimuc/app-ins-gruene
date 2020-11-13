package de.lmu.treeapp.contentData.cms;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.contentClasses.trees.Tree;

public class treeParser {

    List<Tree> trees;
    private Tree tree;
    private String text;

    public treeParser() {
        trees = new ArrayList<Tree>();
    }

    public List<Tree> getTrees() {
        return trees;
    }

    public List<Tree> parse(XmlPullParser parser) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("tree")) {
                            // create a new instance of employee
                            tree = new Tree();
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
                        } else if (tagname.equalsIgnoreCase("qrCode")) {
                            tree.qrCode = text.trim();
                        } else if (tagname.equalsIgnoreCase("leafGames")) {
                            tree.setGameIds(Tree.GameCategories.leaf, getGamesIds(text));
                        } else if (tagname.equalsIgnoreCase("fruitGames")) {
                            tree.setGameIds(Tree.GameCategories.fruit, getGamesIds(text));
                        } else if (tagname.equalsIgnoreCase("trunkGames")) {
                            tree.setGameIds(Tree.GameCategories.trunk, getGamesIds(text));
                        } else if (tagname.equalsIgnoreCase("otherGames")) {
                            tree.setGameIds(Tree.GameCategories.other, getGamesIds(text));
                        } else if (tagname.equalsIgnoreCase("imageTree")) {
                            tree.imageTree = text.trim();
                        } else if (tagname.equalsIgnoreCase("imageLeaf")) {
                            tree.imageLeaf = text.trim();
                        } else if (tagname.equalsIgnoreCase("imageFruit")) {
                            tree.imageFruit = text.trim();
                        } else if (tagname.equalsIgnoreCase("imageTrunk")) {
                            tree.imageTrunk = text.trim();
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


    private List<Integer> getGamesIds(String _text) {
        String[] nums = _text.split(",");
        List<Integer> gameIds = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            gameIds.add(Integer.parseInt(nums[i].trim()));
        }
        return gameIds;
    }
}
