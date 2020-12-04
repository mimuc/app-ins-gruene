package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TreeModel {
    @PrimaryKey
    public int id;
    public String name;
}
