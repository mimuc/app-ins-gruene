package de.lmu.treeapp.activities.minigames.inputString;


import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameInputStringRelations;

public class GameActivity_InputString extends GameActivity_Base {

    private GameInputStringRelations inputStringGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inputStringGame = (GameInputStringRelations) gameContent;
        final TextInputEditText inputField = findViewById(R.id.game_inputString_inputField);
        Button sendButton = findViewById(R.id.game_inputString_sendButton);

        ImageView contentBox = findViewById(R.id.background_image);
        int backgroundImage = getResources().getIdentifier(inputStringGame.getImageResource(), "drawable", getPackageName());
        Glide.with(this).load(backgroundImage).into(contentBox);

        sendButton.setOnClickListener(view -> {
            if (checkAnswer(inputField.getText().toString())) {
                onSuccess();
            } else {
                onFail();
            }
        });
    }

    private boolean checkAnswer(String toString) {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__input_string;
    }

}
