package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Embedded;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentClasses.minigames.IGameBase;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;

public abstract class GameRelations implements IGameBase {
    @Embedded
    public GameBase gameBase;

    @Override
    public int getId() {
        return gameBase.id;
    }

    @Override
    public String getName() {
        return gameBase.name;
    }

    @Override
    @TypeConverters(TypeConversion.class)
    public Minigame_Base.MinigameTypes getType() {
        return gameBase.gameType;
    }

    @Override
    public String getDescription() {
        return gameBase.description;
    }

    @Override
    public String getImageResource() {
        return gameBase.imageResource;
    }
}
