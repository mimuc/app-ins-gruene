package de.lmu.treeapp.contentClasses.minigames.components;

public class AnswerOption {

    public enum OptionTypes {
        text, image
    }

    public OptionTypes type;
    public String content;
    public Boolean right;

    public AnswerOption(){

    }
    public AnswerOption(OptionTypes _type, String _content, Boolean _right){
        type = _type;
        content = _content;
        right = _right;
    }
    public AnswerOption(OptionTypes _type, String _content){
        type = _type;
        content = _content;
        right = false;
    }

}
