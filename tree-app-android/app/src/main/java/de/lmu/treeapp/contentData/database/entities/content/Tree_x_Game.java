package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.TypeConversion;

@Entity
public class Tree_x_Game {
    @PrimaryKey
    public int id;
    public int treeId;
    public int gameId;
    @TypeConverters(TypeConversion.class)
    public Tree.GameCategories gameCategory;
}
