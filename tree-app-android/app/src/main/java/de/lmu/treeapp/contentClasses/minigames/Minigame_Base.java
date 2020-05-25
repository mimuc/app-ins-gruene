package de.lmu.treeapp.contentClasses.minigames;

import de.lmu.treeapp.contentClasses.trees.Tree;

public class Minigame_Base {
    public int uid;
    public String name;
    public MinigameTypes type;
    public String description;
    public String image;


    public enum MinigameTypes {
        ChooseAnswer, InputString, TakePicture, DragDrop, OnlyDescription, Baumory
    }

    public void FillBaseData(Minigame_Base miniGame){
        this.uid = miniGame.uid;
        this.name = miniGame.name;
        this.type = miniGame.type;
        this.description = miniGame.description;
        this.image = miniGame.image;
    }

    /* public int getUid(Minigame_Base game){
        return game.uid;
    }*/
}


