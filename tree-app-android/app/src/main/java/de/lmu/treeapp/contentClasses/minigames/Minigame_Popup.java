package de.lmu.treeapp.contentClasses.minigames;

import java.util.List;
import de.lmu.treeapp.contentClasses.minigames.components.PopupComponent;

public class Minigame_Popup extends Minigame_Base {

    public List<PopupComponent> popUps;

    public Boolean CheckPopup(PopupComponent popupComponent){
        if (popupComponent.right) return true;
        else return false;
    }
}


