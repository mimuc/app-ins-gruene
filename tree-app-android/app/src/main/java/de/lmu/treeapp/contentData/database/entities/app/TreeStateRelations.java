package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Embedded;

import de.lmu.treeapp.contentClasses.trees.Tree;

/**
 * The volatile database tree model with its related attributes.
 */
public class TreeStateRelations {
    @Embedded
    public TreeState treeState;

    public TreeStateRelations(TreeState treeState) {
        this.treeState = treeState;
    }

    public boolean isGameCompleted(Tree.GameCategories category, int gameId) {
        return treeState.isGameCompleted(category, gameId);
    }
}
