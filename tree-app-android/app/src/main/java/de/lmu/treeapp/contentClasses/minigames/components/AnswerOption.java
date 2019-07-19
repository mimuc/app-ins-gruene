package de.lmu.treeapp.contentClasses.minigames.components;

import android.graphics.Path;

public class AnswerOption {

  public enum OptionTypes {
    text, image
  }

  public OptionTypes type = OptionTypes.text;
  public String content;
  public Boolean right = false;

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

  public void SetType(OptionTypes _type){
    this.type = _type;
  }
  public void SetType(String _type){
    if (_type.trim().equalsIgnoreCase("image")){
      this.type = OptionTypes.image;
    }
    else {
      this.type = OptionTypes.text;
    }
  }


}
