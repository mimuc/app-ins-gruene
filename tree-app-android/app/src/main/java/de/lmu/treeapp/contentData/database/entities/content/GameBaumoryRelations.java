package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Relation;

import java.util.List;

/**
 * The persistent database tree model with its related attributes.
 */
public class GameBaumoryRelations extends GameRelations {

    @Relation(parentColumn = "id", entityColumn = "gameId")
    public List<GameBaumoryCard> cards;

    public List<GameBaumoryCard> getCards() {
        return cards;
    }
}
