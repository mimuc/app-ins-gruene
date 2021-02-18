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
     * Explaination: in the "GameDescriptionItem" table are three types of gameIds:
     * 1. Actual gameIds e.g. 1001. If set, these items apply ONLY to this specific game
     * 2. "0", which are always false "distraction" values
     * 3. "1", which are always correct (majority as not to discourage kids)
     * */

    @Override
    public List<GameDescriptionRelations> getAll() {
        List<GameDescriptionRelations> gameDragDropRelationsList = super.getAll();
        // get some random strings to put in as "distraction"
        List<GameDescriptionItem> falseItems = mGetFalseOptions();
        List<GameDescriptionItem> moreRightOptions = mGetRightOptions();
        for (GameDescriptionRelations gameDragDropRelations : gameDragDropRelationsList) {

            List<GameDescriptionItem> items = gameDragDropRelations.getItems();
            for (GameDescriptionItem rightItem : moreRightOptions) {
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
                    rightItem.isRight = true;
                    items.add(rightItem);
                }
            }

            for (GameDescriptionItem falseItem : falseItems) {
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
                    falseItem.isRight = false;
                    items.add(falseItem);
                }
            }
            Collections.shuffle(items);

        }
        return gameDragDropRelationsList;
    }

    @Transaction
    @Query("SELECT * FROM GameDescriptionItem WHERE gameId = 0 ORDER BY RANDOM() LIMIT 5")
    abstract List<GameDescriptionItem> mGetFalseOptions();

    @Transaction
    @Query("SELECT * FROM GameDescriptionItem WHERE gameId = 1 ORDER BY RANDOM() LIMIT 10")
    abstract List<GameDescriptionItem> mGetRightOptions();
}

