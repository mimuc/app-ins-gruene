package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Relation;

import java.util.List;

/**
 * The persistent database for the CatchFruits Minigame with its related attributes.
 */
public class GameCatchFruitsRelations extends GameRelations {

    @Relation(parentColumn = "id", entityColumn = "gameId")
    public List<GameCatchFruitsItem> items;

    public List<GameCatchFruitsItem> getItems() {
        return items;
    }

}
