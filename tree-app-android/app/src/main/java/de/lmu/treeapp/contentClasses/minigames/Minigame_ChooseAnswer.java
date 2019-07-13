package de.lmu.treeapp.contentClasses.minigames;

import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;

public class Minigame_ChooseAnswer extends Minigame_Base {

    public List<AnswerOption> options;

    public Boolean CheckAnswer(AnswerOption givenAnswer){
        if (givenAnswer.right) return true;
        else return false;
    }
}
