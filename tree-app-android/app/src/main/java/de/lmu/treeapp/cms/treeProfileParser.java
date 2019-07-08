package de.lmu.treeapp.cms;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import de.lmu.treeapp.database.entities.TreeProfileModel;

public class treeProfileParser {

    List<TreeProfileModel> treeProfiles;
    private TreeProfileModel treeProfile;
    private String text;

    public treeProfileParser() {
        treeProfiles = new ArrayList<TreeProfileModel>();
    }

    public List<TreeProfileModel> getTrees() {
        return treeProfiles;
    }

    public List<TreeProfileModel> parse(XmlPullParser parser) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("treeProfile")) {
                            // create a new instance of employee
                            treeProfile = new TreeProfileModel();
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

