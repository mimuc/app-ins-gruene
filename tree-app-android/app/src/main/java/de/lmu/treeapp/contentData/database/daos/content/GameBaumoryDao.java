package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.content.GameBaumoryRelations;

@Dao
public interface GameBaumoryDao {

    @Transaction
    @Query("SELECT * FROM GameBaumory")
    List<GameBaumoryRelations> getAll();

    @Transaction
    @Query("SELECT * FROM GameBaumory WHERE id=:id LIMIT 1")
    GameBaumoryRelations getById(int id);
}

