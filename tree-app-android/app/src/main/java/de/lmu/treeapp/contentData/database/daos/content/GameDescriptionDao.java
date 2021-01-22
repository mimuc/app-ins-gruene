package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Collections;
import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameDescriptionItem;
import de.lmu.treeapp.contentData.database.entities.content.GameDescriptionRelations;

@Dao
public abstract class GameDescriptionDao extends GameBaseDao<GameDescriptionRelations> {
    public GameDescriptionDao() {
        super(Minigame_Base.MinigameTypes.Description);
    }

    @Override
    public List<GameDescriptionRelations> getAll() {
        List<GameDescriptionRelations> gameDragDropRelationsList = super.getAll();
        // get some random strings to put in as "distraction"
        List<GameDescriptionItem> globalItems = mGetRandomOptions();
        for (GameDescriptionRelations gameDragDropRelations : gameDragDropRelationsList) {
            Collections.shuffle(globalItems);
            List<GameDescriptionItem> items = gameDragDropRelations.getItems();
            for (GameDescriptionItem globalItem : globalItems) {
                if (items.size() > 15) {
                    // max 16 items
                    break;
                }
                if (!items.contains(globalItem)) {
                    globalItem.isRight = false;
                    items.add(globalItem);
                }
            }
        }
        return gameDragDropRelationsList;
    }

    @Transaction
    @Query("SELECT * FROM GameDescriptionItem ORDER BY RANDOM() limit 50")
    abstract List<GameDescriptionItem> mGetRandomOptions();
}

