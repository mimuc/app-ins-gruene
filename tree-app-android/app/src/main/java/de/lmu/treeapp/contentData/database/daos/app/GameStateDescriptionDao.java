package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.List;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.entities.app.GameStateDescription;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface GameStateDescriptionDao {

    @Transaction
    @Query("SELECT * FROM GameStateDescription")
    List<GameStateDescription> getAll();

    @Query("SELECT * FROM GameStateDescription WHERE  gameId=:gameId AND treeId=:treeId AND gameCategory=:gameCategory")
    Single<List<GameStateDescription>> getDescription(int gameId, int treeId, @TypeConverters(TypeConversion.class) Tree.GameCategories gameCategory);

    @Update
    Completable update(GameStateDescription... models);

    @Insert
    Single<Long> insertOne(GameStateDescription model);
}
