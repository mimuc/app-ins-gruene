package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameContourCheckpoint;
import de.lmu.treeapp.contentData.database.entities.content.GameContourRelations;

@Dao
public abstract class GameContourDao extends GameBaseDao<GameContourRelations> {

    public GameContourDao() {
        super(Minigame_Base.MinigameTypes.Contour);
    }

    /**
     * @return All Contour games, but include global checkpoint items, used for every game.
     */
    @Override
    public List<GameContourRelations> getAll() {
        List<GameContourCheckpoint> globalItems = mGetGlobalCheckpoints();
        List<GameContourRelations> gameDragDropRelationsList = super.getAll();
        for (GameContourRelations gameDragDropRelations : gameDragDropRelationsList) {
            List<GameContourCheckpoint> checkpoints = gameDragDropRelations.getCheckpoints();
            checkpoints.addAll(globalItems);
        }
        return gameDragDropRelationsList;
    }

    /**
     * @return GameContourCheckpoint which can be used for every tree.
     */
    @Transaction
    @Query("SELECT * FROM GameContourCheckpoint WHERE gameId IS NULL")
    abstract List<GameContourCheckpoint> mGetGlobalCheckpoints();
}

