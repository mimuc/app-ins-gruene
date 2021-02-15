package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;

import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropItem;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropRelations;

@Dao
public abstract class GameLeafOrderDao extends GameBaseDao<GameDragDropRelations> {

    public GameLeafOrderDao() {
        super(Minigame_Base.MinigameTypes.LeafOrder);
    }

    /**
     * @return All LeafOrder games, but include global drop items, used for every game.
     */
    @Override
    public List<GameDragDropRelations> getAll() {
        List<GameDragDropRelations> gameLeafOrderRelationsList = super.getAll();

        //clone elements
        for (GameDragDropRelations gameLeafOrderRelations : gameLeafOrderRelationsList) {
            List<GameDragDropItem> items = gameLeafOrderRelations.getItems();
            if (items.size() == 2) {
                for (int i = 2; i < (gameLeafOrderRelations.getZones().size()); i++) {
                    GameDragDropItem item1 = new GameDragDropItem(items.get(0));
                    item1.setId(items.get(0).id + i);
                    items.add(item1);
                    i += 1;
                    GameDragDropItem item2 = new GameDragDropItem(items.get(1));
                    item2.setId(items.get(0).id + i);
                    items.add(item2);
                }
            }
        }
        return gameLeafOrderRelationsList;
    }

}

