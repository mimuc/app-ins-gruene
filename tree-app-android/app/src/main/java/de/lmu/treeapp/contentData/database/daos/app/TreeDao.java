package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.app.TreeModel;

@Dao
public interface TreeDao {

    @Query("SELECT * FROM TreeModel")
    List<TreeModel> getAll();

    @Query("SELECT * FROM TreeModel WHERE uid=:uid LIMIT 1")
    TreeModel getById(int uid);

    @Insert
    void insertOne(TreeModel model);

    @Insert
    void insertAll(TreeModel... models);

    @Insert
    void insertAll(List<TreeModel> models);

    @Update
    void update(TreeModel... models);

    @Delete
    void deleteOne(TreeModel model);
}
