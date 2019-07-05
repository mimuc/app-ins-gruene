package de.lmu.treeapp.minigames;

public class Minigame_InputStringAnswer extends Minigame {
    public String rightAnswer;

    public Minigame_InputStringAnswer(String _id, String _name, String _description, String _rightAnswer){
        super(_id,_name,_description);
        FillRightAnswer(_rightAnswer);
    }

    public void FillRightAnswer(String _rightAnswer){
        rightAnswer = _rightAnswer.trim().toLowerCase();
    }

    public Boolean checkAnswer(String givenAnswer){
        if (givenAnswer.trim().toLowerCase() == rightAnswer){
            return true;
        }
        else return false;
    }
}


