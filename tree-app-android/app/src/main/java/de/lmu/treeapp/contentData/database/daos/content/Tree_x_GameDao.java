package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.TypeConverters;

import java.util.List;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.entities.content.Tree_x_Game;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;
import io.reactivex.rxjava3.core.Single;

@Dao
public abstract class Tree_x_GameDao {

    @Transaction
    @Query("SELECT * FROM Tree_x_Game")
    public abstract List<Tree_x_Game> getAll();

    @Transaction
    @Query("SELECT * FROM Tree_x_Game WHERE id=:id LIMIT 1")
    public abstract Tree_x_Game getById(int id);

    @Transaction
    @Query("SELECT * FROM Tree_x_Game WHERE treeId=:treeId AND gameId=:gameId AND gameCategory=:gameCategory LIMIT 1")
    public abstract Single<Tree_x_Game> getByCompositeKey(int treeId, int gameId, @TypeConverters(TypeConversion.class) Tree.GameCategories gameCategory);
}
