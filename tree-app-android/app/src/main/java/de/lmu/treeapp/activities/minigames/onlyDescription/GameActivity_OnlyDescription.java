package de.lmu.treeapp.activities.minigames.onlyDescription;

import android.os.Bundle;
import android.widget.Button;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameOnlyDescriptionRelations;

public class GameActivity_OnlyDescription extends GameActivity_Base {

    private GameOnlyDescriptionRelations game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = (GameOnlyDescriptionRelations) gameContent;

        Button sendButton = findViewById(R.id.game_onlyDescription_sendButton);

        sendButton.setOnClickListener(view -> onSuccess());

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__only_description;
    }
}
