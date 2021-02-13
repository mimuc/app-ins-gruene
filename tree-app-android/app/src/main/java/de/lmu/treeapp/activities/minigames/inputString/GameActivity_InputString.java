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
import de.lmu.treeapp.contentData.database.AppDatabase;
import de.lmu.treeapp.contentData.database.entities.app.GameStateInputString;
import de.lmu.treeapp.contentData.database.entities.content.GameInputStringRelations;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GameActivity_InputString extends GameActivity_Base implements PopupInterface {

    private TextInputEditText inputField;
    protected Popup popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameInputStringRelations inputStringGame = (GameInputStringRelations) gameContent;
        inputField = findViewById(R.id.game_inputString_inputField);
        Button sendButton = findViewById(R.id.game_inputString_sendButton);

        popup = new Popup(this, treeId);
        popup.setButtonSecondary(true);
        popup.setButtonAcceptText(getString(R.string.popup_btn_finished));
        popup.setButtonSecondaryText(getString(R.string.popup_btn_wiki));

        ImageView contentBox = findViewById(R.id.background_image);
        int backgroundImage = getResources().getIdentifier(inputStringGame.getImageResource(),
                "drawable", getPackageName());
        Glide.with(this).load(backgroundImage).into(contentBox);

        sendButton.setOnClickListener(view -> {
            if (checkAnswer(Objects.requireNonNull(inputField.getText()).toString())) {
                popup.show(PopupType.NEUTRAL);
            } else {
                onFail();
            }
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
    protected Completable saveGameState() {
        GameStateInputString gameStateInputString = new GameStateInputString(gameId, treeId,
                parentCategory, Objects.requireNonNull(inputField.getText()).toString());
        return AppDatabase.getInstance(getApplicationContext()).gameStateInputStringDao()
                .insertOne(gameStateInputString)
                .subscribeOn(Schedulers.io()).flatMapCompletable(s -> Completable.fromAction(() -> {
                    gameStateInputString.id = s.intValue(); // Update id afterwards
                    parentTree.appData.treeInputStrings.add(gameStateInputString);
                }));
    }

}
