package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import de.lmu.treeapp.contentData.database.entities.app.PlayerState;
import io.reactivex.rxjava3.core.Completable;

@Dao
public interface PlayerDao {
    @Query("SELECT * FROM PlayerState WHERE uid=0 LIMIT 1")
    PlayerState get();

    @Query("SELECT * FROM PlayerState WHERE uid=:uid LIMIT 1")
    PlayerState getById(int uid);

    @Insert
    void insertOne(PlayerState model);

    @Update
    Completable updateOne(PlayerState model);
}
