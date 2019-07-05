package de.lmu.treeapp.trees;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.Arrays;

import de.lmu.treeapp.minigames.Minigame;

public class Tree {
    // Variables that won't change while playing -> Gotten from CMS (Json/Xml-File)

    // Unique id of the tree
    public String id;
    // Name of the tree e.g. Ahorn
    public String name;
    // The String contained in the qr-code, that leads to this tree
    public String qrId;
    // The profile for this tree
    public TreeProfile profile;
    // Minigames for this tree
    public Minigame[] leafGames;
    public Minigame[] fruitGames;
    public Minigame[] trunkGames;
    public Minigame[] otherGames;

    public enum GameCategories {
        leaf, fruit, trunk, other, total
    }


    // Variables that change while playing

    // Is the tree unlocked yet
    public Boolean unlocked = false;
    // Minigames finished states
    public Boolean[] leafGamesCompleted;
    public Boolean[] fruitGamesCompleted;
    public Boolean[] trunkGamesCompleted;
    public Boolean[] otherGamesCompleted;

    public Tree(String _id, String _name, String _qrId, TreeProfile _profile){
        id = _id;
        name = _name;
        qrId = _qrId;
        profile = _profile;
    }
    public Tree(String _id, String _name, String _qrId, TreeProfile _profile, Minigame[] _leafGames, Minigame[] _fruitGames, Minigame[] _trunkGames, Minigame[] _otherGames){
        id = _id;
        name = _name;
        qrId = _qrId;
        profile = _profile;
        AddGames(_leafGames,_fruitGames,_trunkGames,_otherGames);
    }

    public void AddGames(Minigame[] _leafGames, Minigame[] _fruitGames, Minigame[] _trunkGames, Minigame[] _otherGames){
        if (_leafGames != null && _leafGames.length > 0){
            leafGames = _leafGames;
            leafGamesCompleted = new Boolean[_leafGames.length];
            Arrays.fill(leafGamesCompleted,false);
        }
        if (_fruitGames != null && _fruitGames.length > 0){
            fruitGames = _fruitGames;
            fruitGamesCompleted = new Boolean[_fruitGames.length];
            Arrays.fill(fruitGamesCompleted,false);
        }
        if (_trunkGames != null && _trunkGames.length > 0){
            trunkGames = _trunkGames;
            trunkGamesCompleted = new Boolean[_trunkGames.length];
            Arrays.fill(trunkGamesCompleted,false);
        }
        if (_otherGames != null && _otherGames.length > 0){
            otherGames = _otherGames;
            otherGamesCompleted = new Boolean[_otherGames.length];
            Arrays.fill(otherGamesCompleted,false);
        }
    }

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
