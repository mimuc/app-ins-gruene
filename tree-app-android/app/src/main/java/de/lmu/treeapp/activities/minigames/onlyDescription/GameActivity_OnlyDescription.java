package de.lmu.treeapp.activities.minigames.onlyDescription;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_OnlyDescription;

public class GameActivity_OnlyDescription extends GameActivity_Base {

    private Minigame_OnlyDescription game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_game__only_description);
        super.onCreate(savedInstanceState);
        game = (Minigame_OnlyDescription) gameContent;

        Button sendButton = findViewById(R.id.game_onlyDescription_sendButton);

        sendButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSuccess();
            }
        });

    }
}
