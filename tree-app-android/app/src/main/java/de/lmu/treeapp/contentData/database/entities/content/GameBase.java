package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import de.lmu.treeapp.contentClasses.minigames.IGameBase;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;

@Entity
public class GameBase implements IGameBase {
    @PrimaryKey
    protected int id;
    protected String name;
    protected String description;
    protected String imageResource;
    protected Minigame_Base.MinigameTypes gameType;

    public GameBase(int id, String name, String description, String imageResource, Minigame_Base.MinigameTypes gameType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageResource = imageResource;
        this.gameType = gameType;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Minigame_Base.MinigameTypes getType() {
        return gameType;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getImageResource() {
        return imageResource;
    }
}
