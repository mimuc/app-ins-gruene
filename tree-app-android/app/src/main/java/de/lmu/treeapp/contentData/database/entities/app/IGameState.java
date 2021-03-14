package de.lmu.treeapp.contentData.database.entities.app;

import de.lmu.treeapp.contentClasses.trees.Tree;

public interface IGameState {
    int getId();

    void setId(int id);

    int getTreeId();

    void setTreeId(int treeId);

    int getGameId();

    void setGameId(int gameId);

    Tree.GameCategories getCategory();

    void setCategory(Tree.GameCategories category);
}
