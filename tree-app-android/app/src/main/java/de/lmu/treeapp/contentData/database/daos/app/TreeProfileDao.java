package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.app.TreeProfileModel;

@Dao
public interface TreeProfileDao {

    @Query("SELECT * FROM TreeProfileModel WHERE uid=:uid LIMIT 1")
    TreeProfileModel getById(int uid);

    @Query("SELECT * FROM TreeProfileModel")
    List<TreeProfileModel> getAll();

    @Insert
    void InsertOne(TreeProfileModel model);

    @Insert
    void InsertAll(TreeProfileModel... models);

    @Update
    void UpdateOne(TreeProfileModel model);

    @Delete
    void DeleteOne(TreeProfileModel model);
}
