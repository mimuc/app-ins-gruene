package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Entity;
import androidx.room.Ignore;

import de.lmu.treeapp.contentClasses.trees.Tree;


@Entity
public class GameStateScore extends AbstractGameState {

    public Integer highscore;

    @Ignore
    public GameStateScore(int treeId, int gameId, Tree.GameCategories gameCategory) {
        this(treeId, gameId, gameCategory, 0);
    }

    public GameStateScore(int treeId, int gameId, Tree.GameCategories gameCategory, int highscore) {
        super(treeId, gameId, gameCategory);
        this.highscore = highscore;
    }

}