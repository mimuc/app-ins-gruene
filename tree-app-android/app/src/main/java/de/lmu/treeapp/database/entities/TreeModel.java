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
        setGameIds(GameCategories.leaf, _leafGames);
        setGameIds(GameCategories.fruit, _fruitGames);
        setGameIds(GameCategories.trunk, _trunkGames);
        setGameIds(GameCategories.other, _otherGames);
    }

    public void setGameIds(GameCategories category, int[] ids){
        if (ids != null && ids.length > 0){
            switch (category){
                case leaf:
                    leafGamesIds = ids;
                    leafGamesCompleted = new Boolean[ids.length];
                    Arrays.fill(leafGamesCompleted, false);
                    break;
                case fruit:
                    fruitGamesIds = ids;
                    fruitGamesCompleted = new Boolean[ids.length];
                    Arrays.fill(fruitGamesCompleted,false);
                    break;
                case trunk:
                    trunkGamesIds = ids;
                    trunkGamesCompleted = new Boolean[ids.length];
                    Arrays.fill(trunkGamesCompleted,false);
                    break;
                case other:
                    otherGamesIds = ids;
                    otherGamesCompleted = new Boolean[ids.length];
                    Arrays.fill(otherGamesCompleted,false);
                    break;
                default:
                    break;
            }
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
