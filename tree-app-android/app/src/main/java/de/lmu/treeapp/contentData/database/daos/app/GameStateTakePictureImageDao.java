package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface GameStateTakePictureImageDao extends IBaseDao<GameStateTakePictureImage> {

    @Transaction
    @Query("SELECT * FROM GameStateTakePictureImage")
    Single<List<GameStateTakePictureImage>> getAll();

    @Transaction
    @Query("SELECT * FROM GameStateTakePictureImage WHERE id = :id LIMIT 1")
    Single<GameStateTakePictureImage> getById(int id);
}
