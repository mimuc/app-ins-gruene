package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.*;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.entities.app.GameStateInputString;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

@Dao
public interface GameStateInputStringDao {

    @Transaction
    @Query("SELECT * FROM GameStateInputString")
    List<GameStateInputString> getAll();

    @Query("SELECT * FROM GameStateInputString WHERE  gameId=:gameId AND treeId=:treeId AND gameCategory=:gameCategory")
    Single<List<GameStateInputString>> getInputString(int gameId, int treeId, @TypeConverters(TypeConversion.class) Tree.GameCategories gameCategory);

    @Update
    Completable update(GameStateInputString... models);

    @Insert
    Single<Long> insertOne(GameStateInputString model);

}
