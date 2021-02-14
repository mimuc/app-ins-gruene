package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.app.TreeState;
import de.lmu.treeapp.contentData.database.entities.app.TreeStateRelations;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface TreeStateDao extends IBaseDao<TreeState> {

    @Transaction
    @Query("SELECT * FROM TreeState")
    Single<List<TreeStateRelations>> getAll();

    @Transaction
    @Query("SELECT * FROM TreeState WHERE id = :id LIMIT 1")
    Single<TreeStateRelations> getById(int id);
}
