package de.lmu.treeapp.minigames;

public class Minigame_ChooseStringAnswer extends Minigame {

    public String[] options;
    public String rightAnswer;

    public Minigame_ChooseStringAnswer(String _id, String _name, String _description, String[] _options, String _rightAnswer){
        super(_id,_name,_description);
        options = _options;
        FillRightAnswer(_rightAnswer);
    }


    private void FillRightAnswer(String _rightAnswer){
        rightAnswer = _rightAnswer.trim().toLowerCase();
    }

    public Boolean checkAnswer(String givenAnswer){
        if (givenAnswer.trim().toLowerCase() == rightAnswer){
            return true;
        }
        else return false;
    }
}
