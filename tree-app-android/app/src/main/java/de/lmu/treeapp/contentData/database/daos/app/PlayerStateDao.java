package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;
import androidx.room.Query;

import de.lmu.treeapp.contentData.database.entities.app.PlayerState;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PlayerStateDao extends IBaseDao<PlayerState> {

    @Query("SELECT * FROM PlayerState WHERE id = 0 LIMIT 1")
    Single<PlayerState> getFirst();

    @Query("SELECT * FROM PlayerState WHERE id = :id LIMIT 1")
    Single<PlayerState> getById(int id);
}
