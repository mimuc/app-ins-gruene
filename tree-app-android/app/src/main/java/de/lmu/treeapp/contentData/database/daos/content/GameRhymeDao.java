package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameRhymeRelations;

@Dao
public abstract class GameRhymeDao extends GameBaseDao<GameRhymeRelations> {

    public GameRhymeDao() {
        super(Minigame_Base.MinigameTypes.Rhyme);
    }

}