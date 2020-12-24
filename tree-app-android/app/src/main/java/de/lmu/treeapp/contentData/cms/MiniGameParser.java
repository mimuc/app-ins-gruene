package de.lmu.treeapp.contentData.cms;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_InputStringAnswer;


public class MiniGameParser {

    List<Minigame_Base> miniGames;
    private String text;

    public MiniGameParser() {
        miniGames = new ArrayList<>();
    }

    public List<Minigame_Base> getMiniGames(Context context) {
        parse_InputString(context.getResources().getXml(R.xml.minigames_inputstring), 200);
        return miniGames;
    }

    // MiniGame_InputString
    private Minigame_InputStringAnswer miniGameInputStringAnswer;

    public void parse_InputString(XmlPullParser parser, int prefix) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("minigame_inputString")) {
                            miniGameInputStringAnswer = new Minigame_InputStringAnswer();
                            miniGameInputStringAnswer.type = Minigame_Base.MinigameTypes.InputString;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase("minigame_inputString")) {
                            miniGames.add(miniGameInputStringAnswer);
                        } else if (tagName.equalsIgnoreCase("id")) {
                            miniGameInputStringAnswer.uid = Integer.parseInt(text) + prefix;
                        } else if (tagName.equalsIgnoreCase("name")) {
                            miniGameInputStringAnswer.name = text;
                        } else if (tagName.equalsIgnoreCase("description")) {
                            miniGameInputStringAnswer.description = text;
                        } else if (tagName.equalsIgnoreCase("image")) {
                            miniGameInputStringAnswer.image = text;
                        } else if (tagName.equalsIgnoreCase("rightAnswer")) {
                            miniGameInputStringAnswer.FillRightAnswer(text);
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
    }
}

