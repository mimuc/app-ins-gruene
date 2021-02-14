package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GameContourCheckpoint {
    @PrimaryKey
    public int id;
    public Integer gameId;
    public float xRel;
    public float yRel;
    public Integer position;
    public Boolean isValid;
}
