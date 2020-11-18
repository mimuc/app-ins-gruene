package de.lmu.treeapp.activities.minigames.baumory;

import android.os.Build;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Baumory;
import de.lmu.treeapp.contentClasses.minigames.components.BaumoryCard;

public class GameActivity_Baumory extends GameActivity_Base implements Baumory_Cards_RecyclerViewAdapter.OptionClickInterface {

    private Minigame_Baumory game;
    private RecyclerView cardsRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private ViewFlipper viewFlipper;
    private Button btnBack, btnRepeat;

    private Boolean multiplayerMode = false;

    /**
     * The total number of players
     */
    private final int playerCount = 2;

    /**
     * The player whose turn it is
     */
    private int playerTurn = 0;

    private TextView tvMpTitle;
    private final TextView[] tvMpScores = new TextView[playerCount];
    private final int[] mpScores = new int[playerCount];
    private final String[] mpNames = new String[playerCount];

    private BaumoryCard firstCard = null;
    private BaumoryCard secondCard = null;
    private ImageButton firstCardButton = null;
    private ImageButton secondCardButton = null;
    private List<BaumoryCard> baumoryCards;
    private List<Integer> finishedCards;
    private int maxMatches = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        setContentView(R.layout.activity_game__baumory);
        super.onCreate(savedInstanceState);

        // setup View-Elements
        viewFlipper = findViewById(R.id.viewFlipperBaumory);
        ImageButton btnSinglePlayer = findViewById(R.id.btn_singleplayer);
        ImageButton btnMultiplayer = findViewById(R.id.btn_multiplayer);
        btnBack = findViewById(R.id.btn_game_baumory_back);
        btnRepeat = findViewById(R.id.btn_game_baumory_repeat);
        tvMpTitle = findViewById(R.id.tv_multiplayerTitle);
        tvMpScores[0] = findViewById(R.id.tv_score_p1);
        tvMpScores[1] = findViewById(R.id.tv_score_p2);

        // set the animation type to ViewFlipper
        viewFlipper.setInAnimation(this, R.anim.fragment_fade_enter);
        viewFlipper.setOutAnimation(this, R.anim.fragment_fade_exit);

        setupBaumoryGame();

        btnSinglePlayer.setOnClickListener(v -> startGame());

        btnMultiplayer.setOnClickListener(v -> {
            setupMultiplayerView();
            startGame();
        });


        btnBack.setOnClickListener(v -> onSuccess());

        btnRepeat.setOnClickListener(v -> {
            firstCard = null;
            secondCard = null;
            firstCardButton = null;
            secondCardButton = null;
            btnBack.setVisibility(View.INVISIBLE);
            btnRepeat.setVisibility(View.INVISIBLE);

            setupMultiplayerView();
            setupBaumoryGame();
        });
    }

    private void startGame() {
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
        if ((firstCard != null && secondCard != null) || finishedCards.contains(_card.match))
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
            handler.postDelayed(this::SuccessfulMatch, 750);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(this::FailedMatch, 750);
        }

    }


    private void SuccessfulMatch() {
        firstCardButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        secondCardButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        firstCard = null;
        secondCard = null;
        if ((finishedCards.size() >= maxMatches) && !multiplayerMode) onSuccess();

        if (multiplayerMode) {
            mpScores[playerTurn]++;
            tvMpScores[playerTurn].setText(getString(R.string.game_mode_player, mpNames[playerTurn], mpScores[playerTurn]));

            if (finishedCards.size() >= maxMatches) MultiplayerLastCard();
        }
    }

    private void FailedMatch() {
        firstCardButton.setBackgroundResource(R.drawable.dark_grey_gradient);
        secondCardButton.setBackgroundResource(R.drawable.dark_grey_gradient);
        firstCardButton.setImageResource(R.drawable.ic_question_big);
        secondCardButton.setImageResource(R.drawable.ic_question_big);
        firstCard = null;
        secondCard = null;

        if (multiplayerMode) {
            playerTurn = (playerTurn + 1) % playerCount;
            tvMpTitle.setText(getString(R.string.game_mode_multiplayer_next_player, mpNames[playerTurn]));
        }
    }

    private void MultiplayerLastCard() {
        tvMpTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        Integer[] maxIndices;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Get the indices of the players, which had the max score.
            int max = Arrays.stream(mpScores).max().getAsInt();
            maxIndices = IntStream.range(0, playerCount).filter(i -> mpScores[i] == max).boxed().toArray(Integer[]::new);
        } else {
            // Polyfill for SDK below 21
            int max = 0;
            List<Integer> maxIndicesList = new ArrayList<>();
            for (int i = 0; i < playerCount; i++) {
                if (mpScores[i] >= max) {
                    if (mpScores[i] > max) {
                        maxIndicesList.clear();
                        max = mpScores[i];
                    }
                    maxIndicesList.add(i);
                }
            }
            maxIndices = maxIndicesList.toArray(new Integer[0]);
        }
        if (maxIndices.length == 1) {
            tvMpTitle.setText(getString(R.string.game_mode_player_won, mpNames[maxIndices[0]]));
        } else {
            tvMpTitle.setText(R.string.game_mode_draw);
        }
        btnBack.setVisibility(View.VISIBLE);
        btnRepeat.setVisibility(View.VISIBLE);

    }

    private void setupMultiplayerView() {
        multiplayerMode = true;
        playerTurn = 0;
        for (int i = 0; i < playerCount; i++) {
            mpNames[i] = String.valueOf((char) (i + 'A')); // Names the players according to their numbers 0: 'A', 1: 'B' etc.
            mpScores[i] = 0; // (Re)set scores.
            tvMpScores[i].setText(getString(R.string.game_mode_player, mpNames[i], mpScores[i]));
            tvMpScores[i].setVisibility(View.VISIBLE);
        }
        tvMpTitle.setText(getString(R.string.game_mode_multiplayer_next_player, mpNames[playerTurn]));
        tvMpTitle.setTextColor(getResources().getColor(R.color.asphalt));
        tvMpTitle.setVisibility(View.VISIBLE);
    }

    private void setupBaumoryGame() {
        finishedCards = new ArrayList<>();
        game = (Minigame_Baumory) gameContent;
        baumoryCards = game.cards;
        maxMatches = baumoryCards.size() / 2;
        Collections.shuffle(baumoryCards);
        setupCardsRecyclerView();
    }
}
