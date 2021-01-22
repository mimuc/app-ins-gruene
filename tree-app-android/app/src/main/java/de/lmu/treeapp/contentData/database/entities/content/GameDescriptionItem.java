package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class GameDescriptionItem {
    @PrimaryKey
    public int id;
    public int gameId;
    public String content;
    @Ignore
    public boolean isRight = true;
}

