package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;

@Entity
public class TreeProfileCard {
    @PrimaryKey
    public int id;
    public int treeId;
    public String name;
    public String imageResource;
    public String content;
    @TypeConverters({TypeConversion.class})
    public Tree.GameCategories unlockedBy;
    public String picture;
}
