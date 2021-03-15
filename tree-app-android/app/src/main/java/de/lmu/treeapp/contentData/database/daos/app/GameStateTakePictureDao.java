package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;

import java.util.List;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePicture;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureRelations;

@Dao
public abstract class GameStateTakePictureDao extends AbstractGameStateRelationsDao<GameStateTakePicture, GameStateTakePictureRelations> {

    public GameStateTakePictureDao() {
        super(GameStateTakePicture.class, GameStateTakePictureRelations.class);
    }

    public static GameStateTakePictureImage getLatestCraftImage(List<GameStateTakePictureRelations> gameStateTakePictureRelationsList) {
        // TODO replace with stream on API 24
        for (GameStateTakePictureRelations gameStateTakePictureRelations : gameStateTakePictureRelationsList) {
            for (GameStateTakePictureImage tpImg : gameStateTakePictureRelations.images) {
                if (tpImg.specialGameName.equals("Bastelaufgabe")) {
                    return tpImg;
                }
            }
        }
        return null;
    }

    public static GameStateTakePictureRelations filterImages(List<GameStateTakePictureRelations> gameStateTakePictureRelations, Tree.GameCategories category) {
        // TODO replace with stream on API 24
        for (GameStateTakePictureRelations relation : gameStateTakePictureRelations) {
            if (relation.getCategory() == category) {
                return relation;
            }
        }
        return null;
    }
}
