package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;

import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;

@Dao
public abstract class GameStateTakePictureDao extends AbstractGameStateDao<GameStateTakePictureImage> {

    public GameStateTakePictureDao() {
        super(GameStateTakePictureImage.class);
    }
}
