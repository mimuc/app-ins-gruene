package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PlayerModel {
    @PrimaryKey
    public int uid = 0;
    public String name = "Forscher";
}
