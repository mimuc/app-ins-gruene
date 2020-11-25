package de.lmu.treeapp.contentClasses.minigames.components;

public class DragDropItem {

    public enum Types {
        text, image
    }

    public int id;
    public Types type = Types.text;
    public String content;
    public int match = -1;
    public float x = 0;
    public float y = 0;
    public int w = 0;
    public int h = 0;

    public void SetType(String _type) {
        if (_type.trim().equalsIgnoreCase("image")) {
            this.type = Types.image;
        } else {
            this.type = Types.text;
        }
    }
}
