package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"baumoryId"}, unique = true)})
public class GameBaumory {
    @PrimaryKey
    public int id;
    public int baumoryId;
    public String name;
    public String description;
    public String imageResource;
}
