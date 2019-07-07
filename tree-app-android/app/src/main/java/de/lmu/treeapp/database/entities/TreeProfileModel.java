package de.lmu.treeapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TreeProfileModel {
    @PrimaryKey
    public int uid;

    public void FirstInit(int _uid){
        this.uid = _uid;
    }
}
