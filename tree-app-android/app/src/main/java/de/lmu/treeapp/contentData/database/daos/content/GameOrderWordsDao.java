package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameOrderWordsRelations;

@Dao
public abstract class GameOrderWordsDao extends GameBaseDao<GameOrderWordsRelations> {

    public GameOrderWordsDao() {
        super(Minigame_Base.MinigameTypes.OrderWords);
    }

}