package de.lmu.treeapp.activities.minigames.picturePuzzle;

import android.os.Bundle;
import android.content.Context;
import android.view.ViewTreeObserver;

import java.util.Random;


import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;

public class GameActivity_PicturePuzzle extends GameActivity_Base implements PopupInterface {

    DragDropListener dragDropGrid;

    int columns = 3;
    int FIELDS = columns * columns;

    int pieceWidth, pieceHeight;
    int img = R.drawable.ic_hasel_frucht;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        scramble();
        setDimensions();
    }

    private void init() {
        dragDropGrid = (DragDropListener) findViewById(R.id.grid);
        dragDropGrid.setNumColumns(columns);
        dragDropGrid.setValues(img, columns);

        tileList = new String[FIELDS];
        for (int i = 0; i < FIELDS; i++) {
            tileList[i] = String.valueOf(i);
        }
    }

    private void scramble() {
        int index;
        String temp;
        Random random = new Random();

        for (int i = tileList.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = tileList[index];
            tileList[index] = tileList[i];
            tileList[i] = temp;
        }
    }

    private void setDimensions() {
        ViewTreeObserver vto = dragDropGrid.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dragDropGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth = dragDropGrid.getMeasuredWidth();
                int displayHeight = dragDropGrid.getMeasuredHeight();

                int statusbarHeight = getStatusBarHeight(getApplicationContext());
                int requiredHeight = displayHeight - statusbarHeight;

                pieceWidth = displayWidth / columns;
                pieceHeight = requiredHeight / columns;

                display(getApplicationContext());
            }
        });
    }
    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private boolean isSolved() {
        boolean solved = false;

        for (int i = 0; i < tileList.length; i++) {
            if (tileList[i].equals(String.valueOf(i))) {
                solved = true;
            } else {
                solved = false;
                break;
            }
        }

        return solved;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__picture_puzzle;
    }


    @Override
    public void onPopupAction(PopupType type, PopupAction action) {

    }
}
