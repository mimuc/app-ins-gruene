package de.lmu.treeapp.contentData.cms;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeProfile;
import de.lmu.treeapp.contentClasses.trees.TreeProfileCard;

public class treeProfileParser {

    List<TreeProfile> treeProfiles;
    private TreeProfile treeProfile;
    private TreeProfileCard treeProfileCard;
    private String text;

    public treeProfileParser() {
        treeProfiles = new ArrayList<TreeProfile>();
    }

    public List<TreeProfile> getTreeProfiles() {
        return treeProfiles;
    }

    public List<TreeProfile> parse(XmlPullParser parser) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("treeProfile")) {
                            // create a new instance of employee
                            treeProfile = new TreeProfile();
                            treeProfile.cards = new ArrayList<>();
                        }
                        else if (tagname.equalsIgnoreCase("card")){
                            treeProfileCard = new TreeProfileCard();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("treeProfile")) {
                            treeProfiles.add(treeProfile);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            treeProfile.uid = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("card_name")){
                            treeProfileCard.name = text.trim();
                        } else if (tagname.equalsIgnoreCase("card_unlockedBy")){
                            switch (text){
                                case "leaf":
                                    treeProfileCard.unlockedBy = Tree.GameCategories.leaf;
                                    break;
                                case "fruit":
                                    treeProfileCard.unlockedBy = Tree.GameCategories.fruit;
                                    break;
                                case "trunk":
                                    treeProfileCard.unlockedBy = Tree.GameCategories.trunk;
                                    break;
                            }
                        } else if (tagname.equalsIgnoreCase("card_content")){
                            treeProfileCard.content = text;
                        } else if (tagname.equalsIgnoreCase("card")){
                            treeProfile.cards.add(treeProfileCard);
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

        return treeProfiles;
    }
}

