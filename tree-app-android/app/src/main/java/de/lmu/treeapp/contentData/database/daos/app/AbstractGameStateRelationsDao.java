package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.entities.app.IGameState;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;
import io.reactivex.rxjava3.core.Single;

@Dao
public abstract class AbstractGameStateRelationsDao<T extends IGameState, S extends IGameState> implements IBaseDao<T> {
    private final Class<T> clazz;
    private final Class<S> resultClazz;

    public AbstractGameStateRelationsDao(Class<T> clazz, Class<S> resultClazz) {
        this.clazz = clazz;
        this.resultClazz = resultClazz;
    }

    @RawQuery
    protected abstract Single<List<S>> getAll(SupportSQLiteQuery query);

    public Single<List<S>> getAll() {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM " + clazz.getSimpleName() + ";");
        return getAll(query);
    }

    @RawQuery
    protected abstract Single<S> getSingle(SupportSQLiteQuery query);

    public Single<S> getSingle(int treeId, int gameId, @TypeConverters(TypeConversion.class) Tree.GameCategories gameCategory) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM " + clazz.getSimpleName()
                + " WHERE treeId = " + treeId
                + " AND gameId = " + gameId
                + " AND gameCategory = '" + TypeConversion.gameCategoryToString(gameCategory) + "' LIMIT 1;");
        return getSingle(query);
    }

    @RawQuery
    protected abstract Single<List<S>> getList(SupportSQLiteQuery query);

    public Single<List<S>> getList(int treeId, int gameId, @TypeConverters(TypeConversion.class) Tree.GameCategories gameCategory) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM " + clazz.getSimpleName()
                + " WHERE treeId = " + treeId
                + " AND gameId = " + gameId
                + " AND gameCategory = '" + TypeConversion.gameCategoryToString(gameCategory) + "';");
        return getList(query);
    }

    public Single<List<S>> getList(int treeId) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM " + clazz.getSimpleName()
                + " WHERE treeId = " + treeId + ";");
        return getList(query);
    }

    public Class<T> getEntityClass() {
        return clazz;
    }

    public Class<S> getResultClass() {
        return resultClazz;
    }
}
