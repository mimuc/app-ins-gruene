package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.lmu.treeapp.contentData.database.entities.app.UserProfileState;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserProfileDao {

    @Query("SELECT * FROM UserProfileState WHERE id=:id LIMIT 1")
    Single<UserProfileState> getById(int id);

    @Query("SELECT * FROM UserProfileState")
    Single<List<UserProfileState>> getAll();

    @Query("SELECT * FROM UserProfileState ORDER BY id ASC LIMIT 1")
    Maybe<UserProfileState> getFirst();

    @Insert
    Single<Long> insertOne(UserProfileState model);

    @Insert
    Completable insertAll(UserProfileState... models);

    @Update
    Completable updateOne(UserProfileState model);

    @Delete
    Completable deleteOne(UserProfileState model);
}
