package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.content.TreeProfileCard;

@Dao
public interface TreeProfileDao {

    @Transaction
    @Query("SELECT * FROM TreeProfileCard WHERE treeId=:treeId")
    List<TreeProfileCard> getCardsByTreeId(int treeId);

    @Transaction
    @Query("SELECT * FROM TreeProfileCard WHERE id=:id LIMIT 1")
    TreeProfileCard getById(int id);
}

