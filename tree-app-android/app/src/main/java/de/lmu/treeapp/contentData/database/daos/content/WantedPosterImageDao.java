package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import de.lmu.treeapp.contentData.database.entities.content.WantedPosterImage;

import java.util.List;

@Dao
public interface WantedPosterImageDao {

    @Transaction
    @Query("SELECT * FROM WantedPosterImage WHERE treeId=:treeId")
    List<WantedPosterImage> getImagesByTreeId(int treeId);
}

