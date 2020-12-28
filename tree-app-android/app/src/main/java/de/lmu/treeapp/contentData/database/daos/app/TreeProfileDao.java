package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.app.TreeProfileState;

@Dao
public interface TreeProfileDao {

    @Query("SELECT * FROM TreeProfileState WHERE uid=:uid LIMIT 1")
    TreeProfileState getById(int uid);

    @Query("SELECT * FROM TreeProfileState")
    List<TreeProfileState> getAll();

    @Insert
    void insertOne(TreeProfileState model);

    @Insert
    void insertAll(TreeProfileState... models);

    @Update
    void updateOne(TreeProfileState model);

    @Delete
    void deleteOne(TreeProfileState model);
}
