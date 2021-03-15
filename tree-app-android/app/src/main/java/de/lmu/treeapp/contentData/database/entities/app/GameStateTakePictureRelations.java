package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.contentClasses.trees.Tree;

public class GameStateTakePictureRelations implements IGameState {

    @Embedded
    public GameStateTakePicture gameState;

    @Relation(parentColumn = "id", entityColumn = "stateId")
    public List<GameStateTakePictureImage> images;

    public GameStateTakePictureRelations(GameStateTakePicture gameState) {
        this.gameState = gameState;
        this.images = new ArrayList<>();
    }

    @Ignore
    public Integer getSelectedImageId() {
        return gameState.selectedImageId;
    }

    @Ignore
    public GameStateTakePictureImage getSelectedImage() {
        if (gameState.selectedImageId == null) return null;
        for (GameStateTakePictureImage image : images) {
            if (image.id == gameState.selectedImageId) {
                return image;
            }
        }
        return null;
    }

    @Override
    public int getId() {
        return gameState.getId();
    }

    @Override
    public void setId(int id) {
        gameState.setId(id);
    }

    @Override
    public int getTreeId() {
        return gameState.getTreeId();
    }

    @Override
    public void setTreeId(int treeId) {
        gameState.setTreeId(treeId);
    }

    @Override
    public int getGameId() {
        return gameState.getGameId();
    }

    @Override
    public void setGameId(int gameId) {
        gameState.setGameId(gameId);
    }

    @Override
    public Tree.GameCategories getCategory() {
        return gameState.getCategory();
    }

    @Override
    public void setCategory(Tree.GameCategories category) {
        gameState.setCategory(category);
    }
}
