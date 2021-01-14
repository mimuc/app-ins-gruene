package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameOnlyDescriptionRelations;

@Dao
public abstract class GameOnlyDescriptionDao extends GameBaseDao<GameOnlyDescriptionRelations> {

    public GameOnlyDescriptionDao() {
        super(Minigame_Base.MinigameTypes.OnlyDescription);
    }
}

