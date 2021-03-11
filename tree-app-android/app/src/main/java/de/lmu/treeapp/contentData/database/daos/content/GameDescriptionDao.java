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


    /*
     * Explaination: in the "GameDescriptionItem" table are two types of gameIds:
     * 1. Actual gameIds e.g. 1001. If set, these items apply ONLY to this specific game
     * 2. null, which applies for all games (global)
     * */

    @Override
    public List<GameDescriptionRelations> getAll() {
        List<GameDescriptionRelations> gameDragDropRelationsList = super.getAll();
        // get some random strings to put in as "distraction"
        List<GameDescriptionItem> globalFalseItems = mGetGlobalFalseOptions(5);
        List<GameDescriptionItem> globalRightOptions = mGetGlobalRightOptions(10);
        for (GameDescriptionRelations gameDragDropRelations : gameDragDropRelationsList) {

            List<GameDescriptionItem> items = gameDragDropRelations.getItems();
            for (GameDescriptionItem rightItem : globalRightOptions) {
                if (items.size() > 15) {
                    // max 16 items
                    break;
                }
                /*
                 * Make sure we don't get the same value twice.
                 * Use name for comparison as id varies dependent on game.
                 */
                boolean existsAlready = false;
                for (GameDescriptionItem exists : items) {
                    if (exists.content.equals(rightItem.content)) {
                        existsAlready = true;
                        break;
                    }
                }
                if (!existsAlready) {
                    items.add(rightItem);
                }
            }

            for (GameDescriptionItem falseItem : globalFalseItems) {
                if (items.size() > 15) {
                    // max 16 items
                    break;
                }
                /*
                 * Make sure we don't get the same value twice.
                 * Use name for comparison as id varies dependent on game.
                 */
                boolean existsAlready = false;
                for (GameDescriptionItem exists : items) {
                    if (exists.content.equals(falseItem.content)) {
                        existsAlready = true;
                        break;
                    }
                }
                if (!existsAlready) {
                    items.add(falseItem);
                }
            }
            Collections.shuffle(items);

        }
        return gameDragDropRelationsList;
    }

    @Transaction
    @Query("SELECT * FROM GameDescriptionItem WHERE gameId IS NULL and isRight = 0 ORDER BY RANDOM() LIMIT :limit")
    abstract List<GameDescriptionItem> mGetGlobalFalseOptions(int limit);

    @Transaction
    @Query("SELECT * FROM GameDescriptionItem WHERE gameId IS NULL and isRight = 1 ORDER BY RANDOM() LIMIT :limit")
    abstract List<GameDescriptionItem> mGetGlobalRightOptions(int limit);
}
