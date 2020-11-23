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
        trees = new ArrayList<>();
    }

    public List<Tree> getTrees() {
        return trees;
    }

    public List<Tree> parse(XmlPullParser parser) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("tree")) {
                            // create a new instance of employee
                            tree = new Tree();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase("tree")) {
                            trees.add(tree);
                        } else if (tagName.equalsIgnoreCase("name")) {
                            tree.name = text;
                        } else if (tagName.equalsIgnoreCase("id")) {
                            tree.uid = Integer.parseInt(text);
                        } else if (tagName.equalsIgnoreCase("profileId")) {
                            tree.profileId = Integer.parseInt(text);
                        } else if (tagName.equalsIgnoreCase("qrCode")) {
                            tree.qrCode = text.trim();
                        } else if (tagName.equalsIgnoreCase("leafGames")) {
                            tree.setGameIds(Tree.GameCategories.leaf, getGamesIds(text));
                        } else if (tagName.equalsIgnoreCase("fruitGames")) {
                            tree.setGameIds(Tree.GameCategories.fruit, getGamesIds(text));
                        } else if (tagName.equalsIgnoreCase("trunkGames")) {
                            tree.setGameIds(Tree.GameCategories.trunk, getGamesIds(text));
                        } else if (tagName.equalsIgnoreCase("otherGames")) {
                            tree.setGameIds(Tree.GameCategories.other, getGamesIds(text));
                        } else if (tagName.equalsIgnoreCase("imageTree")) {
                            tree.imageTree = text.trim();
                        } else if (tagName.equalsIgnoreCase("imageLeaf")) {
                            tree.imageLeaf = text.trim();
                        } else if (tagName.equalsIgnoreCase("imageFruit")) {
                            tree.imageFruit = text.trim();
                        } else if (tagName.equalsIgnoreCase("imageTrunk")) {
                            tree.imageTrunk = text.trim();
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return trees;
    }


    private List<Integer> getGamesIds(String _text) {
        String[] nums = _text.split(",");
        List<Integer> gameIds = new ArrayList<>();
        for (String num : nums) {
            gameIds.add(Integer.parseInt(num.trim()));
        }
        return gameIds;
    }
}
