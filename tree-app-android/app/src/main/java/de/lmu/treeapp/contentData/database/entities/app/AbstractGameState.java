package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;

public abstract class AbstractGameState {

    // Use id as primary key, as you may want to save multiple states for the same game.
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int treeId;

    public int gameId;

    @TypeConverters(TypeConversion.class)
    public Tree.GameCategories gameCategory;

    public AbstractGameState(int treeId, int gameId, Tree.GameCategories gameCategory) {
        this(0, treeId, gameId, gameCategory);
    }

    public AbstractGameState(int id, int treeId, int gameId, Tree.GameCategories gameCategory) {
        this.id = id;
        this.gameId = gameId;
        this.treeId = treeId;
        this.gameCategory = gameCategory;
    }
}