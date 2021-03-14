package de.lmu.treeapp.activities.minigames.baumory;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.AppDatabase;
import de.lmu.treeapp.contentData.database.entities.content.GameBaumoryCard;
import de.lmu.treeapp.contentData.database.entities.content.GameBaumoryRelations;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;
import de.lmu.treeapp.utils.glide.BackgroundTarget;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GameActivity_Baumory extends GameActivity_Base implements Baumory_Cards_RecyclerViewAdapter.OptionClickInterface, PopupInterface {

    private Popup popup;
    private Boolean isTimerRunning = false;
    private String time;

    private Boolean multiPlayerMode = false;
    private Boolean difficultyHard = false;
    protected Dialog popupWindow;

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

    private GameBaumoryCard firstCard = null;
    private GameBaumoryCard secondCard = null;
    private ImageButton firstCardButton = null;
    private ImageButton secondCardButton = null;
    private List<GameBaumoryCard> baumoryCards;
    private List<Integer> finishedCards;
    private int maxMatches = 0;
    private BaumorySelectionFragment baumorySelectionFragment;
    private Boolean selectionVisible = false;
    private TextView timeView;
    private EditText editName;
    private String name;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // show selection of single/multiPlayer and difficulty
        baumorySelectionFragment = new BaumorySelectionFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.selection_fragment_container, baumorySelectionFragment).commit();
        selectionVisible = true;

        popupWindow = new Dialog(this);
        popupWindow.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();
                    baumorySelectionFragment = new BaumorySelectionFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.selection_fragment_container, baumorySelectionFragment).commit();
                    selectionVisible = true;
                }
                return true;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__baumory;
    }

    public void startGame(Boolean multiPlayer, Boolean hard) {
        if (selectionVisible) {
            getSupportFragmentManager().beginTransaction()
                    .detach(baumorySelectionFragment).commit();
            selectionVisible = false;
        }

        multiPlayerMode = multiPlayer;
        difficultyHard = hard;
        tvMpTitle = findViewById(R.id.tv_multiplayerTitle);
        tvMpScores[0] = findViewById(R.id.tv_score_p1);
        tvMpScores[1] = findViewById(R.id.tv_score_p2);
        timeView = findViewById(R.id.time_view);
        if (!multiPlayerMode) {
            timeView.setVisibility(View.VISIBLE);
            startTimer();
        } else {
            timeView.setVisibility(View.INVISIBLE);
            showNamePopup();
        }
        setupBaumoryGame();
    }

    private void showNamePopup() {
        popupWindow.setContentView(R.layout.popup_second_player_name);
        editName = popupWindow.findViewById(R.id.edit_name);
        Button next = popupWindow.findViewById(R.id.next_button);
        next.setOnClickListener(v ->
        {
            String input = editName.getText().toString();
            if (input.isEmpty()) {
                name = getString(R.string.game_second_player_default);
            } else {
                name = editName.getText().toString();
            }
            popupWindow.dismiss();
            setupMultiplayerView();
        });
        popupWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.show();
        Window window = popupWindow.getWindow();
        window.setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
    }

    private void setupCardsRecyclerView() {
        RecyclerView cardsRecyclerView = findViewById(R.id.game_baumory_recyclerView);
        cardsRecyclerView.setHasFixedSize(false);
        int columns = 4;
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), columns);
        cardsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        RecyclerView.Adapter recyclerViewAdapter = new Baumory_Cards_RecyclerViewAdapter(this, baumoryCards, getApplicationContext(), parentTree, parentCategory);
        cardsRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void optionClicked(GameBaumoryCard _card, Baumory_Cards_RecyclerViewAdapter.ViewHolder _viewHolder) {
        if ((firstCard != null && secondCard != null) || finishedCards.contains(_card.match))
            return;

        int imageId = getResources().getIdentifier(_card.imageResource, "drawable", getPackageName());
        Glide.with(this).load(imageId).into(_viewHolder.button);
        if (firstCard == null) {
            firstCardButton = _viewHolder.button;
            Glide.with(this).load(R.drawable.white_card_background).into(new BackgroundTarget(firstCardButton));
            firstCard = _card;
        } else if (secondCard == null && _card != firstCard) {
            secondCardButton = _viewHolder.button;
            Glide.with(this).load(R.drawable.white_card_background).into(new BackgroundTarget(secondCardButton));
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

    private void startTimer() {
        // displaying stopwatch
        isTimerRunning = true;
        {
            final Handler handler_ = new Handler(getMainLooper());
            handler_.post(new Runnable() {
                int seconds = 0;

                @Override
                public void run() {
                    int hours = seconds / 3600;
                    int minutes = ((seconds % 3600) / 60) + hours * 60;
                    int secs = seconds % 60;
                    time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
                    timeView.setText(time);
                    if (isTimerRunning) {
                        seconds++;
                    } else {
                        return;
                    }
                    handler_.postDelayed(this, 1000);
                }
            });
        }
    }


    private void SuccessfulMatch() {
        firstCard = null;
        secondCard = null;
        if (multiPlayerMode) {
            if (playerTurn == 0) {
                Glide.with(this).load(R.drawable.light_green_crad_background).into(new BackgroundTarget(firstCardButton));
                Glide.with(this).load(R.drawable.light_green_crad_background).into(new BackgroundTarget(secondCardButton));
            } else {
                Glide.with(this).load(R.drawable.light_red_card_background).into(new BackgroundTarget(firstCardButton));
                Glide.with(this).load(R.drawable.light_red_card_background).into(new BackgroundTarget(secondCardButton));
            }
            mpScores[playerTurn]++;
            tvMpScores[playerTurn].setText(getString(R.string.game_mode_player, mpNames[playerTurn], mpScores[playerTurn]));
            if (finishedCards.size() >= maxMatches) MultiplayerLastCard();
        } else {
            Glide.with(this).load(R.drawable.forest_border_card).into(new BackgroundTarget(firstCardButton));
            Glide.with(this).load(R.drawable.forest_border_card).into(new BackgroundTarget(secondCardButton));
            if ((finishedCards.size() >= maxMatches) && !multiPlayerMode) {
                isTimerRunning = false;
                popup.setButtonSecondary(true);
                popup.setButtonSecondaryText(getString(R.string.button_repeat));
                popup.showWithButtonText(PopupType.POSITIVE_ANIMATION, getString(R.string.button_done), getString(R.string.popup_puzzle_won_text, time));
            }
        }
    }

    private void FailedMatch() {
        Glide.with(this).load(R.drawable.forst_card_background).into(new BackgroundTarget(firstCardButton));
        Glide.with(this).load(R.drawable.forst_card_background).into(new BackgroundTarget(secondCardButton));
        Glide.with(this).load(R.drawable.ic_singleplayer_squirrel).into(firstCardButton);
        Glide.with(this).load(R.drawable.ic_singleplayer_squirrel).into(secondCardButton);
        firstCard = null;
        secondCard = null;
        if (multiPlayerMode) {
            playerTurn = (playerTurn + 1) % playerCount;
            tvMpTitle.setText(getString(R.string.game_mode_multiplayer_next_player, mpNames[playerTurn]));
        }
    }

    private void MultiplayerLastCard() {
        tvMpTitle.setTextColor(getResources().getColor(R.color.forest));
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
            popup.setButtonSecondary(true);
            popup.setButtonSecondaryText(getString(R.string.button_repeat));
            popup.showWithButtonText(PopupType.POSITIVE_ANIMATION, getString(R.string.button_done), getString(R.string.game_mode_player_won, mpNames[maxIndices[0]]));
        } else {
            popup.setButtonSecondary(true);
            popup.setButtonSecondaryText(getString(R.string.button_repeat));
            popup.showWithButtonText(PopupType.POSITIVE_ANIMATION, getString(R.string.button_done), getString(R.string.game_mode_draw));
        }
    }

    private void setupMultiplayerView() {
        multiPlayerMode = true;
        playerTurn = 0;
        AppDatabase.getInstance(this).userProfileDao().getFirst().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
            if (s != null && s.name != null) {
                mpNames[0] = s.name;
            } else {
                mpNames[0] = getString(R.string.game_first_player_default);
            }
            mpNames[1] = name;
            for (int i = 0; i < playerCount; i++) {
                // Names the players according to their numbers 0: 'A', 1: 'B' etc.
                mpScores[i] = 0; // (Re)set scores.
                tvMpScores[i].setText(getString(R.string.game_mode_player, mpNames[i], mpScores[i]));
                tvMpScores[i].setVisibility(View.VISIBLE);
            }
            tvMpTitle.setText(getString(R.string.game_mode_multiplayer_next_player, mpNames[playerTurn]));
            tvMpTitle.setTextColor(getResources().getColor(R.color.asphalt));
            tvMpTitle.setVisibility(View.VISIBLE);
        });
    }

    private void setupBaumoryGame() {
        finishedCards = new ArrayList<>();
        GameBaumoryRelations game = (GameBaumoryRelations) gameContent;
        List<GameBaumoryCard> allCards;
        allCards = game.getCards();
        baumoryCards = new ArrayList<>();
        if (!difficultyHard) {
            for (GameBaumoryCard card : allCards) {
                if (card.difficulty == 0) {
                    baumoryCards.add(card);
                }
            }
        } else {
            baumoryCards = allCards;
        }
        maxMatches = baumoryCards.size() / 2;
        Collections.shuffle(baumoryCards);
        setupCardsRecyclerView();
        popup = new Popup(this);
        popup.setWinTitle(getString(R.string.popup_win_title_done));
    }


    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (type == PopupType.POSITIVE_ANIMATION) {
            if (action == PopupAction.ACCEPT) {
                onSuccess();
            } else {
                DataManager.getInstance(getApplicationContext()).setGameCompleted(parentCategory, gameContent.getId(), parentTree).subscribe();
                startGame(multiPlayerMode, difficultyHard);
            }
        }
    }
}
