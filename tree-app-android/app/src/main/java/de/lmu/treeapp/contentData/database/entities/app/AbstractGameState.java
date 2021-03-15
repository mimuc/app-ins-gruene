package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;

public abstract class AbstractGameState implements IGameState {

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

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getGameId() {
        return gameId;
    }

    @Override
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public int getTreeId() {
        return treeId;
    }

    @Override
    public void setTreeId(int treeId) {
        this.treeId = treeId;
    }

    @Override
    public Tree.GameCategories getCategory() {
        return gameCategory;
    }

    @Override
    public void setCategory(Tree.GameCategories gameCategory) {
        this.gameCategory = gameCategory;
    }

}