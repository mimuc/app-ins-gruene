package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.content.GameBaumoryRelations;

@Dao
public interface GameBaumoryDao {

    @Transaction
    @Query("SELECT * FROM GameBase WHERE gameType = 'Baumory'")
    List<GameBaumoryRelations> getAll();

    @Transaction
    @Query("SELECT * FROM GameBase WHERE gameType = 'Baumory' AND id=:id LIMIT 1")
    GameBaumoryRelations getById(int id);
}

