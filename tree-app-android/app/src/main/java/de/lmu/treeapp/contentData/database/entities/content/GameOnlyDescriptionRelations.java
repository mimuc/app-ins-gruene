package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Relation;

import java.util.List;

/**
 * The persistent database for the game OnlyDescription with its related attributes.
 */
public class GameOnlyDescriptionRelations extends GameRelations {

    @Relation(parentColumn = "id", entityColumn = "gameId")
    protected List<GameOnlyDescriptionTextItem> textItems;

    @Relation(parentColumn = "id", entityColumn = "gameId")
    protected List<GameOnlyDescriptionImageItem> imageItems;

    public GameOnlyDescriptionRelations(List<GameOnlyDescriptionTextItem> textItems, List<GameOnlyDescriptionImageItem> imageItems) {
        this.textItems = textItems;
        this.imageItems = imageItems;
    }

    public List<GameOnlyDescriptionTextItem> getTextItems() {
        return textItems;
    }

    public List<GameOnlyDescriptionImageItem> getImageItems() {
        return imageItems;
    }

    // Maybe split into cards, which each have its own description
}
