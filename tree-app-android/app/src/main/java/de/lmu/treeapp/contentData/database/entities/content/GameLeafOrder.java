package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GameLeafOrder {

    @PrimaryKey
    public int id;

    public int gameId;

    public Boolean isAlternate;
}
