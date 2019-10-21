package de.lmu.treeapp.activities.minigames.inputString;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.android.material.textfield.TextInputEditText;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_InputStringAnswer;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

public class GameActivity_InputString extends GameActivity_Base {

    private Minigame_InputStringAnswer inputStringGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_game__input_string);
        super.onCreate(savedInstanceState);


        inputStringGame = (Minigame_InputStringAnswer) gameContent;
        final TextInputEditText inputField = findViewById(R.id.game_inputString_inputField);
        Button sendButton = findViewById(R.id.game_inputString_sendButton);

        sendButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputStringGame.CheckAnswer(inputField.getText().toString())){
                    onSuccess();
                }
                else {
                    onFail();
                }
            }
        });

    }

}
