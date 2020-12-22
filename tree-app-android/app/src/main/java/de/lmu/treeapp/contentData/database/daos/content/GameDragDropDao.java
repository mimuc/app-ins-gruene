package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.content.GameDragDropItem;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropRelations;

@Dao
public abstract class GameDragDropDao {

    @Transaction
    @Query("SELECT * FROM GameBase WHERE gameType = 'DragDrop'")
    abstract List<GameDragDropRelations> mGetAll();

    /**
     * @return All DragDrop games, but include global drop items, used for every game.
     */
    public List<GameDragDropRelations> getAll() {
        List<GameDragDropItem> globalItems = mGetGlobalItems();
        List<GameDragDropRelations> gameDragDropRelationsList = mGetAll();
        for (GameDragDropRelations gameDragDropRelations: gameDragDropRelationsList) {
            List<GameDragDropItem> items = gameDragDropRelations.getItems();
            items.addAll(globalItems);
        }
        return gameDragDropRelationsList;
    }

    @Transaction
    @Query("SELECT * FROM GameBase WHERE gameType = 'DragDrop' AND id=:id LIMIT 1")
    abstract GameDragDropRelations getById(int id);

    /**
     * @return DragDropItem which can be used for every tree.
     */
    @Transaction
    @Query("SELECT * FROM GameDragDropItem WHERE gameId IS NULL")
    abstract List<GameDragDropItem> mGetGlobalItems();
}

