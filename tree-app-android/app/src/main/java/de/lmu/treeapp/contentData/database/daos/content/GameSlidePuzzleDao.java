package de.lmu.treeapp.contentData.database.daos.content;
import androidx.room.Dao;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameSlidePuzzleRelations;

@Dao
public abstract class GameSlidePuzzleDao extends GameBaseDao<GameSlidePuzzleRelations> {

    public GameSlidePuzzleDao() {
        super(Minigame_Base.MinigameTypes.SlidePuzzle);
    }


}
