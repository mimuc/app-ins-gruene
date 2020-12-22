package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameBaumoryRelations;

@Dao
public abstract class GameBaumoryDao extends GameBaseDao<GameBaumoryRelations> {

    public GameBaumoryDao() {
        super(Minigame_Base.MinigameTypes.Baumory);
    }
}

