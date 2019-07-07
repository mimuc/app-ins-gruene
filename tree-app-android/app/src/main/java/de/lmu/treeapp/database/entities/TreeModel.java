package de.lmu.treeapp.database.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity
public class TreeModel {
    @PrimaryKey
    public int uid;

    public String name;
    public int profileId;

    public int[] leafGamesIds;
    public int[] fruitGamesIds;
    public int[] trunkGamesIds;
    public int[] otherGamesIds;

    public Boolean unlocked = false;
    public Boolean[] leafGamesCompleted;
    public Boolean[] fruitGamesCompleted;
    public Boolean[] trunkGamesCompleted;
    public Boolean[] otherGamesCompleted;

    public void FirstInit(int _uid, String _name, int _profileId, int[] _leafGamesIds, int[] _fruitGamesIds, int[] _trunkGamesIds, int[] _otherGamesIds){
        this.uid = _uid;
        this.name = _name;
        this.profileId = _profileId;
        this.AddGames(_leafGamesIds,_fruitGamesIds,_trunkGamesIds,_otherGamesIds);
    }

    public enum GameCategories {
        leaf, fruit, trunk, other, total
    }

    private void AddGames(int[] _leafGames, int[] _fruitGames, int[] _trunkGames, int[] _otherGames){
        if (_leafGames != null && _leafGames.length > 0){
            leafGamesIds = _leafGames;
            leafGamesCompleted = new Boolean[_leafGames.length];
            Arrays.fill(leafGamesCompleted,false);
        }
        if (_fruitGames != null && _fruitGames.length > 0){
            fruitGamesIds = _fruitGames;
            fruitGamesCompleted = new Boolean[_fruitGames.length];
            Arrays.fill(fruitGamesCompleted,false);
        }
        if (_trunkGames != null && _trunkGames.length > 0){
            trunkGamesIds = _trunkGames;
            trunkGamesCompleted = new Boolean[_trunkGames.length];
            Arrays.fill(trunkGamesCompleted,false);
        }
        if (_otherGames != null && _otherGames.length > 0){
            otherGamesIds = _otherGames;
            otherGamesCompleted = new Boolean[_otherGames.length];
            Arrays.fill(otherGamesCompleted,false);
        }
    }


}
