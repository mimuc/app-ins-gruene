package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.IGameBase;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;

/**
 * The persistent database tree model with its related attributes.
 */
public class GameChooseAnswerRelations implements IGameBase {
    @Embedded
    public GameChooseAnswer game;

    @Relation(parentColumn = "id", entityColumn = "questionId")
    public List<GameChooseAnswerOption> options;

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
        return Minigame_Base.MinigameTypes.ChooseAnswer;
    }

    @Override
    public String getDescription() {
        return game.description;
    }

    @Override
    public String getImageResource() {
        return null; // "leaf"
    }

    public List<GameChooseAnswerOption> getOptions() {
        return options;
    }
}
