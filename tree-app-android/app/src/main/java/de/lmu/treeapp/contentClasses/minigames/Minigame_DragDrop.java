package de.lmu.treeapp.contentClasses.minigames;

import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.components.DragDropItem;
import de.lmu.treeapp.contentClasses.minigames.components.DragDropZone;

public class Minigame_DragDrop extends Minigame_Base {

    public List<DragDropItem> items;
    public List<DragDropZone> zones;
    public String layout = "";
}
