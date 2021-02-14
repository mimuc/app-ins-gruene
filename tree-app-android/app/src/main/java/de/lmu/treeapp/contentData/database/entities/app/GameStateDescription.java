package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Entity;
import androidx.room.Ignore;

import de.lmu.treeapp.contentClasses.trees.Tree;

@Entity
public class GameStateDescription extends AbstractGameState {

    public String description;

    @Ignore
    public GameStateDescription(int treeId, int gameId, Tree.GameCategories gameCategory) {
        this(treeId, gameId, gameCategory, null);
    }

    public GameStateDescription(int treeId, int gameId, Tree.GameCategories gameCategory, String description) {
        super(treeId, gameId, gameCategory);
        this.description = description;
    }
}
