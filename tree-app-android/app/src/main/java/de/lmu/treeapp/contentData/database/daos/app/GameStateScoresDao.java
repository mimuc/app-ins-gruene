package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;

import de.lmu.treeapp.contentData.database.entities.app.GameStateScore;

@Dao
public abstract class GameStateScoresDao extends AbstractGameStateDao<GameStateScore> {

    public GameStateScoresDao() {
        super(GameStateScore.class);
    }
}
