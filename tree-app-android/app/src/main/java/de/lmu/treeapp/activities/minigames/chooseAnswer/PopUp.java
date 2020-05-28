package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import de.lmu.treeapp.R;

public class PopUp extends GameActivity_ChooseAnswer {

    //PopupWindow display method
    public void showPopupWindow(final View view) {

        //Create a View object through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.activity_popup, null);

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        //final PopupWindow popupWindow = new PopupWindow(popupView);
        final PopupWindow popupWindow = new PopupWindow(popupView, view.getWidth(), view.getHeight() ,focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        TextView result = popupView.findViewById(R.id.Text_Result);
        Button buttonForward = popupView.findViewById(R.id.forward_next_game);

        buttonForward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Go to the next view
                Toast.makeText(view.getContext(), "Wow, popup action button", Toast.LENGTH_SHORT).show();
                showNextGame();
                popupWindow.dismiss();
            }
        });

        //Handler for clicking on the inactive zone of the window
        /*popupView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });*/
    }
}