package de.lmu.treeapp.contentClasses.minigames;

public class Minigame_Base {
    public int uid;
    public String name;
    public String type;
    public String description;
    public String image;

    public void FillBaseData(Minigame_Base miniGame){
        this.uid = miniGame.uid;
        this.name = miniGame.name;
        this.type = miniGame.type;
        this.description = miniGame.description;
        this.image = miniGame.image;
    }
}


