package de.lmu.treeapp.activities.minigames.picturePuzzle;

import android.os.Bundle;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;

public class GameActivity_PicturePuzzle extends GameActivity_Base {


    protected void onCreate(Bundle savedInstanceState) {
        super.active = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__picture_puzzle;
    }


}
