package de.lmu.treeapp.contentClasses.minigames;

public class Minigame_Base implements IGameBase{
    public int uid;
    public String name;
    public MinigameTypes type;
    public String description;
    public String image;

    public enum MinigameTypes {
        ChooseAnswer, InputString, TakePicture, DragDrop, OnlyDescription, Baumory, OrderWords, Puzzle, Undefined
    }

    @Override
    public int getId() {
        return uid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MinigameTypes getType() {
        return type;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getImageResource() {
        return image;
    }
}


