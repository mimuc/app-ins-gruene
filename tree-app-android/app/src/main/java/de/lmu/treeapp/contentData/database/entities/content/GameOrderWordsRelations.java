package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Relation;

import java.util.List;

public class GameOrderWordsRelations extends GameRelations {
    @Relation(parentColumn = "id", entityColumn = "gameId")
    protected List<GameOrderWordsItem> items;

    public GameOrderWordsRelations(List<GameOrderWordsItem> items) {
        this.items = items;
    }

    public List<GameOrderWordsItem> getItems() {
        return items;
    }
}
