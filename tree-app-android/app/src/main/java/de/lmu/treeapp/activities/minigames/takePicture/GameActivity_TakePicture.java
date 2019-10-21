package de.lmu.treeapp.activities.minigames.takePicture;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_TakePicture;

public class GameActivity_TakePicture extends GameActivity_Base {

    private Minigame_TakePicture takePictureGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_game__take_picture);
        super.onCreate(savedInstanceState);


        takePictureGame = (Minigame_TakePicture) gameContent;
        Button sendButton = findViewById(R.id.game_takePicture_sendButton);

        sendButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create Link to file for Tree_Profile etc.
                onSuccess();
            }
        });

    }
}
