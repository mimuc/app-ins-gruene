package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.*;

import de.lmu.treeapp.contentData.database.entities.app.GameStateScore;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

@Dao
public interface GameStateScoresDao {

    @Transaction
    @Query("SELECT * FROM GameStateScore")
    List<GameStateScore> getAll();

    @Query("SELECT * FROM GameStateScore WHERE tree_x_gameId=:tree_x_gameId")
    Single<GameStateScore> getById(int tree_x_gameId);

    @Update
    Completable update(GameStateScore... models);

    @Insert
    Single<Long> insertOne(GameStateScore model);

}
