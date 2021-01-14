package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameTakePictureRelations;

@Dao
public abstract class GameTakePictureDao extends GameBaseDao<GameTakePictureRelations> {

    public GameTakePictureDao() {
        super(Minigame_Base.MinigameTypes.TakePicture);
    }
}

