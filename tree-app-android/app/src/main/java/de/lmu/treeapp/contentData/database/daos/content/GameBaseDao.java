package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;

@Dao
public abstract class GameBaseDao<T> {

    protected Minigame_Base.MinigameTypes mGameType;

    public GameBaseDao(Minigame_Base.MinigameTypes gameType) {
        mGameType = gameType;
    }

    @Transaction
    @Query("SELECT * FROM GameBase WHERE gameType = :gameType")
    protected abstract List<T> mGetAll(String gameType);

    public List<T> getAll() {
        return mGetAll(mGameType.name());
    }

    @Transaction
    @Query("SELECT * FROM GameBase WHERE gameType = :gameType AND id=:id LIMIT 1")
    protected abstract T mGetById(String gameType, int id);

    public T getById(int id) {
        return mGetById(mGameType.name(), id);
    }
}

