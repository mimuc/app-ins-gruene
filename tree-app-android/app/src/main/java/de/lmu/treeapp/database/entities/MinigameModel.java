package de.lmu.treeapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class MinigameModel {
    @PrimaryKey
    public int uid;
    public String name;
    public String description;

}
