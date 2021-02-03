package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameCatchFruitsRelations;


@Dao
public abstract class GameCatchFruitsDao extends GameBaseDao<GameCatchFruitsRelations> {
    public GameCatchFruitsDao() {
        super(Minigame_Base.MinigameTypes.CatchFruits);
    }
}