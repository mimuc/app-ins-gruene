package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.content.TreeRelations;

@Dao
public interface TreeDao {

    @Transaction
    @Query("SELECT * FROM TreeModel")
    List<TreeRelations> getAll();

    @Transaction
    @Query("SELECT * FROM TreeModel WHERE id=:id LIMIT 1")
    TreeRelations getById(int id);
}
