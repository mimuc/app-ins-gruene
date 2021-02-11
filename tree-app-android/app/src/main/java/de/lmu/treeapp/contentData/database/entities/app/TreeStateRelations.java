package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.contentClasses.trees.Tree;

/**
 * The volatile database tree model with its related attributes.
 */
public class TreeStateRelations {
    @Embedded
    public TreeState treeState;

    @Relation(parentColumn = "id", entityColumn = "treeId")
    public List<GameStateTakePictureImage> takePictureImages;

    @Relation(parentColumn = "id", entityColumn = "treeId")
    public List<GameStateInputString> treeInputStrings;

    @Relation(parentColumn = "id", entityColumn = "treeId")
    public List<GameStateDescription> treeDescriptions;

    public TreeStateRelations(TreeState treeState) {
        this.treeState = treeState;
        this.takePictureImages = new ArrayList<>();
        this.treeInputStrings = new ArrayList<>();
        this.treeDescriptions = new ArrayList<>();
    }

    public boolean isGameCompleted(Tree.GameCategories category, int gameId) {
        return treeState.isGameCompleted(category, gameId);
    }

    public GameStateTakePictureImage getLatestTakePictureImage(Tree.GameCategories category) {
        GameStateTakePictureImage resImg = null;
        // TODO replace with stream on API 24
        for (GameStateTakePictureImage tpImg : takePictureImages) {
            if (tpImg.gameCategory == category) {
                // Search the most recent image by id
                if (resImg == null || resImg.creationDateTime.before(tpImg.creationDateTime)) {
                    resImg = tpImg;
                }
            }
        }
        return resImg;
    }

    public List<GameStateTakePictureImage> getTakePictureImages(Tree.GameCategories category) {
        List<GameStateTakePictureImage> resImages = new ArrayList<>();
        for (GameStateTakePictureImage tpImg : takePictureImages) {
            if (tpImg.gameCategory == category) {
                resImages.add(tpImg);
            }
        }
        return resImages;
    }
}
