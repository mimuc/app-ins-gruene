package de.lmu.treeapp.activities.minigames.slidePuzzle;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ViewFlipper;


import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameSlidePuzzleRelations;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;

public class GameActivity_SlidePuzzle extends GameActivity_Base implements PopupInterface, Runnable{
    DragDropGrid grid;
    private GameSlidePuzzleRelations game;
    int dimension = 3;
    Popup popup;
    int img = R.drawable.sb_bluete_foto_ahorn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = (GameSlidePuzzleRelations) gameContent;

        ViewFlipper viewFlipper = findViewById(R.id.viewFlipperSlidePuzzle);
        viewFlipper.setInAnimation(this, R.anim.fragment_fade_enter);
        viewFlipper.setOutAnimation(this, R.anim.fragment_fade_exit);

        ImageButton btnMale = findViewById(R.id.btn_male);
        ImageButton btnFemale = findViewById(R.id.btn_female);

        btnMale.setOnClickListener(v -> {
            img = R.drawable.sb_bluete_foto_buche_m;
            viewFlipper.showNext(); // Switch to next View
            popup = new Popup(this);
            popup.setWinTitle("Wunderbar!");
            grid = (DragDropGrid) findViewById(R.id.grid);
            grid.setImage(img, dimension);
            grid.setOnCompleteCallback(() -> {
                popup.showWithButtonText(PopupType.POSITIVE, "Fertig", "Du hast das Puzzle gelöst.");
                grid.postDelayed(GameActivity_SlidePuzzle.this, 800);
            });
        });
        btnFemale.setOnClickListener(v -> {
            img = R.drawable.sb_bluete_foto_buche_w;
            viewFlipper.showNext(); // Switch to next View
            init();
        });
    }
    public void init(){
        popup = new Popup(this);
        popup.setWinTitle("Wunderbar!");
        grid = (DragDropGrid) findViewById(R.id.grid);
        grid.setImage(img, dimension);
        grid.setOnCompleteCallback(() -> {
            popup.showWithButtonText(PopupType.POSITIVE, "Fertig", "Du hast das Puzzle gelöst.");
            grid.postDelayed(GameActivity_SlidePuzzle.this, 800);
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
