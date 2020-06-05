package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;


public class PopUpClass extends GameActivity_ChooseAnswer{//AppCompatActivity {


    public Button forwardButton;
    public String popUpText;

    private RecyclerView popupRecyclerView;
    private RecyclerView.Adapter popupAdapter;
    private List<PopUpClass> PopUps;

    public PopUpClass (String text, Button button){
        this.forwardButton = button;
        this.popUpText = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.popup_test);
        super.onCreate(savedInstanceState);
        setupPopupRecyclerView();
    }

    private void setupPopupRecyclerView(){
        popupRecyclerView = findViewById(R.id.popUp_recylcerView);
        popupRecyclerView.setHasFixedSize(true);
        popupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        PopUps = new ArrayList<>();
        popupAdapter = new PopUp_RecyclerViewAdapter(PopUps, this, (PopUp_RecyclerViewAdapter.ButtonClickInterface) this);
        popupRecyclerView.setAdapter(popupAdapter);
    }

   public String getPopUpText(){
        return popUpText;
    }
   public Button getForwardButton(){
        return forwardButton;
   }
}
