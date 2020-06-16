package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

public class PopUp extends GameActivity_ChooseAnswer{
    private final Context context;


    protected PopUp(Context context) {
            this.context = context;
        }

     /*public PopUp(Context context, String whichPopUp){
        this. context=context;
        this.whichPopUp = whichPopUp;
    }*/

        //PopupWindow display method
        public void showPopupWindow(View view) {

            //Create a View object through inflater
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
            final View popupView = inflater.inflate(R.layout.activity_popup, null);

            //Make Inactive Items Outside Of PopupWindow
            boolean focusable = false;

            //Create a window with our parameters
            //final PopupWindow popupWindow = new PopupWindow(popupView);
            final PopupWindow popupWindow = new PopupWindow(popupView, view.getWidth(), view.getHeight(), focusable);

            //Set the location of the window on the screen
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            //Initialize the elements of our window, install the handler
            TextView popUpText = popupView.findViewById(R.id.text_result);
            Button forwardButton = popupView.findViewById(R.id.forward_next_game);

            forwardButton.setOnClickListener(new View.OnClickListener() {
                //Go to the next view
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Weiter wurde geklickt", Toast.LENGTH_SHORT).show();
                    Intent nextQuestion = new Intent(context, GameActivity_ChooseAnswer.class);
                    nextQuestion.putExtra("TreeId", treeId);
                    nextQuestion.putExtra("Category", parentCategory);
                    nextQuestion.putExtra("GameId", getNextQuizID());
                    startActivity(nextQuestion);
                    popupWindow.dismiss();

                }
            });
        }
}