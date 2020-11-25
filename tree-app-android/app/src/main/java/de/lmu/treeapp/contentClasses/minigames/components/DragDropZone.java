package de.lmu.treeapp.contentClasses.minigames.components;

public class DragDropZone {

    public String content;
    public int id;
    public int match = -1;
    public float x = 0;
    public float y = 0;
    public int w = 0;
    public int h = 0;
    public Boolean valideMatch = false;


    public DragDropItem currentItem = null;

    public void setCurrentItem(DragDropItem currentItem) {
        this.currentItem = currentItem;
    }

    public void setValideMatch(Boolean valideMatch) {
        this.valideMatch = valideMatch;
    }

    public boolean IsMatchedRight() {
        if (currentItem == null) return false;
        else return currentItem.match == match && currentItem.match >= 0;
    }

    public boolean IsFilled() {
        return currentItem != null;
    }
}
