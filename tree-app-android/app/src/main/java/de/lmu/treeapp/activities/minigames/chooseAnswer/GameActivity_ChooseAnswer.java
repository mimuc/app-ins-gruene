package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.app.Dialog;
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
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.MediaType;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.entities.content.GameChooseAnswerOption;
import de.lmu.treeapp.contentData.database.entities.content.GameChooseAnswerRelations;

public class GameActivity_ChooseAnswer extends GameActivity_Base implements ChooseAnswer_Options_RecyclerViewAdapter.OptionClickInterface {

    public static int current;
    public static ArrayList<Integer> quizIDs = new ArrayList<>();
    protected static String resultText;
    protected static int resultImage;
    RecyclerView optionsRecyclerView;
    Adapter<ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder> recyclerViewAdapter;
    Dialog popupWindow;
    Button btnAccept;
    TextView popupTitle, popupText, popup_result_text, description;
    ImageView popup_result_image;
    private int showAnswer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        description = findViewById(R.id.game_description);
        optionsRecyclerView = findViewById(R.id.game_chooseAnswer_recyclerView);
        optionsRecyclerView.setHasFixedSize(true);
        setupOptionRecyclerView();
        popupWindow = new Dialog(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__choose_answer;
    }

    private void setupOptionRecyclerView() {
        recyclerViewAdapter = new ChooseAnswer_Options_RecyclerViewAdapter(this, (GameChooseAnswerRelations) gameContent, getApplicationContext(), parentTree, parentCategory);
        optionsRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void showNextQuestion() {
        gameContent = DataManager.getInstance(getApplicationContext()).GetMinigame(getNextQuizID());
        description.setText(gameContent.getDescription());
        setupOptionRecyclerView();
        showAnswer = 0;
        popupWindow.dismiss();
    }

    @Override
    public void optionClicked(GameChooseAnswerOption option) {
        if (option.isRight) {
            showPositivePopup();
        } else {
            showAnswer++;
            showNegativePopup(option);
        }
    }

    //create popup which gives feedback to the user's answer
    private void showPositivePopup() {
        popupWindow.setContentView(R.layout.popup_quiz_positive);
        btnAccept = popupWindow.findViewById(R.id.forward_next_game_positive);
        popupTitle = popupWindow.findViewById(R.id.popup_positive_title);

        // Checks if the current activity is last one
        if (current > 1) {
            btnAccept.setText(R.string.game_btn_next);
        } else btnAccept.setText(R.string.game_btn_finished);

        ViewCompat.animate(btnAccept).setStartDelay(200).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();

        //close the popup to repeat the question or finish the game and go back to the overview
        btnAccept.setOnClickListener(v -> {
            quizIDs.add(gameContent.getId());
            if (current > 1) {
                showNextQuestion();
                current--;
            } else {
                popupWindow.dismiss();
                onQuizSuccess(quizIDs);
            }
        });

        popupWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.show();

        Window window = popupWindow.getWindow();
        window.setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
    }

    //create popup which gives feedback to the user's answer: the right answer is shown after 2 wrong given answers
    private void showNegativePopup(GameChooseAnswerOption option) {
        popupWindow.setContentView(R.layout.popup_quiz_negative);
        btnAccept = popupWindow.findViewById(R.id.repeat_game);
        popupTitle = popupWindow.findViewById(R.id.popup_negative_title);
        popupText = popupWindow.findViewById(R.id.popup_negative_text);

        if (showAnswer < 2) {
            popupText.setText(R.string.game_popup_try_again);
            popupText.setVisibility(View.VISIBLE);
            btnAccept.setVisibility(View.VISIBLE);

            ViewCompat.animate(popupText).setStartDelay(400).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();
            ViewCompat.animate(btnAccept).setStartDelay(800).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();

        } else {
            if (option.optionType == MediaType.TEXT) {
                popup_result_text = popupWindow.findViewById(R.id.popup_answer_text);
                popup_result_text.setText(resultText);
                popup_result_text.setVisibility(View.VISIBLE);

                ViewCompat.animate(popup_result_text).setStartDelay(600).alpha(1).setDuration(400).setInterpolator(new DecelerateInterpolator(1.2f)).start();

            } else if (option.optionType == MediaType.IMAGE) {
                popup_result_image = popupWindow.findViewById(R.id.popup_answer_picture);
                popup_result_image.setBackgroundResource(resultImage);
                popup_result_image.setVisibility(View.VISIBLE);

                ViewCompat.animate(popup_result_image).setStartDelay(600).alpha(1).setDuration(400).setInterpolator(new DecelerateInterpolator(1.2f)).start();
            }

            // Checks if the current activity is last one
            if (current > 1) {
                btnAccept.setText(R.string.game_btn_next);
            } else btnAccept.setText(R.string.game_btn_finished);

            btnAccept.setVisibility(View.VISIBLE);
            ViewCompat.animate(popupText).setStartDelay(500).alpha(1).setDuration(200).setInterpolator(new DecelerateInterpolator(1.2f)).start();
            ViewCompat.animate(btnAccept).setStartDelay(900).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();
        }

        //close the popup to repeat the question or show the next question
        btnAccept.setOnClickListener(v -> {
            if (showAnswer < 2) {
                popupWindow.dismiss();
            } else {
                quizIDs.add(gameContent.getId());
                if (current > 1) {
                    showNextQuestion();
                    current--;
                } else {
                    popupWindow.dismiss();
                    onQuizSuccess(quizIDs);
                }
            }
        });

        popupWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.show();

        Window window = popupWindow.getWindow();
        window.setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
    }
}