package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Relation;

import java.util.List;



public class GameDescriptionRelations extends GameRelations {

    @Relation(parentColumn = "id", entityColumn = "gameId")
    public List<GameDescriptionItem> items;

    public List<GameDescriptionItem> getItems() {
        return items;
    }

}


