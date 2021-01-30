package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import de.lmu.treeapp.contentData.database.entities.content.WantedPoster;

import java.util.List;

@Dao
public interface WantedPosterTextDao {

    @Transaction
    @Query("SELECT * FROM WantedPoster WHERE treeId=:treeId")
    List<WantedPoster> getTextsByTreeId(int treeId);
}

