package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.IGameBase;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;

/**
 * The persistent database tree model with its related attributes.
 */
public class GameBaumoryRelations implements IGameBase {
    @Embedded
    public GameBaumory game;

    @Relation(parentColumn = "baumoryId", entityColumn = "baumoryId")
    public List<GameBaumoryCard> cards;

    @Override
    public int getId() {
        return game.id;
    }

    @Override
    public String getName() {
        return game.name;
    }

    @Override
    public Minigame_Base.MinigameTypes getType() {
        return Minigame_Base.MinigameTypes.Baumory;
    }

    @Override
    public String getDescription() {
        return game.description;
    }

    @Override
    public String getImageResource() {
        return game.imageResource;
    }

    public List<GameBaumoryCard> getCards() {
        return cards;
    }
}
