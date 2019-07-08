package de.lmu.treeapp.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.lmu.treeapp.database.entities.TreeModel;

@Dao
public interface TreeDao {

    @Query("SELECT * FROM treemodel")
    List<TreeModel> getAll();

    @Query("SELECT * FROM treemodel WHERE uid=:uid LIMIT 1")
    TreeModel getById(int uid);

    @Query("SELECT * FROM treemodel WHERE name LIKE :name LIMIT 1")
    TreeModel getByName(String name);

    @Insert
    void InsertOne(TreeModel model);

    @Insert
    void InsertAll(TreeModel... models);

    @Insert
    void InsertAll(List<TreeModel> models);

    @Update
    void UpdateOne(TreeModel model);

    @Delete
    void DeleteOne(TreeModel model);
}
