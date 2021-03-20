package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import de.lmu.treeapp.contentData.database.typeconversion.DateConverter;

@Entity
public class GameStateTakePictureImage {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int stateId;
    public String imagePath;
    @TypeConverters(DateConverter.class)
    public Date creationDateTime;
    public String specialGameName;

    public GameStateTakePictureImage(int stateId, String imagePath, Date creationDateTime, String specialGameName) {
        this.stateId = stateId;
        this.imagePath = imagePath;
        this.creationDateTime = creationDateTime;
        this.specialGameName = specialGameName;
    }
}