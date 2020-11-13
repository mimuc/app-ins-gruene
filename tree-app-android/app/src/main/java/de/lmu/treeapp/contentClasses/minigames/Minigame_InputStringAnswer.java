package de.lmu.treeapp.contentClasses.minigames;

public class Minigame_InputStringAnswer extends Minigame_Base {

    private String rightAnswer = "";

    public void FillRightAnswer(String _rightAnswer) {
        rightAnswer = _rightAnswer.trim().toLowerCase();
    }

    public Boolean CheckAnswer(String givenAnswer) {
        // If specific answer is expected:
        /*
        if (givenAnswer.trim().equalsIgnoreCase(rightAnswer) || rightAnswer == ""){
            return true;
        }
        else return false;
        */
        return true;
    }
}
