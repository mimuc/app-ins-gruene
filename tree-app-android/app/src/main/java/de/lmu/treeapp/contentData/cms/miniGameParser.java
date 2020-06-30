package de.lmu.treeapp.contentData.cms;
import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Baumory;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.Minigame_DragDrop;
import de.lmu.treeapp.contentClasses.minigames.Minigame_InputStringAnswer;
import de.lmu.treeapp.contentClasses.minigames.Minigame_OnlyDescription;
import de.lmu.treeapp.contentClasses.minigames.Minigame_TakePicture;
import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;
import de.lmu.treeapp.contentClasses.minigames.components.BaumoryCard;
import de.lmu.treeapp.contentClasses.minigames.components.DragDropItem;
import de.lmu.treeapp.contentClasses.minigames.components.DragDropZone;


public class miniGameParser {

    List<Minigame_Base> miniGames;
    private String text;

    public miniGameParser() {
        miniGames = new ArrayList<Minigame_Base>();
    }
    public List<Minigame_Base> getMiniGames(Context context) {
        parse_ChooseAnswer(context.getResources().getXml(R.xml.minigames_chooseanswer), 100);
        parse_InputString(context.getResources().getXml(R.xml.minigames_inputstring), 200);
        parse_TakePicture(context.getResources().getXml(R.xml.minigames_takepicture), 300);
        parse_DragDrop(context.getResources().getXml(R.xml.minigames_dragdrop),400);
        parse_OnlyDescription(context.getResources().getXml(R.xml.minigames_onlydescription),500);
        parse_Baumory(context.getResources().getXml(R.xml.minigames_baumory),600);
        return miniGames;
    }

    // MiniGame_ChooseAnswer
    private Minigame_ChooseAnswer miniGameChooseAnswer;
    private AnswerOption answerOption;

    public void parse_ChooseAnswer(XmlPullParser parser, int prefix) {
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
                            miniGameChooseAnswer.uid = Integer.parseInt(text) + prefix;
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

    public void parse_InputString(XmlPullParser parser, int prefix) {
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
                            miniGameInputStringAnswer.uid = Integer.parseInt(text) + prefix;
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

    public void parse_TakePicture(XmlPullParser parser, int prefix) {
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
                            miniGames.add(miniGameTakePicture);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            miniGameTakePicture.uid = Integer.parseInt(text) + prefix;
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


    // MiniGame_DragDrop
    private Minigame_DragDrop miniGameDragDrop;
    private DragDropItem dragDropItem;
    private DragDropZone dragDropZone;

    public void parse_DragDrop(XmlPullParser parser, int prefix) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("minigame_dragDrop")) {
                            miniGameDragDrop = new Minigame_DragDrop();
                            miniGameDragDrop.type = Minigame_Base.MinigameTypes.DragDrop;
                            miniGameDragDrop.items = new ArrayList<>();
                            miniGameDragDrop.zones = new ArrayList<>();
                        }
                        else if (tagname.equalsIgnoreCase("dragItem")){
                            dragDropItem = new DragDropItem();
                        }
                        else if (tagname.equalsIgnoreCase("dropItem")){
                            dragDropZone = new DragDropZone();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("minigame_dragDrop")) {
                            miniGames.add(miniGameDragDrop);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            miniGameDragDrop.uid = Integer.parseInt(text) + prefix;
                        } else if (tagname.equalsIgnoreCase("name")) {
                            miniGameDragDrop.name = text;
                        } else if (tagname.equalsIgnoreCase("description")){
                            miniGameDragDrop.description = text;
                        } else if (tagname.equalsIgnoreCase("image")){
                            miniGameDragDrop.image = text;
                        } else if (tagname.equalsIgnoreCase("layout")){
                            miniGameDragDrop.layout = text;
                        } else if (tagname.equalsIgnoreCase("dragItem_type")){
                            dragDropItem.SetType(text);
                        } else if (tagname.equalsIgnoreCase("dragItem_content")){
                            dragDropItem.content = (text);
                        } else if (tagname.equalsIgnoreCase("dragItem_match")) {
                            dragDropItem.match = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("dragItem_x")){
                            dragDropItem.x = Float.parseFloat(text);
                        } else if (tagname.equalsIgnoreCase("dragItem_y")) {
                            dragDropItem.y = Float.parseFloat(text);
                        } else if (tagname.equalsIgnoreCase("dragItem_w")){
                            dragDropItem.w = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("dragItem_h")){
                            dragDropItem.h = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("dropItem_match")){
                            dragDropZone.match = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("dropItem_content")){
                            dragDropZone.content = (text);
                        } else if (tagname.equalsIgnoreCase("dropItem_x")){
                            dragDropZone.x = Float.parseFloat(text);
                        } else if (tagname.equalsIgnoreCase("dropItem_y")){
                            dragDropZone.y = Float.parseFloat(text);
                        } else if (tagname.equalsIgnoreCase("dropItem_w")){
                            dragDropZone.w = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("dropItem_h")){
                            dragDropZone.h = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("dragItem")){
                            miniGameDragDrop.items.add(dragDropItem);
                        } else if (tagname.equalsIgnoreCase("dropItem")){
                            miniGameDragDrop.zones.add(dragDropZone);
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


    // MiniGame_OnlyDescription
    private Minigame_OnlyDescription miniGameOnlyDescription;

    public void parse_OnlyDescription(XmlPullParser parser, int prefix) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("minigame_onlyDescription")) {
                            miniGameOnlyDescription = new Minigame_OnlyDescription();
                            miniGameOnlyDescription.type = Minigame_Base.MinigameTypes.OnlyDescription;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("minigame_onlyDescription")) {
                            miniGames.add(miniGameOnlyDescription);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            miniGameOnlyDescription.uid = Integer.parseInt(text) + prefix;
                        } else if (tagname.equalsIgnoreCase("name")) {
                            miniGameOnlyDescription.name = text;
                        } else if (tagname.equalsIgnoreCase("description")){
                            miniGameOnlyDescription.description = text;
                        } else if (tagname.equalsIgnoreCase("image")){
                            miniGameOnlyDescription.image = text;
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


    // MiniGame_Baumory
    private Minigame_Baumory minigameBaumory;
    private BaumoryCard card;

    public void parse_Baumory(XmlPullParser parser, int prefix) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("minigame_baumory")) {
                            minigameBaumory = new Minigame_Baumory();
                            minigameBaumory.type = Minigame_Base.MinigameTypes.Baumory;
                            minigameBaumory.cards = new ArrayList<>();
                        }
                        else if (tagname.equalsIgnoreCase("card")){
                            card = new BaumoryCard();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("minigame_baumory")) {
                            miniGames.add(minigameBaumory);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            minigameBaumory.uid = Integer.parseInt(text) + prefix;
                        } else if (tagname.equalsIgnoreCase("name")) {
                            minigameBaumory.name = text;
                        } else if (tagname.equalsIgnoreCase("description")){
                            minigameBaumory.description = text;
                        } else if (tagname.equalsIgnoreCase("image")){
                            minigameBaumory.image = text;
                        }  else if (tagname.equalsIgnoreCase("card_image")){
                            card.content = (text);
                        } else if (tagname.equalsIgnoreCase("card_match")){
                            card.match = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("card")){
                            minigameBaumory.cards.add(card);
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

