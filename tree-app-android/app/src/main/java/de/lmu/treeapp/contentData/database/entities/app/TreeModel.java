package de.lmu.treeapp.contentData.database.entities.app;

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

    public Boolean isUnlocked;
    public List<Integer> leafGamesCompleted;
    public List<Integer> fruitGamesCompleted;
    public List<Integer> trunkGamesCompleted;
    public List<Integer> otherGamesCompleted;
    public String imageTreeTaken;
    public String imageLeafTaken;
    public String imageFruitTaken;
    public String imageTrunkTaken;

    @Ignore
    public void initDefault(int _uid) {
        this.uid = _uid;
        this.isUnlocked = false;
        this.leafGamesCompleted = new ArrayList<>();
        this.fruitGamesCompleted = new ArrayList<>();
        this.trunkGamesCompleted = new ArrayList<>();
        this.otherGamesCompleted = new ArrayList<>();
        this.imageTreeTaken = "";
        this.imageLeafTaken = "";
        this.imageFruitTaken = "";
        this.imageTrunkTaken = "";
    }

    @Ignore
    public boolean isGameCompleted(Tree.GameCategories category, int gameId) {
        switch (category) {
            case leaf:
                return containsId(leafGamesCompleted, gameId);
            case fruit:
                return containsId(fruitGamesCompleted, gameId);
            case trunk:
                return containsId(trunkGamesCompleted, gameId);
            case other:
                return containsId(otherGamesCompleted, gameId);
            default:
                return false;
        }
    }

    private boolean containsId(List<Integer> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == id) {
                return true;
            }
        }
        return false;
    }
}
