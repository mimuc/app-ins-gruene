package de.lmu.treeapp.contentData.cms;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import de.lmu.treeapp.contentClasses.trees.TreeProfile;

public class treeProfileParser {

    List<TreeProfile> treeProfiles;
    private TreeProfile treeProfile;
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

