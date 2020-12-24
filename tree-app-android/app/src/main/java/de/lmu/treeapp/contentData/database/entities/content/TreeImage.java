package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentClasses.trees.TreeComponent;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;

@Entity
public class TreeImage {
    @PrimaryKey
    public int id;
    public int treeId;
    public String imageResource;
    @TypeConverters(TypeConversion.class)
    public TreeComponent treeComponent;
}
