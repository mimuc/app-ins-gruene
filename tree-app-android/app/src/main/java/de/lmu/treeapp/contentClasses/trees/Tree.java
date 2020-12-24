package de.lmu.treeapp.contentClasses.trees;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.contentData.database.entities.app.TreeStateRelations;
import de.lmu.treeapp.contentData.database.entities.content.TreeImage;
import de.lmu.treeapp.contentData.database.entities.content.TreeRelations;
import de.lmu.treeapp.contentData.database.entities.content.Tree_x_Game;

public class Tree {
    public String qrCode; // TODO maybe can be generated, else add to persistent.db

    public List<Integer> leafGamesIds = new ArrayList<>();
    public List<Integer> fruitGamesIds = new ArrayList<>();
    public List<Integer> trunkGamesIds = new ArrayList<>();
    public List<Integer> otherGamesIds = new ArrayList<>();

    public List<TreeImage> treeImages;

    public TreeRelations contentData;
    public TreeStateRelations appData;

    public Tree() {
        super();
    }

    public void initContentData(TreeRelations treeModel) {
        contentData = treeModel;
        treeImages = contentData.images;

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

    public void initAppData(TreeStateRelations treeState) {
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
                return GetGamesProgression(leafGamesIds, appData.treeState.leafGamesCompleted) * 100;
            case fruit:
                return GetGamesProgression(fruitGamesIds, appData.treeState.fruitGamesCompleted) * 100;
            case trunk:
                return GetGamesProgression(trunkGamesIds, appData.treeState.trunkGamesCompleted) * 100;
            case other:
                return GetGamesProgression(otherGamesIds, appData.treeState.otherGamesCompleted) * 100;
            case total:
                float valLeaf = GetGamesProgression(leafGamesIds, appData.treeState.leafGamesCompleted);
                float valFruit = GetGamesProgression(fruitGamesIds, appData.treeState.fruitGamesCompleted);
                float valTrunk = GetGamesProgression(trunkGamesIds, appData.treeState.trunkGamesCompleted);
                float valOther = GetGamesProgression(otherGamesIds, appData.treeState.otherGamesCompleted);
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

    public TreeImage getTreeImage(TreeComponent component) {
        for (TreeImage treeImage : treeImages) {
            if (treeImage.treeComponent == component) {
                return treeImage;
            }
        }
        return null;
    }

    public static TreeComponent toTreeComponent(GameCategories gameCategory) {
        switch (gameCategory) {
            case total:
                return TreeComponent.TREE;
            case leaf:
                return TreeComponent.LEAF;
            case fruit:
                return TreeComponent.FRUIT;
            case trunk:
                return TreeComponent.TRUNK;
            case other:
            case none:
            default:
                return TreeComponent.OTHER;
        }
    }

    public static GameCategories toGameCategory(TreeComponent component) {
        switch (component) {
            case TREE:
                return GameCategories.total;
            case LEAF:
                return GameCategories.leaf;
            case FRUIT:
                return GameCategories.fruit;
            case TRUNK:
                return GameCategories.trunk;
            case OTHER:
            default:
                return GameCategories.other;
        }
    }

    public enum GameCategories {
        leaf, fruit, trunk, other, total, none
    }

}
