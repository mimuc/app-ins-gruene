package de.lmu.treeapp.contentData.database.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;


@Entity
public class TreeModel {
    @PrimaryKey
    public int uid;

    public Boolean unlocked;
    public List<Integer> leafGamesCompleted;
    public List<Integer> fruitGamesCompleted;
    public List<Integer> trunkGamesCompleted;
    public List<Integer> otherGamesCompleted;

    @Ignore
    public void InitDefault(int _uid){
        this.uid = _uid;
        this.unlocked = false;
        this.leafGamesCompleted = new ArrayList<>();
        this.fruitGamesCompleted = new ArrayList<>();
        this.trunkGamesCompleted = new ArrayList<>();
        this.otherGamesCompleted = new ArrayList<>();
    }



}
