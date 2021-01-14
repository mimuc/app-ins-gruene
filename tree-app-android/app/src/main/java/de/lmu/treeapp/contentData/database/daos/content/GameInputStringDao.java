package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameInputStringRelations;

@Dao
public abstract class GameInputStringDao extends GameBaseDao<GameInputStringRelations> {
    public GameInputStringDao() {
        super(Minigame_Base.MinigameTypes.InputString);
    }

}
