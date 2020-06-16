package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;

public class GameActivity_ChooseAnswer extends GameActivity_Base implements ChooseAnswer_Options_RecyclerViewAdapter.OptionClickInterface {

    RecyclerView optionsRecyclerView;
    private Dialog popupWindow;
    private Button btnAccept;
    private TextView popupTitle, popupText, popup_result_text;
    private ImageView popup_result_image;
    private int current = ChooseAnswer_Options_RecyclerViewAdapter.current;
    private int getAnswer = 0; //ChooseAnswer_Options_RecyclerViewAdapter.getAnswer;
    protected static String resultText = ChooseAnswer_Options_RecyclerViewAdapter.resultText;
    protected static int resultImage;
    //private int resultImage = ChooseAnswer_Options_RecyclerViewAdapter.resultImageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_game__choose_answer);
        super.onCreate(savedInstanceState);
        setupOptionRecyclerView();
        popupWindow = new Dialog(this);
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

    @Override
    public void optionClicked(AnswerOption option){
        if(option.right) {
            showPositivePopup();
        } else {
            getAnswer=getAnswer+1;
            showNegativePopup(option);
        }
    }

    //Create a popup window after an option was clicked: Gives feedback to the user's answer
    private void showPositivePopup() {
        popupWindow.setContentView(R.layout.popup_quiz_positive);
        btnAccept = (Button) popupWindow.findViewById(R.id.forward_next_game_positive);
        popupTitle = (TextView) popupWindow.findViewById(R.id.popup_positive_title);

        if (current<3) {
            btnAccept.setText("Weiter");
        } else {
            btnAccept.setText("Fertig!");
        }

        ViewCompat.animate(btnAccept).setStartDelay(300).alpha(1).setDuration(400).setInterpolator(new DecelerateInterpolator(1.2f)).start();

        //Close the popup or finish the game and go back to the overview
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(current<3){
                    showNextQuestion();
                }else{
                    onQuizSuccess();
                }
            }
        });

        popupWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.show();

        Window window = popupWindow.getWindow();
        window.setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
    }

    private void showNegativePopup(AnswerOption option) {
        popupWindow.setContentView(R.layout.popup_quiz_negative);
        btnAccept = (Button) popupWindow.findViewById(R.id.repeat_game);
        popupTitle = (TextView) popupWindow.findViewById(R.id.popup_negative_title);
        popupText = (TextView) popupWindow.findViewById(R.id.popup_negative_text);

        if(getAnswer<2){
            popupText.setVisibility(View.INVISIBLE);
            popup_result_text = (TextView) popupWindow.findViewById(R.id.popup_right_answer_text);
            popup_result_text.setText("Probier's doch noch einmal...einen Versuch hast du übrig!");
            popup_result_text.setVisibility(View.VISIBLE);

            ViewCompat.animate(popup_result_text).setStartDelay(900).alpha(1).setDuration(900).setInterpolator(new DecelerateInterpolator(1.2f)).start();
            ViewCompat.animate(btnAccept).setStartDelay(100).alpha(1).setDuration(900).setInterpolator(new DecelerateInterpolator(1.2f)).start();

        }else{

            if(option.type == AnswerOption.OptionTypes.text) {
                popup_result_text = (TextView) popupWindow.findViewById(R.id.popup_right_answer_text);
                popup_result_text.setText(resultText);
                popup_result_text.setVisibility(View.VISIBLE);
            }else if(option.type == AnswerOption.OptionTypes.image){
                popup_result_image = (ImageView) popupWindow.findViewById(R.id.popup_right_answer_picture);
                popup_result_image.setBackgroundResource(resultImage);
                popup_result_image.setVisibility(View.VISIBLE);
                if(current<3){
                    btnAccept.setText("Weiter");
                } else btnAccept.setText("Fertig");


                ViewCompat.animate(popupText).setStartDelay(800).alpha(1).setDuration(900).setInterpolator(new DecelerateInterpolator(1.2f)).start();
                ViewCompat.animate(popup_result_image).setStartDelay(1000).alpha(1).setDuration(1000).setInterpolator(new DecelerateInterpolator(1.2f)).start();
                ViewCompat.animate(btnAccept).setStartDelay(1100).alpha(1).setDuration(900).setInterpolator(new DecelerateInterpolator(1.2f)).start();
            }
        }

        //Close the popup to repeat the question
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(getAnswer<2){
                    popupWindow.dismiss();
                } else {
                    if(current<3){
                        ChooseAnswer_Options_RecyclerViewAdapter.current++;
                        showNextQuestion();
                    }else{
                        onQuizSuccess();
                    }
                }
            }
        });

        popupWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.show();

        Window window = popupWindow.getWindow();
        window.setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
    }

    private void showNextQuestion(){
        Intent nextQuestion = new Intent(getApplicationContext(), GameActivity_ChooseAnswer.class);
        nextQuestion.putExtra("TreeId", treeId);
        nextQuestion.putExtra("Category", parentCategory);
        nextQuestion.putExtra("GameId", getNextQuizID());
        popupWindow.dismiss();
        finish();
        startActivity(nextQuestion);
        //overridePendingTransition(0,0);
        //nextQuestion.addFlags(nextQuestion.FLAG_ACTIVITY_NO_ANIMATION);
    }



        /*
        //Create a View object through inflater
        //LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        //final View popupView = inflater.inflate(R.layout.activity_popup, null);

        //Create a window with our parameters
        //final PopupWindow popupWindow = new PopupWindow(popupView, view.getWidth(), view.getHeight(), false);
        final Dialog popupWindow = new Dialog(this);
        popupWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Set the location of the window on the screen
        //popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        popupWindow.setContentView(findViewById(R.id.cardview_popup));

        //Initialize the elements of the popup window (text and button)
        //TextView popUpText = popupView.findViewById(R.id.text_result);
       // final Button forwardButton = popupView.findViewById(R.id.forward_next_game);
        TextView popUpText = findViewById(R.id.text_result);
        final Button forwardButton = findViewById(R.id.forward_next_game);

        //Set the text of the popup based on answer and process within the quiz
        if (option.right) {
            if (count != 3) {
                popUpText.setText("Super!");
                forwardButton.setText("Weiter");
            } else {
                popUpText.setText("Super!");
                forwardButton.setText("Fertig");
            }
        } else {
            if (count != 3) {
                popUpText.setText("Leider Falsch");
                forwardButton.setText("Nochmal");
            } else {
                popUpText.setText("Leider falsch");
                forwardButton.setText("Fertig!");
            }
        }

        //Go to the next question of the quiz game or repeat the current one. By finishing all questions go back to the game selection overview.
        forwardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(forwardButton.getText()=="Nochmal"){
                    popupWindow.dismiss();
                }
                else if(forwardButton.getText()=="Weiter"){
                    Intent nextQuestion = new Intent(getApplicationContext(), GameActivity_ChooseAnswer.class);
                    nextQuestion.putExtra("TreeId", treeId);
                    nextQuestion.putExtra("Category", parentCategory);
                    nextQuestion.putExtra("GameId", getNextQuizID());
                    popupWindow.dismiss();
                    finish();
                    startActivity(nextQuestion);
                    overridePendingTransition(0,0);
                    nextQuestion.addFlags(nextQuestion.FLAG_ACTIVITY_NO_ANIMATION);
                }
                else {
                    onQuizSuccess();
                }
            }
        });  */
}




