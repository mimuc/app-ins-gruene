package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GameBaumoryCard {
    @PrimaryKey
    public int id;
    public int gameId;
    public int match;
    public String imageResource;
    public int difficulty;
}
