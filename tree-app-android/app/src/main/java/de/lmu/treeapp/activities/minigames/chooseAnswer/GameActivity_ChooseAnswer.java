package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;
import de.lmu.treeapp.contentData.DataManager;

public class GameActivity_ChooseAnswer extends GameActivity_Base implements ChooseAnswer_Options_RecyclerViewAdapter.OptionClickInterface {

    private RecyclerView optionsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_game__choose_answer);
        super.onCreate(savedInstanceState);
        setupOptionRecyclerView();
    }

    private void setupOptionRecyclerView(){
        optionsRecyclerView = findViewById(R.id.game_chooseAnswer_recyclerView);
        optionsRecyclerView.setHasFixedSize(true);
        int columns = 2;
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), columns);
        optionsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        RecyclerView.Adapter recyclerViewAdapter = new ChooseAnswer_Options_RecyclerViewAdapter(this,(Minigame_ChooseAnswer) gameContent, getApplicationContext(), parentTree, parentCategory);
        optionsRecyclerView.setAdapter(recyclerViewAdapter);
    }

    /*@Override
    public void optionClicked(AnswerOption option){
        if (option.right) {
            Toast.makeText(getApplicationContext(), "Richtig", Toast.LENGTH_LONG).show();
            //Intent nextQuestion = new Intent(getApplicationContext(), GameActivity_ChooseAnswer.class);
           // nextQuestion.putExtra("TreeId", treeId);
            //nextQuestion.putExtra("Category", parentCategory);
            //nextQuestion.putExtra("GameId", getNextGameID());
           // startActivity(nextQuestion);
            showPopupWindow(option, optionsRecyclerView);
        } else {
            Toast.makeText(getApplicationContext(), "Falsch", Toast.LENGTH_LONG).show();
            //new PopUp(getApplicationContext()).showPopupWindow(this.optionsRecyclerView);
            //PopUp popUpObject = new PopUp();//context, "richtig"
           // popUpObject.showPopupWindow(this.optionsRecyclerView);
            showPopupWindow(option, optionsRecyclerView);
        }
 }*/
    int count = ChooseAnswer_Options_RecyclerViewAdapter.count;
    @Override
    public void optionClicked(AnswerOption option){
        showPopupWindow(option, optionsRecyclerView);
    }

    /*protected void showNextGame(){
        Intent nextQuestion = new Intent(getApplicationContext(), GameActivity_ChooseAnswer.class);
        nextQuestion.putExtra("TreeId", treeId);
        nextQuestion.putExtra("Category", parentCategory);
        nextQuestion.putExtra("GameId", getNextGameID());
        startActivity(nextQuestion);
    }*/

    // Create a PopupWindow after an option was clicked: Gives feedback to the user's answer
    protected void showPopupWindow(AnswerOption option, View view) {

        //Create a View object through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.activity_popup, null);

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, view.getWidth(), view.getHeight(), false);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window (Text and Button)
        TextView popUpText = popupView.findViewById(R.id.text_result);
        Button forwardButton = popupView.findViewById(R.id.forward_next_game);

        // Set the text of the popup based on the given answer
        if(option.right){
            if(count!=3){
                popUpText.setText("Super!");
                forwardButton.setText("Weiter");
            } else {
                popUpText.setText("Super!");
                forwardButton.setText("Fertig!");
            }
        }else {
            if(count!=3){
                popUpText.setText("Leider Falsch");
                forwardButton.setText("Weiter");
            } else {
                popUpText.setText("Leider falsch");
                forwardButton.setText("Fertig!");
            }
        }

        //Go to the next question of the quiz game. If all 3 questions are done, go back to the game selection activity.
        forwardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count>=3){
                    Toast.makeText(v.getContext(), "Fertig!!!", Toast.LENGTH_SHORT).show();
                    onQuizSuccess();
                } else {
                   // onQuizSuccess();
                    Toast.makeText(v.getContext(), "Weiter wurde geklickt", Toast.LENGTH_SHORT).show();
                    Intent nextQuestion = new Intent(getApplicationContext(), GameActivity_ChooseAnswer.class);
                    nextQuestion.putExtra("TreeId", treeId);
                    nextQuestion.putExtra("Category", parentCategory);
                    nextQuestion.putExtra("GameId", getNextGameID());
                    popupWindow.dismiss();
                    startActivity(nextQuestion);
                }
            }
        });
    }
}
