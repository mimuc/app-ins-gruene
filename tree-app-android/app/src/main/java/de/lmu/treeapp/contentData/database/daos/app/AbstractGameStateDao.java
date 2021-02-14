package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.RawQuery;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.entities.app.AbstractGameState;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;
import io.reactivex.rxjava3.core.Single;

public abstract class AbstractGameStateDao<T extends AbstractGameState> implements IBaseDao<T> {

    private final Class<T> clazz;

    public AbstractGameStateDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    @RawQuery
    protected abstract Single<List<T>> getAll(SupportSQLiteQuery query);

    public Single<List<T>> getAll() {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM " + clazz.getSimpleName() + ";");
        return getAll(query);
    }

    @RawQuery
    protected abstract Single<T> getSingle(SupportSQLiteQuery query);

    public Single<T> getSingle(int treeId, int gameId, @TypeConverters(TypeConversion.class) Tree.GameCategories gameCategory) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM " + clazz.getSimpleName()
                + " WHERE treeId = " + treeId
                + " AND gameId = " + gameId
                + " AND gameCategory = '" + TypeConversion.gameCategoryToString(gameCategory) + "' LIMIT 1;");
        return getSingle(query);
    }

    @RawQuery
    protected abstract Single<List<T>> getList(SupportSQLiteQuery query);

    public Single<List<T>> getList(int treeId, int gameId, @TypeConverters(TypeConversion.class) Tree.GameCategories gameCategory) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM " + clazz.getSimpleName()
                + " WHERE treeId = " + treeId
                + " AND gameId = " + gameId
                + " AND gameCategory = '" + TypeConversion.gameCategoryToString(gameCategory) + "';");
        return getList(query);
    }

    public Class<T> getEntityClass() {
        return clazz;
    }
}
