package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GameChooseAnswer {
    @PrimaryKey
    public int id;
    public int treeId;
    public String name;
    public String description;
    public String gameType;
}
