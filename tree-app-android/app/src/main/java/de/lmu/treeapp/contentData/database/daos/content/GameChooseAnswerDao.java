package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.content.GameChooseAnswerRelations;

@Dao
public interface GameChooseAnswerDao {

    @Transaction
    @Query("SELECT * FROM GameChooseAnswer")
    List<GameChooseAnswerRelations> getAll();

    @Transaction
    @Query("SELECT * FROM GameChooseAnswer WHERE id=:id LIMIT 1")
    GameChooseAnswerRelations getById(int id);
}

