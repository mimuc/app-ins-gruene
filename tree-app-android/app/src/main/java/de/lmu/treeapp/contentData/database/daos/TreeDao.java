package de.lmu.treeapp.contentData.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.TreeModel;

@Dao
public interface TreeDao {

    @Query("SELECT * FROM treemodel")
    List<TreeModel> getAll();

    @Query("SELECT * FROM treemodel WHERE uid=:uid LIMIT 1")
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
