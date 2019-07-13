package de.lmu.treeapp.contentClasses.trees;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.TreeModel;

public class Tree {
    public int uid;

    public String name;
    public int profileId;
    public String qrCode;

    public List<Integer> leafGamesIds;
    public List<Integer> fruitGamesIds;
    public List<Integer> trunkGamesIds;
    public List<Integer> otherGamesIds;

    public TreeModel changeable;



    public enum GameCategories {
        leaf, fruit, trunk, other, total
    }


    public void InitFromCMS(int _uid, String _name, int _profileId, List<Integer>  _leafGamesIds, List<Integer>  _fruitGamesIds, List<Integer>  _trunkGamesIds, List<Integer> _otherGamesIds){
        this.uid = _uid;
        this.name = _name;
        this.profileId = _profileId;
        this.AddGames(_leafGamesIds,_fruitGamesIds,_trunkGamesIds,_otherGamesIds);
    }

    public void InitFromDB(TreeModel treeModel){
        changeable = treeModel;
    }

    private void AddGames(List<Integer> _leafGames, List<Integer>  _fruitGames, List<Integer>  _trunkGames, List<Integer>  _otherGames){
        setGameIds(GameCategories.leaf, _leafGames);
        setGameIds(GameCategories.fruit, _fruitGames);
        setGameIds(GameCategories.trunk, _trunkGames);
        setGameIds(GameCategories.other, _otherGames);
    }

    public void setGameIds(GameCategories category, List<Integer> ids){
        if (ids != null && ids.size() > 0){
            switch (category){
                case leaf:
                    leafGamesIds = ids;
                    break;
                case fruit:
                    fruitGamesIds = ids;
                    break;
                case trunk:
                    trunkGamesIds = ids;
                    break;
                case other:
                    otherGamesIds = ids;
                    break;
                default:
                    break;
            }
        }
    }

    public float GetGameProgressionPercent(GameCategories category){
        switch (category){
            case leaf:
                return GetGamesProgression(leafGamesIds, changeable.leafGamesCompleted) * 100;
            case fruit:
                return GetGamesProgression(fruitGamesIds, changeable.fruitGamesCompleted) * 100;
            case trunk:
                return GetGamesProgression(trunkGamesIds, changeable.trunkGamesCompleted) * 100;
            case other:
                return GetGamesProgression(otherGamesIds, changeable.otherGamesCompleted) * 100;
            case total:
                float valLeaf = GetGamesProgression(leafGamesIds, changeable.leafGamesCompleted);
                float valFruit = GetGamesProgression(fruitGamesIds, changeable.fruitGamesCompleted);
                float valTrunk = GetGamesProgression(trunkGamesIds, changeable.trunkGamesCompleted);
                float valOther = GetGamesProgression(otherGamesIds, changeable.otherGamesCompleted);
                float valTotal = (valLeaf + valFruit + valTrunk + valOther)/4;
                return valTotal * 100;
            default:
                return 0;
        }
    }

    private float GetGamesProgression(List<Integer> gamesTotal, List<Integer> gamesCompleted){
        int completed = 0;
        for (int i = 0; i < gamesCompleted.size(); i++){
            for (int j = 0; j < gamesTotal.size(); j++){
                if (gamesTotal.get(j) == gamesCompleted.get(i)){
                    completed++;
                    break;
                }
            }
        }
        float value = completed/gamesTotal.size();
        return value;
    }


}