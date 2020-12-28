package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.app.TreeState;
import de.lmu.treeapp.contentData.database.entities.app.TreeStateRelations;
import io.reactivex.rxjava3.core.Completable;

@Dao
public interface TreeDao {

    @Query("SELECT * FROM TreeState")
    List<TreeStateRelations> getAll();

    @Query("SELECT * FROM TreeState WHERE id=:id LIMIT 1")
    TreeStateRelations getById(int id);

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
