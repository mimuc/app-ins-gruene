package de.lmu.treeapp.activities.minigames.baumory;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Baumory;
import de.lmu.treeapp.contentClasses.minigames.components.BaumoryCard;

public class GameActivity_Baumory extends GameActivity_Base implements Baumory_Cards_RecyclerViewAdapter.OptionClickInterface{

    private Minigame_Baumory game;
    private RecyclerView cardsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_game__baumory);
        super.onCreate(savedInstanceState);
        game = (Minigame_Baumory) gameContent;
        setupCardsRecyclerView();
    }

    private void setupCardsRecyclerView(){
        cardsRecyclerView = findViewById(R.id.game_baumory_recyclerView);
        cardsRecyclerView.setHasFixedSize(true);
        int columns = 4;
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), columns);
        cardsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        RecyclerView.Adapter recyclerViewAdapter = new Baumory_Cards_RecyclerViewAdapter(this, game, getApplicationContext(), parentTree, parentCategory);
        cardsRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void optionClicked(BaumoryCard option){
        if (option.match == 1){
            onSuccess();
        }
        else {
            onFail();
        }
    }
}
