package de.lmu.treeapp.activities.minigames.baumory;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Baumory;
import de.lmu.treeapp.contentClasses.minigames.components.BaumoryCard;

public class GameActivity_Baumory extends GameActivity_Base implements Baumory_Cards_RecyclerViewAdapter.OptionClickInterface{

    private Minigame_Baumory game;
    private RecyclerView cardsRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;

    private BaumoryCard firstCard = null;
    private BaumoryCard secondCard = null;
    private ImageButton firstCardButton = null;
    private ImageButton secondCardButton = null;
    private List<BaumoryCard> baumoryCards;
    private List<Integer> finishedCards;
    private int maxMatches = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_game__baumory);
        super.onCreate(savedInstanceState);
        finishedCards = new ArrayList<>();
        game = (Minigame_Baumory) gameContent;
        baumoryCards = game.cards;
        maxMatches = baumoryCards.size()/2;
        Collections.shuffle(baumoryCards);
        setupCardsRecyclerView();
    }

    private void setupCardsRecyclerView(){
        cardsRecyclerView = findViewById(R.id.game_baumory_recyclerView);
        cardsRecyclerView.setHasFixedSize(false);
        int columns = 4;
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), columns);
        cardsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewAdapter = new Baumory_Cards_RecyclerViewAdapter(this, baumoryCards, getApplicationContext(), parentTree, parentCategory);
        cardsRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void optionClicked(BaumoryCard _card, Baumory_Cards_RecyclerViewAdapter.ViewHolder _viewHolder){
        if ((firstCard != null && secondCard != null) || finishedCards.contains(_card.match)) return;
        int imageId = getResources().getIdentifier(_card.content, "drawable", getPackageName());
        _viewHolder.button.setImageResource(imageId);
        if (firstCard == null){
            firstCardButton = _viewHolder.button;
            firstCard = _card;
        }
        else if (secondCard == null && _card != firstCard){
            secondCardButton = _viewHolder.button;
            secondCard = _card;
        }
        Evaluate();
    }


    private void Evaluate(){
        if (firstCard == null || secondCard == null) return;

        if (firstCard.match == secondCard.match){
            finishedCards.add(firstCard.match);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SuccessfulMatch();
                }
            }, 1000);
        }
        else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    FailedMatch();
                }
            }, 1000);
        }

    }


    private void SuccessfulMatch(){
        firstCardButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        secondCardButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        firstCard = null;
        secondCard = null;
        if (finishedCards.size() >= maxMatches) onSuccess();
    }

    private void FailedMatch(){
        firstCardButton.setBackgroundResource(R.drawable.dark_grey_gradient);
        secondCardButton.setBackgroundResource(R.drawable.dark_grey_gradient);
        firstCardButton.setImageResource(R.drawable.ic_question_big);
        secondCardButton.setImageResource(R.drawable.ic_question_big);
        firstCard = null;
        secondCard = null;
    }
}
