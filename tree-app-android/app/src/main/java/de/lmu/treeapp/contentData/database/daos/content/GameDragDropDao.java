package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropItem;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropRelations;

@Dao
public abstract class GameDragDropDao extends GameBaseDao<GameDragDropRelations> {

    public GameDragDropDao() {
        super(Minigame_Base.MinigameTypes.DragDrop);
    }

    /**
     * @return All DragDrop games, but include global drop items, used for every game.
     */
    @Override
    public List<GameDragDropRelations> getAll() {
        List<GameDragDropItem> globalItems = mGetGlobalItems();
        List<GameDragDropRelations> gameDragDropRelationsList = super.getAll();
        for (GameDragDropRelations gameDragDropRelations : gameDragDropRelationsList) {
            List<GameDragDropItem> items = gameDragDropRelations.getItems();
            items.addAll(globalItems);
        }
        return gameDragDropRelationsList;
    }

    /**
     * @return DragDropItem which can be used for every tree.
     */
    @Transaction
    @Query("SELECT * FROM GameDragDropItem WHERE gameId IS NULL")
    abstract List<GameDragDropItem> mGetGlobalItems();
}

