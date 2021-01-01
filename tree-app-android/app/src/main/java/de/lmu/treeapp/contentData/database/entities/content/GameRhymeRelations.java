package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Relation;

import java.util.List;

public class GameRhymeRelations extends GameRelations {
    @Relation(parentColumn = "id", entityColumn = "gameId")
    protected List<GameRhymeItem> items;

    public GameRhymeRelations(List<GameRhymeItem> items) {
        this.items = items;
    }

    public List<GameRhymeItem> getItems() {
        return items;
    }
}
