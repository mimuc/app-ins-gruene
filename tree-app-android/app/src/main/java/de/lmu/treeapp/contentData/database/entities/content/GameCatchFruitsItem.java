package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentClasses.trees.TreeComponent;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;

@Entity
public class GameCatchFruitsItem {
    @PrimaryKey
    public Integer id;
    public String content;
    @TypeConverters({TypeConversion.class})
    public TreeComponent treeComponent;
    public Integer treeId;
    public Integer gameId;
}
