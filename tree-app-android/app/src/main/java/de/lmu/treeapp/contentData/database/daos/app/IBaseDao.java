package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface IBaseDao<T> {

    @Update
    Completable update(T... models);

    @Update
    Completable updateOne(T model);

    @Insert
    Single<Long> insertOne(T model);

    @Insert
    Single<List<Long>> insertAll(T... models);

    @Insert
    Single<List<Long>> insertAll(List<T> models);

    @Delete
    Completable deleteOne(T model);
}
