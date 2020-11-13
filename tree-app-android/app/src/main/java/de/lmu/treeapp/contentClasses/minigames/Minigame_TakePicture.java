package de.lmu.treeapp.contentClasses.minigames;

public class Minigame_TakePicture extends Minigame_Base {

    private String pictureName = "";

    public void FillPictureName(String _name) {
        pictureName = _name.trim().toLowerCase();
    }

    public String GetPictureName() {
        return pictureName;
    }

}
