package de.lmu.treeapp.contentData.cms;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.Minigame_InputStringAnswer;
import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;


// TODO: Create own XML for each type of miniGame --> one Parse-function for each --> combine them --> OR: In parse, first check for modes and go into other methods depending on that.
public class miniGameParser {

    List<Minigame_Base> miniGames;
    private Minigame_Base miniGame;

    private Minigame_ChooseAnswer miniGameChooseAnswer;
    private AnswerOption answerOption;

    private Minigame_InputStringAnswer miniGameInputStringAnswer;
    private String text;

    public miniGameParser() {
        miniGames = new ArrayList<Minigame_Base>();
    }

    public List<Minigame_Base> getMiniGames() {
        return miniGames;
    }

    public List<Minigame_Base> parse(XmlPullParser parser) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("minigame_select")) {
                            miniGameChooseAnswer = new Minigame_ChooseAnswer();
                            miniGame = new Minigame_Base();
                            miniGame.type = "ChooseAnswer";
                        }
                        else if (tagname.equalsIgnoreCase("minigame_inputString")) {
                            miniGameInputStringAnswer = new Minigame_InputStringAnswer();
                            miniGame = new Minigame_Base();
                            miniGame.type = "InputStringAnswer";
                        }
                        else if (tagname.equalsIgnoreCase("option")){
                            answerOption = new AnswerOption();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("minigame_select")) {
                            miniGameChooseAnswer.FillBaseData(miniGame);
                            miniGames.add(miniGameChooseAnswer);
                        } else if (tagname.equalsIgnoreCase("minigame_inputString")) {
                            miniGameInputStringAnswer.FillBaseData(miniGame);
                            miniGames.add(miniGameInputStringAnswer);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            miniGame.uid = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("name")) {
                            miniGame.name = text;
                        } else if (tagname.equalsIgnoreCase("type")) {
                            miniGame.type = text;
                        } else if (tagname.equalsIgnoreCase("description")){
                            miniGame.description = text;
                        } else if (tagname.equalsIgnoreCase("image")){
                            miniGame.image = text;
                        } else if (tagname.equalsIgnoreCase("rightAnswer")){
                            miniGameInputStringAnswer.FillRightAnswer(text);
                        } else if (tagname.equalsIgnoreCase("option")){
                            miniGameChooseAnswer.options.add(answerOption);
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

        return miniGames;
    }
}

