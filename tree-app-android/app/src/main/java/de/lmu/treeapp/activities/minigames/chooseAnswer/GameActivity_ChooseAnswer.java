package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
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

    @Override
    public void optionClicked(AnswerOption option){
        if (option.right){
            onSuccess();
        }
        else {
            onFail();
        }
    }
}
