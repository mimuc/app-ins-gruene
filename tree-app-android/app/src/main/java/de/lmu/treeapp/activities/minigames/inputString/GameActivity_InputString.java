package de.lmu.treeapp.activities.minigames.inputString;


import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.daos.app.GameStateInputStringDao;
import de.lmu.treeapp.contentData.database.entities.app.GameStateInputString;
import de.lmu.treeapp.contentData.database.entities.content.GameInputStringRelations;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;

public class GameActivity_InputString extends GameActivity_Base implements PopupInterface {

    private TextInputEditText inputField;
    protected Popup popup;
    protected GameStateInputString gameStateInputString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        popup = new Popup(this, treeId);
        popup.setButtonSecondary(true);
        popup.setButtonAcceptText(getString(R.string.popup_btn_finished));
        popup.setButtonSecondaryText(getString(R.string.popup_btn_wiki));

        getGameState().observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            GameInputStringRelations inputStringGame = (GameInputStringRelations) gameContent;
            inputField = findViewById(R.id.game_inputString_inputField);
            Button sendButton = findViewById(R.id.game_inputString_sendButton);

            if (gameStateInputString != null) {
                inputField.setText(gameStateInputString.inputString);
            }
            ImageView contentBox = findViewById(R.id.background_image);
            int backgroundImage = getResources().getIdentifier(inputStringGame.getImageResource(), "drawable", getPackageName());
            Glide.with(getApplicationContext()).load(backgroundImage).into(contentBox);

            sendButton.setOnClickListener(view -> {
                if (checkAnswer(Objects.requireNonNull(inputField.getText()).toString())) {
                    popup.show(PopupType.NEUTRAL);
                } else {
                    onFail();
                }
            });
        });
    }

    private boolean checkAnswer(String toString) {
        return true;
    }

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (action == PopupAction.ACCEPT) {
            saveGameState().subscribe();
            onSuccess();
        } else {
            // Set success without going back to overview
            DataManager.getInstance(getApplicationContext()).setGameCompleted(parentCategory,
                    gameContent.getId(), parentTree);
            saveGameState().subscribe();
            showTreeProfile();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__input_string;
    }

    @Override
    protected Completable getGameState() {
        return DataManager.getInstance(getApplicationContext()).getOrCreateGameStateSingle(treeId, gameId, parentCategory, GameStateInputStringDao.class).flatMapCompletable(s -> {
            gameStateInputString = s;
            parentTree.appData.treeInputStrings.add(gameStateInputString);
            return Completable.complete();
        });
    }

    @Override
    protected Completable saveGameState() {
        gameStateInputString.inputString = Objects.requireNonNull(inputField.getText()).toString();
        return DataManager.getInstance(getApplicationContext()).updateGameState(gameStateInputString, GameStateInputStringDao.class);
    }

}
