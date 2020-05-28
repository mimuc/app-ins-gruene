package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.GameSelectionActivity;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;
import de.lmu.treeapp.contentClasses.trees.Tree;
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

    /* @Override
    public void optionClicked(AnswerOption option){
        if (option.right){
            onSuccess();
        }
        else {
            onFail();
        }
    }*/


     @Override
    public void optionClicked(AnswerOption option){
            if (option.right) {
                /*Toast.makeText(getApplicationContext(), "Richtig", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), GameActivity_ChooseAnswer.class);
                intent.putExtra("TreeId", this.treeId);
                intent.putExtra("Category", this.parentCategory);
                intent.putExtra("GameId", getNext());
                startActivity(intent);*/
                showNextGame();
            } else {
                Toast.makeText(getApplicationContext(), "Falsch", Toast.LENGTH_LONG).show();
                PopUp popUpObject = new PopUp();
                popUpObject.showPopupWindow(optionsRecyclerView);
            }
    }

   /* @Override
    public void optionClicked(AnswerOption option) {
        if (option.right) {
            Toast.makeText(getApplicationContext(), "Richtig", Toast.LENGTH_LONG).show();
            //showPopup
            PopUp popUpObject = new PopUp();
            popUpObject.showPopupWindow(optionsRecyclerView);
        } else{
                Toast.makeText(getApplicationContext(), "Falsch", Toast.LENGTH_LONG).show();
                //showPopup
                PopUp popUpObject = new PopUp();
                popUpObject.showPopupWindow(optionsRecyclerView);
            }
        }*/

        public void showNextGame() {
            Intent intent = new Intent(getApplicationContext(), GameActivity_ChooseAnswer.class);
            intent.putExtra("TreeId", this.treeId);
            intent.putExtra("Category", this.parentCategory);
            intent.putExtra("GameId", getNext());
            startActivity(intent);
        }


}
