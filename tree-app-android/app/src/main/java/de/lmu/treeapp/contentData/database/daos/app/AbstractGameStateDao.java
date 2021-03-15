package de.lmu.treeapp.contentData.database.daos.app;

import de.lmu.treeapp.contentData.database.entities.app.IGameState;

public abstract class AbstractGameStateDao<T extends IGameState> extends AbstractGameStateRelationsDao<T, T> {

    public AbstractGameStateDao(Class<T> clazz) {
        super(clazz, clazz);
    }
}
