package de.lmu.treeapp.minigames;

public class Minigame_InputStringAnswer extends Minigame_Base {

    private String rightAnswer;

    private void FillRightAnswer(String _rightAnswer){
        rightAnswer = _rightAnswer.trim().toLowerCase();
    }

    public Boolean CheckAnswer(String givenAnswer){
        if (givenAnswer.trim().toLowerCase() == rightAnswer){
            return true;
        }
        else return false;
    }
}
