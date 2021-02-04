package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class GameStateScore {
    @PrimaryKey
    public Integer tree_x_gameId;
    public Integer highscore;

    public GameStateScore(int tree_x_gameId, int highscore) {
        this.tree_x_gameId = tree_x_gameId;
        this.highscore = highscore;
    }

}