package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;

import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameOnlyDescriptionImageItem;
import de.lmu.treeapp.contentData.database.entities.content.GameOnlyDescriptionRelations;

@Dao
public abstract class GameOnlyDescriptionDao extends GameBaseDao<GameOnlyDescriptionRelations> {

    public GameOnlyDescriptionDao() {
        super(Minigame_Base.MinigameTypes.OnlyDescription);
    }


    @Override
    public List<GameOnlyDescriptionRelations> getAll() {
        List<GameOnlyDescriptionRelations> gameOnlyDescriptionRelationsList = super.getAll();

        //clone elements
        for (GameOnlyDescriptionRelations onlyDescriptionRelations : gameOnlyDescriptionRelationsList) {
            List<GameOnlyDescriptionImageItem> items = onlyDescriptionRelations.getImageItems();
            int x = items.size();
            if (items.size() != 0) {
                int amount = items.get(0).amount;

                for (int i = 0; i < x * amount - x; i++) {
                    GameOnlyDescriptionImageItem item = new GameOnlyDescriptionImageItem(items.get(i));
                    items.add(item);
                }
            }
        }
        return gameOnlyDescriptionRelationsList;
    }

}

