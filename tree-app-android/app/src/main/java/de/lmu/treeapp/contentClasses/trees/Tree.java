package de.lmu.treeapp.contentClasses.trees;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.contentData.database.entities.app.TreeState;
import de.lmu.treeapp.contentData.database.entities.content.TreeImage;
import de.lmu.treeapp.contentData.database.entities.content.TreeRelations;
import de.lmu.treeapp.contentData.database.entities.content.Tree_x_Game;

public class Tree {
    public String qrCode; // TODO maybe can be generated, else add to persistent.db

    public List<Integer> leafGamesIds = new ArrayList<>();
    public List<Integer> fruitGamesIds = new ArrayList<>();
    public List<Integer> trunkGamesIds = new ArrayList<>();
    public List<Integer> otherGamesIds = new ArrayList<>();

    // TODO replace with getter methods which return images directly from contentData as type "TreeImage".
    public String imageTree = "";
    public String imageLeaf = "";
    public String imageFruit = "";
    public String imageTrunk = "";
    public String imageOther = "";

    public TreeRelations contentData;
    public TreeState appData;

    public Tree() {
        super();
    }

    public void initContentData(TreeRelations treeModel) {
        contentData = treeModel;

        for (TreeImage image : contentData.images) {
            switch (image.treeComponent) {
                case TREE:
                    imageTree = image.imageResource;
                    break;
                case LEAF:
                    imageLeaf = image.imageResource;
                    break;
                case FRUIT:
                    imageFruit = image.imageResource;
                    break;
                case TRUNK:
                    imageTrunk = image.imageResource;
                    break;
                case OTHER:
                    imageOther = image.imageResource;
                    break;
            }
        }

        for (Tree_x_Game game : contentData.tree_x_games) {
            switch (game.gameCategory) {
                case leaf:
                    leafGamesIds.add(game.gameId);
                    break;
                case fruit:
                    fruitGamesIds.add(game.gameId);
                    break;
                case trunk:
                    trunkGamesIds.add(game.gameId);
                    break;
                case other:
                    otherGamesIds.add(game.gameId);
                    break;
                case total:
                case none:
                    break;
            }
        }
    }

    public void initAppData(TreeState treeState) {
        appData = treeState;
    }

    public int getId() {
        return this.contentData.treeModel.id;
    }

    public String getName() {
        return this.contentData.treeModel.name;
    }

    public List<Integer> GetGameIds(GameCategories category) {
        switch (category) {
            case leaf:
                return leafGamesIds;
            case fruit:
                return fruitGamesIds;
            case trunk:
                return trunkGamesIds;
            case other:
                return otherGamesIds;
            default:
                return null;
        }
    }

    public float GetGameProgressionPercent(GameCategories category) {

        switch (category) {
            case leaf:
                return GetGamesProgression(leafGamesIds, appData.leafGamesCompleted) * 100;
            case fruit:
                return GetGamesProgression(fruitGamesIds, appData.fruitGamesCompleted) * 100;
            case trunk:
                return GetGamesProgression(trunkGamesIds, appData.trunkGamesCompleted) * 100;
            case other:
                return GetGamesProgression(otherGamesIds, appData.otherGamesCompleted) * 100;
            case total:
                float valLeaf = GetGamesProgression(leafGamesIds, appData.leafGamesCompleted);
                float valFruit = GetGamesProgression(fruitGamesIds, appData.fruitGamesCompleted);
                float valTrunk = GetGamesProgression(trunkGamesIds, appData.trunkGamesCompleted);
                float valOther = GetGamesProgression(otherGamesIds, appData.otherGamesCompleted);
                float valTotal = (valLeaf + valFruit + valTrunk + valOther) / 4;
                return valTotal * 100;
            default:
                return 0;
        }
    }

    private float GetGamesProgression(List<Integer> gamesTotal, List<Integer> gamesCompleted) {
        if (gamesTotal.isEmpty()) {
            return 1;
        }
        int completed = 0;
        for (int i = 0; i < gamesCompleted.size(); i++) {
            for (int j = 0; j < gamesTotal.size(); j++) {
                if (gamesTotal.get(j).intValue() == gamesCompleted.get(i).intValue()) {
                    completed++;
                    break;
                }
            }
        }
        return (float) completed / (float) gamesTotal.size();
    }

    public enum GameCategories {
        leaf, fruit, trunk, other, total, none
    }

}
