package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Entity;
import androidx.room.Ignore;

import de.lmu.treeapp.contentClasses.trees.Tree;

@Entity
public class GameStateInputString extends AbstractGameState {

    public String inputString;

    @Ignore
    public GameStateInputString(int treeId, int gameId, Tree.GameCategories gameCategory) {
        this(treeId, gameId, gameCategory, null);
    }

    public GameStateInputString(int treeId, int gameId, Tree.GameCategories gameCategory, String inputString) {
        super(treeId, gameId, gameCategory);
        this.inputString = inputString;
    }
}
