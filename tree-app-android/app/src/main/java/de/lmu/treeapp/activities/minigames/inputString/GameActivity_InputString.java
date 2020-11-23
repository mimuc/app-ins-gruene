package de.lmu.treeapp.activities.minigames.inputString;


import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_InputStringAnswer;

public class GameActivity_InputString extends GameActivity_Base {

    private Minigame_InputStringAnswer inputStringGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_game__input_string);
        super.onCreate(savedInstanceState);


        inputStringGame = (Minigame_InputStringAnswer) gameContent;
        final TextInputEditText inputField = findViewById(R.id.game_inputString_inputField);
        Button sendButton = findViewById(R.id.game_inputString_sendButton);

        sendButton.setOnClickListener(view -> {
            if (inputStringGame.CheckAnswer(inputField.getText().toString())) {
                onSuccess();
            } else {
                onFail();
            }
        });

    }

}
