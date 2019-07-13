package de.lmu.treeapp.contentData.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.TreeProfileModel;

@Dao
public interface TreeProfileDao {

    @Query("SELECT * FROM treeprofilemodel WHERE uid=:uid LIMIT 1")
    TreeProfileModel getById(int uid);

    @Query("SELECT * FROM treeprofilemodel")
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
