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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;

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
    protected static int columnNumber;//ChooseAnswer_Options_RecyclerViewAdapter.columnNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_game__choose_answer);
        super.onCreate(savedInstanceState);
        setupOptionRecyclerView();
        popupWindow = new Dialog(this);
    }

    private void setupOptionRecyclerView(){
        optionsRecyclerView = findViewById(R.id.auto_fit_recycler_view); //(R.id.game_chooseAnswer_recyclerView);
        optionsRecyclerView.setHasFixedSize(true);
        //int columns = 2;
        //if(ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder.button)
        //RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext());
        //optionsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        /*((GridLayoutManager) recyclerViewLayoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (2 - position % 2);
            }
        });*/

        RecyclerView.Adapter recyclerViewAdapter = new ChooseAnswer_Options_RecyclerViewAdapter(this,(Minigame_ChooseAnswer) gameContent, getApplicationContext(), parentTree, parentCategory);
        optionsRecyclerView.setAdapter(recyclerViewAdapter);

        /*recyclerViewAdapter.getItemViewType(int position){
            switch (recyclerViewAdapter.getItemViewType(position)){
                case recyclerViewAdapter.Button:
                    return 1;
                case recyclerViewAdapter.TYPE_ITEM:
                    return 2;
                default:
                    return -1;
            }
        }*/
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

    //Create a popup window after an option was clicked: Gives feedback to the user's answer
    private void showPositivePopup() {
        popupWindow.setContentView(R.layout.popup_quiz_positive);
        btnAccept = (Button) popupWindow.findViewById(R.id.forward_next_game_positive);
        popupTitle = (TextView) popupWindow.findViewById(R.id.popup_positive_title);

        if (current<4) {
            btnAccept.setText("Weiter");
        } else {
            btnAccept.setText("Fertig!");
        }

        ViewCompat.animate(btnAccept).setStartDelay(200).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();

        //Close the popup or finish the game and go back to the overview
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(current<4){
                    ChooseAnswer_Options_RecyclerViewAdapter.current++;
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
        btnAccept = popupWindow.findViewById(R.id.repeat_game);
        popupTitle = popupWindow.findViewById(R.id.popup_negative_title);
        popupText = popupWindow.findViewById(R.id.popup_negative_text);

        if(showAnswer<2){
            //popupText.setVisibility(View.INVISIBLE);
            popup_result_text = popupWindow.findViewById(R.id.popup_right_answer_text);
            popup_result_text.setText("Probier's doch noch einmal...einen Versuch hast du übrig!");
            popup_result_text.setVisibility(View.VISIBLE);
            btnAccept.setVisibility(View.VISIBLE);

            ViewCompat.animate(popup_result_text).setStartDelay(400).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();
            ViewCompat.animate(btnAccept).setStartDelay(800).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();

        }else{

            if(option.type == AnswerOption.OptionTypes.text) {
                popup_result_text = popupWindow.findViewById(R.id.popup_right_answer_text);
                popup_result_text.setText(resultText);
                popup_result_text.setVisibility(View.VISIBLE);

                ViewCompat.animate(popup_result_text).setStartDelay(600).alpha(1).setDuration(400).setInterpolator(new DecelerateInterpolator(1.2f)).start();

            }else if(option.type == AnswerOption.OptionTypes.image){
                popup_result_image = popupWindow.findViewById(R.id.popup_right_answer_picture);
                popup_result_image.setBackgroundResource(resultImage);
                popup_result_image.setVisibility(View.VISIBLE);

                ViewCompat.animate(popup_result_image).setStartDelay(600).alpha(1).setDuration(400).setInterpolator(new DecelerateInterpolator(1.2f)).start();
            }

            if(current<4){
                btnAccept.setText("Weiter");
            } else {
                btnAccept.setText("Fertig");
            }

            btnAccept.setVisibility(View.VISIBLE);
            ViewCompat.animate(popupText).setStartDelay(500).alpha(1).setDuration(200).setInterpolator(new DecelerateInterpolator(1.2f)).start();
            ViewCompat.animate(btnAccept).setStartDelay(900).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();
        }

        //Close the popup to repeat the question
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(showAnswer<2){
                    popupWindow.dismiss();
                } else {
                    if(current<4){
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




