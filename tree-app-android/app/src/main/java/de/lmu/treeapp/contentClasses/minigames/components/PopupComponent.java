package de.lmu.treeapp.contentClasses.minigames.components;


public class PopupComponent {
    public enum PopupTypes {
        rightPopup, falsePopup
    }

    public PopupTypes type = PopupTypes.rightPopup;
    public String content;
    public Boolean right = false;

    public PopupComponent(){}
    public PopupComponent(PopupTypes _type, String _content, Boolean _right){
        type = _type;
        content = _content;
        right = _right;
    }
    public PopupComponent(PopupTypes _type, String _content){
        type = _type;
        content = _content;
        right = false;
    }

    public void SetType(PopupTypes _type){
        this.type = _type;
    }
    public void SetType(String _type){
        if (_type.trim().equalsIgnoreCase("rightPopup")){
            this.type = PopupTypes.rightPopup;
        }
        else {
            this.type = PopupTypes.falsePopup;
        }
    }
}
