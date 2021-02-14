package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;

import de.lmu.treeapp.contentData.database.entities.app.GameStateInputString;

@Dao
public abstract class GameStateInputStringDao extends AbstractGameStateDao<GameStateInputString> {

    public GameStateInputStringDao() {
        super(GameStateInputString.class);
    }
}
