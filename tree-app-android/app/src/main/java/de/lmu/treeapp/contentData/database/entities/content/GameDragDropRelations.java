package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Relation;

import java.util.List;

/**
 * The persistent database for the game DragDrop with its related attributes.
 */
public class GameDragDropRelations extends GameRelations {

    @Relation(parentColumn = "id", entityColumn = "gameId")
    protected List<GameDragDropItem> items;

    @Relation(parentColumn = "id", entityColumn = "gameId")
    protected List<GameDragDropZone> zones;

    public GameDragDropRelations(List<GameDragDropItem> items, List<GameDragDropZone> zones) {
        this.items = items;
        this.zones = zones;
    }

    public List<GameDragDropItem> getItems() {
        return items;
    }

    public List<GameDragDropZone> getZones() {
        return zones;
    }
}
