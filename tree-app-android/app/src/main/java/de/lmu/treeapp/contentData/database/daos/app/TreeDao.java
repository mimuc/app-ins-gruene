package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.app.TreeState;
import io.reactivex.rxjava3.core.Completable;

@Dao
public interface TreeDao {

    @Query("SELECT * FROM TreeState")
    List<TreeState> getAll();

    @Query("SELECT * FROM TreeState WHERE uid=:uid LIMIT 1")
    TreeState getById(int uid);

    @Insert
    void insertOne(TreeState model);

    @Insert
    void insertAll(TreeState... models);

    @Insert
    void insertAll(List<TreeState> models);

    @Update
    Completable update(TreeState... models);

    @Delete
    void deleteOne(TreeState model);
}
