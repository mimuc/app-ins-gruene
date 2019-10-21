package de.lmu.treeapp.contentData.cms;
import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.Minigame_InputStringAnswer;
import de.lmu.treeapp.contentClasses.minigames.Minigame_TakePicture;
import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;


public class miniGameParser {

    List<Minigame_Base> miniGames;
    private String text;
    public miniGameParser() {
        miniGames = new ArrayList<Minigame_Base>();
    }
    public List<Minigame_Base> getMiniGames(Context context) {

        parse_ChooseAnswer(context.getResources().getXml(R.xml.minigames_chooseanswer));
        parse_InputString(context.getResources().getXml(R.xml.minigames_inputstring));
        parse_TakePicture(context.getResources().getXml(R.xml.minigames_takepicture));

        return miniGames;
    }

    // MiniGame_ChooseAnswer
    private Minigame_ChooseAnswer miniGameChooseAnswer;
    private AnswerOption answerOption;

    public void parse_ChooseAnswer(XmlPullParser parser) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("minigame_chooseAnswer")) {
                            miniGameChooseAnswer = new Minigame_ChooseAnswer();
                            miniGameChooseAnswer.type = Minigame_Base.MinigameTypes.ChooseAnswer;
                            miniGameChooseAnswer.options = new ArrayList<>();
                        }
                        else if (tagname.equalsIgnoreCase("option")){
                            answerOption = new AnswerOption();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("minigame_chooseAnswer")) {
                            miniGames.add(miniGameChooseAnswer);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            miniGameChooseAnswer.uid = Integer.parseInt(text) + 100;
                        } else if (tagname.equalsIgnoreCase("name")) {
                            miniGameChooseAnswer.name = text;
                        } else if (tagname.equalsIgnoreCase("description")){
                            miniGameChooseAnswer.description = text;
                        } else if (tagname.equalsIgnoreCase("image")){
                            miniGameChooseAnswer.image = text;
                        } else if (tagname.equalsIgnoreCase("option_type")){
                            answerOption.SetType(text);
                        } else if (tagname.equalsIgnoreCase("option_content")){
                            answerOption.content = (text);
                        } else if (tagname.equalsIgnoreCase("option_right")){
                            answerOption.right = Boolean.parseBoolean(text);
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
    }

    // MiniGame_InputString
    private Minigame_InputStringAnswer miniGameInputStringAnswer;

    public void parse_InputString(XmlPullParser parser) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("minigame_inputString")) {
                            miniGameInputStringAnswer = new Minigame_InputStringAnswer();
                            miniGameInputStringAnswer.type = Minigame_Base.MinigameTypes.InputString;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("minigame_inputString")) {
                            miniGames.add(miniGameInputStringAnswer);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            miniGameInputStringAnswer.uid = Integer.parseInt(text) + 200;
                        } else if (tagname.equalsIgnoreCase("name")) {
                            miniGameInputStringAnswer.name = text;
                        } else if (tagname.equalsIgnoreCase("description")){
                            miniGameInputStringAnswer.description = text;
                        } else if (tagname.equalsIgnoreCase("image")){
                            miniGameInputStringAnswer.image = text;
                        } else if (tagname.equalsIgnoreCase("rightAnswer")){
                            miniGameInputStringAnswer.FillRightAnswer(text);
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
    }


    // MiniGame_TakePicture
    private Minigame_TakePicture miniGameTakePicture;

    public void parse_TakePicture(XmlPullParser parser) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("minigame_takePicture")) {
                            miniGameTakePicture = new Minigame_TakePicture();
                            miniGameTakePicture.type = Minigame_Base.MinigameTypes.TakePicture;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("minigame_takePicture")) {
                            miniGames.add(miniGameInputStringAnswer);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            miniGameTakePicture.uid = Integer.parseInt(text) + 300;
                        } else if (tagname.equalsIgnoreCase("name")) {
                            miniGameTakePicture.name = text;
                        } else if (tagname.equalsIgnoreCase("description")){
                            miniGameTakePicture.description = text;
                        } else if (tagname.equalsIgnoreCase("image")){
                            miniGameTakePicture.image = text;
                        } else if (tagname.equalsIgnoreCase("pictureName")){
                            miniGameTakePicture.FillPictureName(text);
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
    }
}

