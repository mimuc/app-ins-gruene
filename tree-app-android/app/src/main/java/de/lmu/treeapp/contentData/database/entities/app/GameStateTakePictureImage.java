package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.typeconversion.DateConverter;

@Entity
public class GameStateTakePictureImage {
    @PrimaryKey(autoGenerate = true)
    public int id;
    // public int txgId; // Tree_x_Game Id
    public int gameId;
    public int treeId;
    public Tree.GameCategories gameCategory;
    public String imagePath;
    @TypeConverters(DateConverter.class)
    public Date creationDateTime;

    public GameStateTakePictureImage(int gameId, int treeId, Tree.GameCategories gameCategory, String imagePath, Date creationDateTime) {
        this.gameId = gameId;
        this.treeId = treeId;
        this.gameCategory = gameCategory;
        this.imagePath = imagePath;
        this.creationDateTime = creationDateTime;
    }
}