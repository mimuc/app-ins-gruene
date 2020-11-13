package de.lmu.treeapp.contentData.database.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.contentClasses.trees.Tree;


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
    public void InitDefault(int _uid) {
        this.uid = _uid;
        this.unlocked = false;
        this.leafGamesCompleted = new ArrayList<>();
        this.fruitGamesCompleted = new ArrayList<>();
        this.trunkGamesCompleted = new ArrayList<>();
        this.otherGamesCompleted = new ArrayList<>();
    }

    @Ignore
    public boolean IsGameCompleted(Tree.GameCategories category, int gameId) {
        switch (category) {
            case leaf:
                return ContainsId(leafGamesCompleted, gameId);
            case fruit:
                return ContainsId(fruitGamesCompleted, gameId);
            case trunk:
                return ContainsId(trunkGamesCompleted, gameId);
            case other:
                return ContainsId(otherGamesCompleted, gameId);
            default:
                return false;
        }
    }

    private boolean ContainsId(List<Integer> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == id) {
                return true;
            }
        }
        return false;
    }
}
