package de.lmu.treeapp.activities.minigames.picturePuzzle;

import android.os.Bundle;


import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;

public class GameActivity_PicturePuzzle extends GameActivity_Base implements PopupInterface, Runnable{
    DragDropGrid grid;
    int dimension = 3;
    Popup popup;
    int img = R.drawable.sb_bluete_foto_ahorn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game__picture_puzzle);
        popup = new Popup(this);
        popup.setWinTitle("Wunderbar!");
        grid = (DragDropGrid) findViewById(R.id.grid);
        grid.setImage(img, dimension);
        grid.setOnCompleteCallback(() -> {
            popup.showWithButtonText(PopupType.POSITIVE, "Fertig", "Du hast das Puzzle gelöst.");
            grid.postDelayed(GameActivity_PicturePuzzle.this, 800);
        });
    }

    @Override
    public void run() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__picture_puzzle;
    }

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (type == PopupType.POSITIVE) {
            onSuccess();
        }
    }
}
