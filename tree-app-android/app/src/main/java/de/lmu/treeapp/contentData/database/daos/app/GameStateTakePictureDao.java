package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.List;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface GameStateTakePictureDao {

    //@Query("SELECT * FROM GameStateTakePictureImage WHERE txgId=:txgId")
    //Single<List<GameStateTakePictureImage>> getImages(int txgId);

    @Query("SELECT * FROM GameStateTakePictureImage WHERE gameId=:gameId AND treeId=:treeId AND gameCategory=:gameCategory")
    Single<List<GameStateTakePictureImage>> getImages(int gameId, int treeId, @TypeConverters(TypeConversion.class) Tree.GameCategories gameCategory);

    @Insert
    Single<Long> insertImage(GameStateTakePictureImage image);

    @Update
    Completable updateImage(GameStateTakePictureImage image);
}
