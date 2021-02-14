package de.lmu.treeapp.contentData.database.daos.app;

import androidx.room.Dao;

import de.lmu.treeapp.contentData.database.entities.app.GameStateDescription;

@Dao
public abstract class GameStateDescriptionDao extends AbstractGameStateDao<GameStateDescription> {

    public GameStateDescriptionDao() {
        super(GameStateDescription.class);
    }
}
