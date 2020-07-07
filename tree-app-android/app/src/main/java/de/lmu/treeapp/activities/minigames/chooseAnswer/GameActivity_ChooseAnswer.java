package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;
import de.lmu.treeapp.contentData.DataManager;

public class GameActivity_ChooseAnswer extends GameActivity_Base implements ChooseAnswer_Options_RecyclerViewAdapter.OptionClickInterface {

    RecyclerView optionsRecyclerView;
    Dialog popupWindow;
    Button btnAccept;
    TextView popupTitle, popupText, popup_result_text;
    ImageView popup_result_image;
    private int current = ChooseAnswer_Options_RecyclerViewAdapter.current;
    protected static String resultText;
    protected static int resultImage;
    private int showAnswer = 0;

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

        //RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        //optionsRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        RecyclerView.Adapter recyclerViewAdapter = new ChooseAnswer_Options_RecyclerViewAdapter(this,(Minigame_ChooseAnswer) gameContent, getApplicationContext(), parentTree, parentCategory);
        optionsRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void optionClicked(AnswerOption option){
        if(option.right) {
            showPositivePopup();
        } else {
            showAnswer=showAnswer+1;
            showNegativePopup(option);
        }
    }

    //create popup which gives feedback to the user's answer
    private void showPositivePopup() {
        popupWindow.setContentView(R.layout.popup_quiz_positive);
        btnAccept = popupWindow.findViewById(R.id.forward_next_game_positive);
        popupTitle = popupWindow.findViewById(R.id.popup_positive_title);

        if(current>0) {
            btnAccept.setText("Weiter");
        }else{
            btnAccept.setText("Fertig!");
        }

        ViewCompat.animate(btnAccept).setStartDelay(200).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();

        //close the popup to repeat the question or finish the game and go back to the overview
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(current>0){
                    showNextQuestion();
                }else{
                    DataManager.getInstance(getApplicationContext()).GameCompleted(parentCategory, gameContent.uid, parentTree);
                    onQuizSuccess();
                }
            }
        });

        popupWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.show();

        Window window = popupWindow.getWindow();
        window.setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
    }

    //create popup which gives feedback to the user's answer: the right answer is shown after 2 wrong given answers
    private void showNegativePopup(AnswerOption option) {
        popupWindow.setContentView(R.layout.popup_quiz_negative);
        btnAccept = popupWindow.findViewById(R.id.repeat_game);
        popupTitle = popupWindow.findViewById(R.id.popup_negative_title);
        popupText = popupWindow.findViewById(R.id.popup_negative_text);

        if(showAnswer<2){
            popup_result_text = popupWindow.findViewById(R.id.popup_answer_text);
            popup_result_text.setText("Probier's doch noch einmal...einen Versuch hast du übrig!");
            popup_result_text.setVisibility(View.VISIBLE);
            btnAccept.setVisibility(View.VISIBLE);

            ViewCompat.animate(popup_result_text).setStartDelay(400).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();
            ViewCompat.animate(btnAccept).setStartDelay(800).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();

        }else{

            if(option.type == AnswerOption.OptionTypes.text) {
                popup_result_text = popupWindow.findViewById(R.id.popup_answer_text);
                popup_result_text.setText(resultText);
                popup_result_text.setVisibility(View.VISIBLE);

                ViewCompat.animate(popup_result_text).setStartDelay(600).alpha(1).setDuration(400).setInterpolator(new DecelerateInterpolator(1.2f)).start();

            }else if(option.type == AnswerOption.OptionTypes.image){
                popup_result_image = popupWindow.findViewById(R.id.popup_answer_picture);
                popup_result_image.setBackgroundResource(resultImage);
                popup_result_image.setVisibility(View.VISIBLE);

                ViewCompat.animate(popup_result_image).setStartDelay(600).alpha(1).setDuration(400).setInterpolator(new DecelerateInterpolator(1.2f)).start();
            }

            if(current>0){
                btnAccept.setText("Weiter");
            } else {
                btnAccept.setText("Fertig");
            }

            btnAccept.setVisibility(View.VISIBLE);
            ViewCompat.animate(popupText).setStartDelay(500).alpha(1).setDuration(200).setInterpolator(new DecelerateInterpolator(1.2f)).start();
            ViewCompat.animate(btnAccept).setStartDelay(900).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();
        }

        //close the popup to repeat the question or show the next question
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(showAnswer<2){
                    popupWindow.dismiss();
                } else {
                    if(current>0){
                        ChooseAnswer_Options_RecyclerViewAdapter.current--;
                        showNextQuestion();
                    }else{
                        DataManager.getInstance(getApplicationContext()).GameCompleted(parentCategory, gameContent.uid, parentTree);
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
        finish(); // Removes the current quiz activity from the stack
        startActivity(nextQuestion);
    }
}




