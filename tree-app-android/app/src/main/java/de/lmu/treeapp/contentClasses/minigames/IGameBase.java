package de.lmu.treeapp.contentClasses.minigames;

public interface IGameBase {

    int getId();

    String getName();

    Minigame_Base.MinigameTypes getType();

    String getDescription();

    String getImageResource();
}
