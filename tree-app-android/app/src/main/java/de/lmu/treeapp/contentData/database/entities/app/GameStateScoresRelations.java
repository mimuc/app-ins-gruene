package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Embedded;
import androidx.room.Relation;

/**
 * The volatile database game state score with its related attributes.
 */
public class GameStateScoresRelations {
    @Embedded
    public int tree_x_gameId;
    int highscore;

    @Relation(parentColumn = "id", entityColumn = "tree_x_game")
    public GameStateScore gameStateScore;

    public GameStateScoresRelations(GameStateScore treeState) {
        this.tree_x_gameId = treeState.tree_x_gameId;
        this.highscore = treeState.highscore;
    }

    public int getHighscore() {
        return this.highscore;
    }

}
