package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import de.lmu.treeapp.contentClasses.trees.Tree;

@Entity
public class GameStateInputString {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int gameId;
    public int treeId;
    public Tree.GameCategories gameCategory;
    public String inputString;

    public GameStateInputString(int gameId, int treeId, Tree.GameCategories gameCategory, String inputString) {
        this.gameId = gameId;
        this.treeId = treeId;
        this.gameCategory = gameCategory;
        this.inputString = inputString;
    }
}
