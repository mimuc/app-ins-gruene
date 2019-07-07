package de.lmu.treeapp.minigames;

import de.lmu.treeapp.minigames.components.AnswerOption;

public class Minigame_ChooseAnswer extends Minigame_Base {

    public AnswerOption[] options;

    public Boolean CheckAnswer(AnswerOption givenAnswer){
        if (givenAnswer.right) return true;
        else return false;
    }
}
