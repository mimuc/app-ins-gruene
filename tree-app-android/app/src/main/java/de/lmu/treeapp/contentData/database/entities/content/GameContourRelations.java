package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Relation;

import java.util.List;

public class GameContourRelations extends GameRelations {

    @Relation(parentColumn = "id", entityColumn = "gameId")
    public List<GameContourCheckpoint> checkpoints;

    public List<GameContourCheckpoint> getCheckpoints() {
        return checkpoints;
    }
}
