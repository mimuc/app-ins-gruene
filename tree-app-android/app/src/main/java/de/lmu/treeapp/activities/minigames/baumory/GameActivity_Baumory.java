package de.lmu.treeapp.activities.minigames.baumory;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Baumory;
import de.lmu.treeapp.contentClasses.minigames.components.BaumoryCard;

//TODO: Redesign Frontend
//TODO: Review Multiplayer-Design
//TODO: Review Multiplayer-Wording
//TODO: Maybe add a popup after Multiplayer-Game Ends to Inform about the winner

public class GameActivity_Baumory extends GameActivity_Base implements Baumory_Cards_RecyclerViewAdapter.OptionClickInterface {

    private Minigame_Baumory game;
    private RecyclerView cardsRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private ViewFlipper viewFlipper;
    private ImageButton btnSinglePlayer, btnMultiplayer;
    private Button btnBack;
    private TextView tvMpTitle, tvScoreP1, tvScoreP2;

    private Boolean multiplayerMode = false;
    private Boolean p2Turn = false;
    private Boolean p1Turn = true;
    private int[] scores = new int []{0,0};
    private Boolean matchFinished = false;

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
        //setup View-Elements
        viewFlipper = findViewById(R.id.viewFlipperBaumory); // get the reference of ViewFlipper
        btnSinglePlayer = findViewById(R.id.btn_singleplayer); // get the reference of button
        btnMultiplayer = findViewById(R.id.btn_multiplayer); // get the reference of button
        btnBack = findViewById(R.id.btn_game_baumory_back); // get the reference of button
        tvMpTitle = findViewById(R.id.tv_multiplayerTitle); // get the reference of textview
        tvScoreP1 = findViewById(R.id.tv_score_p1); // get the reference of textview
        tvScoreP2 = findViewById(R.id.tv_score_p2); // get the reference of textview

        // set the animation type to ViewFlipper
        viewFlipper.setInAnimation(this, R.anim.fragment_fade_enter);
        viewFlipper.setOutAnimation(this, R.anim.fragment_fade_exit);

        finishedCards = new ArrayList<>();
        game = (Minigame_Baumory) gameContent;
        baumoryCards = game.cards;
        maxMatches = baumoryCards.size() / 2;
        Collections.shuffle(baumoryCards);
        setupCardsRecyclerView();

        btnSinglePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        btnMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiplayerMode = true;
                p1Turn = true;
                tvMpTitle.setText(getString(R.string.game_mode_multiplayer_next_player, "1"));
                tvScoreP1.setText(getString(R.string.game_mode_player, 1, scores[0]));
                tvScoreP2.setText(getString(R.string.game_mode_player, 2, scores[1]));
                tvMpTitle.setVisibility(View.VISIBLE);
                tvScoreP1.setVisibility(View.VISIBLE);
                tvScoreP2.setVisibility(View.VISIBLE);
                startGame();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSuccess();
            }
        });
    }

    private void startGame(){
            viewFlipper.showNext(); // Switch to next View
    }

    private void setupCardsRecyclerView() {
        cardsRecyclerView = findViewById(R.id.game_baumory_recyclerView);
        cardsRecyclerView.setHasFixedSize(false);
        int columns = 4;
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), columns);
        cardsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewAdapter = new Baumory_Cards_RecyclerViewAdapter(this, baumoryCards, getApplicationContext(), parentTree, parentCategory);
        cardsRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void optionClicked(BaumoryCard _card, Baumory_Cards_RecyclerViewAdapter.ViewHolder _viewHolder) {
        if (((firstCard != null && secondCard != null) || finishedCards.contains(_card.match)) && !multiplayerMode)
            return;

        int imageId = getResources().getIdentifier(_card.content, "drawable", getPackageName());
        _viewHolder.button.setImageResource(imageId);
        if (firstCard == null) {
            firstCardButton = _viewHolder.button;
            firstCard = _card;
        } else if (secondCard == null && _card != firstCard) {
            secondCardButton = _viewHolder.button;
            secondCard = _card;
        }
        Evaluate();
    }


    private void Evaluate() {
        if (firstCard == null || secondCard == null) return;

        if (firstCard.match == secondCard.match) {
            finishedCards.add(firstCard.match);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SuccessfulMatch();
                }
            }, 1000);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    FailedMatch();
                }
            }, 1000);
        }


    }


    private void SuccessfulMatch() {
        firstCardButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        secondCardButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        firstCard = null;
        secondCard = null;
        if (finishedCards.size() >= maxMatches) MultiplayerLastCard();

        if(multiplayerMode){
            if(p1Turn){
                scores[0] ++;
                tvScoreP1.setText(getString(R.string.game_mode_player, 1, scores[0]));
            }else{
                scores[1] ++;
                tvScoreP2.setText(getString(R.string.game_mode_player, 2, scores[1]));
            }

            if(matchFinished){
                MultiplayerLastCard();
            }
        }
    }

    private void FailedMatch() {
        firstCardButton.setBackgroundResource(R.drawable.dark_grey_gradient);
        secondCardButton.setBackgroundResource(R.drawable.dark_grey_gradient);
        firstCardButton.setImageResource(R.drawable.ic_question_big);
        secondCardButton.setImageResource(R.drawable.ic_question_big);
        firstCard = null;
        secondCard = null;

        if(multiplayerMode){
                if (p1Turn) {
                    tvMpTitle.setText(getString(R.string.game_mode_multiplayer_next_player, "2"));
                } else {
                    tvMpTitle.setText(getString(R.string.game_mode_multiplayer_next_player, "1"));
                }
                p1Turn = !p1Turn;
                p2Turn = !p2Turn;
        }

    }

    private void MultiplayerLastCard(){
        tvMpTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        if(scores[0]>scores[1]){
            tvMpTitle.setText(getString(R.string.game_mode_player_won, "1"));
        } else if(scores[1]>scores[0]){
            tvMpTitle.setText(getString(R.string.game_mode_player_won, "2"));
        } else {
            tvMpTitle.setText(R.string.game_mode_draw);
        }
        btnBack.setVisibility(View.VISIBLE);

    }
}
