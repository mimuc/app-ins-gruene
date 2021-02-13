package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import de.lmu.treeapp.contentClasses.trees.Tree;

@Entity
public class GameStateDescription {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int gameId;
    public int treeId;
    public Tree.GameCategories gameCategory;
    public String description;

    public GameStateDescription(int gameId, int treeId, Tree.GameCategories gameCategory, String description) {
        this.gameId = gameId;
        this.treeId = treeId;
        this.gameCategory = gameCategory;
        this.description = description;
    }
}
