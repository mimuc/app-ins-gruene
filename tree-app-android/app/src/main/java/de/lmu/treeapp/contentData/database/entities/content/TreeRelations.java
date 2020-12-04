package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/**
 * The persistent database tree model with its related attributes.
 */
public class TreeRelations {
    @Embedded
    public TreeModel treeModel;

    @Relation(parentColumn = "id", entityColumn = "treeId")
    public List<TreeImage> images;

    @Relation(parentColumn = "id", entityColumn = "treeId")
    public List<Tree_x_Game> tree_x_games;
}
