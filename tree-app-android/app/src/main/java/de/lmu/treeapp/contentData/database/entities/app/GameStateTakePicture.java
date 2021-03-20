package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Entity;
import androidx.room.Ignore;

import de.lmu.treeapp.contentClasses.trees.Tree;

@Entity
public class GameStateTakePicture extends AbstractGameState {

    public Integer selectedImageId;

    /**
     * Constructor needed for static construction.
     */
    @Ignore
    public GameStateTakePicture(int treeId, int gameId, Tree.GameCategories gameCategory) {
        this(treeId, gameId, gameCategory, null);
    }

    public GameStateTakePicture(int treeId, int gameId, Tree.GameCategories gameCategory, Integer selectedImageId) {
        super(treeId, gameId, gameCategory);
        this.selectedImageId = selectedImageId;
    }
}
