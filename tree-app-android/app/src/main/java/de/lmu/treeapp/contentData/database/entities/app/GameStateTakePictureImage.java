package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.typeconversion.DateConverter;

@Entity
public class GameStateTakePictureImage extends AbstractGameState {

    public String imagePath;
    @TypeConverters(DateConverter.class)
    public Date creationDateTime;
    public String specialGameName;

    @Ignore
    public GameStateTakePictureImage(int treeId, int gameId, Tree.GameCategories gameCategory) {
        this(treeId, gameId, gameCategory, null, null, null);
    }

    public GameStateTakePictureImage(int treeId, int gameId, Tree.GameCategories gameCategory, String imagePath, Date creationDateTime, String specialGameName) {
        super(treeId, gameId, gameCategory);
        this.treeId = treeId;
        this.imagePath = imagePath;
        this.creationDateTime = creationDateTime;
        this.specialGameName = specialGameName;
    }

    public static GameStateTakePictureImage getLatestTakePictureImage(List<GameStateTakePictureImage> takePictureImages, Tree.GameCategories category) {
        GameStateTakePictureImage resImg = null;
        // TODO replace with stream on API 24
        for (GameStateTakePictureImage tpImg : takePictureImages) {
            if (tpImg.gameCategory == category && tpImg.specialGameName.equals("")) {
                // Search the most recent image by id
                if (resImg == null || resImg.creationDateTime.before(tpImg.creationDateTime)) {
                    resImg = tpImg;
                }
            }
        }
        return resImg;
    }

    public static GameStateTakePictureImage getLatestCraftTaskPictureImage(List<GameStateTakePictureImage> takePictureImages) {
        GameStateTakePictureImage resImg = null;
        for (GameStateTakePictureImage tpImg : takePictureImages) {
            if (tpImg.specialGameName.equals("Bastelaufgabe")) {
                resImg = tpImg;
            }
        }
        return resImg;
    }

    public static List<GameStateTakePictureImage> getTakePictureImages(List<GameStateTakePictureImage> takePictureImages, Tree.GameCategories category) {
        List<GameStateTakePictureImage> resImages = new ArrayList<>();
        for (GameStateTakePictureImage tpImg : takePictureImages) {
            if (tpImg.gameCategory == category) {
                resImages.add(tpImg);
            }
        }
        return resImages;
    }
}