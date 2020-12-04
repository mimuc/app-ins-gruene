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
    void InsertOne(TreeModel model);

    @Insert
    void InsertAll(TreeModel... models);

    @Insert
    void InsertAll(List<TreeModel> models);

    @Update
    void Update(TreeModel... models);

    @Delete
    void DeleteOne(TreeModel model);
}
