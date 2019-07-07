package de.lmu.treeapp.database.daos;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.lmu.treeapp.database.entities.MinigameModel;

public interface MinigameDao {

    @Query("SELECT * FROM minigamemodel WHERE uid=:uid LIMIT 1")
    MinigameModel getById(int uid);

    @Query("SELECT * FROM minigamemodel")
    List<MinigameModel> getAll();

    @Insert
    void InsertOne(MinigameModel model);

    @Insert
    void InsertAll(MinigameModel... models);

    @Update
    void UpdateOne(MinigameModel model);

    @Delete
    void DeleteOne(MinigameModel model);
}
