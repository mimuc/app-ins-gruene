package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class UserProfileState implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String age;
    public Integer location;
    public Integer tree;
    public Integer leaf;
    public Integer season;
    public Integer avatar;
}
