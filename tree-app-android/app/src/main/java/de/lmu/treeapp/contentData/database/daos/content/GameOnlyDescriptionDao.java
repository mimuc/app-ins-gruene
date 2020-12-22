package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.content.GameOnlyDescriptionRelations;

@Dao
public interface GameOnlyDescriptionDao {

    @Transaction
    @Query("SELECT * FROM GameBase WHERE gameType = 'OnlyDescription'")
    List<GameOnlyDescriptionRelations> getAll();

    @Transaction
    @Query("SELECT * FROM GameBase WHERE gameType = 'OnlyDescription' AND id=:id LIMIT 1")
    GameOnlyDescriptionRelations getById(int id);
}

