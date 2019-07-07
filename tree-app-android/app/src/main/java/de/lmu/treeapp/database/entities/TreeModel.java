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

    @Ignore
    public void FirstInit(int _uid, String _name, int _profileId, int[] _leafGamesIds, int[] _fruitGamesIds, int[] _trunkGamesIds, int[] _otherGamesIds){
        this.uid = _uid;
        this.name = _name;
        this.profileId = _profileId;
        this.AddGames(_leafGamesIds,_fruitGamesIds,_trunkGamesIds,_otherGamesIds);
    }

    @Ignore
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

    public enum GameCategories {
        leaf, fruit, trunk, other, total
    }
    @Ignore
    public float GetGameProgressionPercent(GameCategories category){
        switch (category){
            case leaf:
                return GetGamesProgression(leafGamesCompleted) * 100;
            case fruit:
                return GetGamesProgression(fruitGamesCompleted) * 100;
            case trunk:
                return GetGamesProgression(trunkGamesCompleted) * 100;
            case other:
                return GetGamesProgression(otherGamesCompleted) * 100;
            case total:
                float valLeaf = GetGamesProgression(leafGamesCompleted);
                float valFruit = GetGamesProgression(fruitGamesCompleted);
                float valTrunk = GetGamesProgression(trunkGamesCompleted);
                float valOther = GetGamesProgression(otherGamesCompleted);
                float valTotal = (valLeaf + valFruit + valTrunk + valOther)/4;
                return valTotal * 100;
            default:
                return 0;
        }
    }
    @Ignore
    private float GetGamesProgression(Boolean[] gamesCompleted){
        int completed = 0;
        int total = gamesCompleted.length;
        for (int i = 0; i < total; i++){
            if (gamesCompleted[i] == true){
                completed++;
            }
        }
        float value = completed/total;
        return value;
    }


}
