package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GamePuzzleRelations;

@Dao
public abstract class GamePuzzleDao extends GameBaseDao<GamePuzzleRelations> {

    public GamePuzzleDao() {
        super(Minigame_Base.MinigameTypes.Puzzle);
    }


}

