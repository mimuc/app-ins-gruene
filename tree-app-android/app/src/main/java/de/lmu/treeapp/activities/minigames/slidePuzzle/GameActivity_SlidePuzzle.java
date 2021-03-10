package de.lmu.treeapp.activities.minigames.slidePuzzle;

import android.os.Bundle;


import androidx.fragment.app.Fragment;

import com.google.android.gms.common.util.ArrayUtils;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;

public class GameActivity_SlidePuzzle extends GameActivity_Base implements PopupInterface, Runnable {
    DragDropGrid grid;
    int dimension = 3;
    Popup popup;
    int img = R.drawable.sb_bluete_foto_ahorn;
    Fragment imageSelectFragment;
    int[] mwTrees = new int[]{1, 9, 5, 3, 8};

    enum BlossomType {
        none,
        male,
        female,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game__picture_puzzle);
        int treeId = parentTree.getId();
        if (ArrayUtils.contains(mwTrees, treeId)) {
            int imgM = selectImage(BlossomType.male);
            int imgF = selectImage(BlossomType.female);
            imageSelectFragment = new SlidePuzzle_ImageSelection();
            Bundle b = new Bundle();
            b.putInt("tree", treeId);
            b.putInt("imgM", imgM);
            b.putInt("imgF", imgF);
            imageSelectFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.selection_frame, imageSelectFragment).commit();
        } else {
            startPuzzle(BlossomType.none);
        }

    }

    public void startPuzzle(BlossomType type) {
        if (ArrayUtils.contains(mwTrees, treeId)) {
            getSupportFragmentManager().beginTransaction()
                    .detach(imageSelectFragment).commit();
        }
        img = selectImage(type);
        popup = new Popup(this);
        popup.setWinTitle(getString(R.string.slidepuzzle_wonderful));
        grid = findViewById(R.id.grid);
        grid.setImage(img, dimension);
        grid.setOnCompleteCallback(() -> {
            popup.showWithButtonText(PopupType.POSITIVE,
                    getString(R.string.popup_btn_finished),
                    getString(R.string.slidepuzzle_win));
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

    //Buche, Fichte, Hasel, Kiefer, Tanne (M/W)
    //1, 9, 5, 3, 8
    //Ahorn, Birke, Eberesche, Eiche, Linde
    //0, 6, 7, 4, 2

    private int selectImage(BlossomType type) {
        int img;
        switch (parentTree.getId()) {
            case 0:
                img = R.drawable.sb_bluete_foto_ahorn;
                break;
            case 1:
                if (type == BlossomType.male) {
                    img = R.drawable.sb_bluete_foto_buche_m;
                } else {
                    img = R.drawable.sb_bluete_foto_buche_w;
                }
                break;
            case 2:
                img = R.drawable.sb_bluete_foto_linde;
                break;
            case 3:
                if (type == BlossomType.male) {
                    img = R.drawable.sb_bluete_foto_kiefer_m;
                } else {
                    img = R.drawable.sb_bluete_foto_kiefer_w;
                }
                break;
            case 4:
                img = R.drawable.sb_bluete_foto_eiche;
                break;
            case 5:
                if (type == BlossomType.male) {
                    img = R.drawable.sb_bluete_foto_hasel_m;
                } else {
                    img = R.drawable.sb_bluete_foto_hasel_w;
                }
                break;
            case 6:
                img = R.drawable.sb_bluete_foto_birke;
                break;
            case 7:
                img = R.drawable.sb_bluete_foto_eberesche;
                break;
            case 8:
                if (type == BlossomType.male) {
                    img = R.drawable.sb_bluete_foto_tanne_m;
                } else {
                    img = R.drawable.sb_bluete_foto_tanne_w;
                }
                break;
            case 9:
                if (type == BlossomType.male) {
                    img = R.drawable.sb_bluete_foto_fichte_m;
                } else {
                    img = R.drawable.sb_bluete_foto_fichte_w;
                }
                break;
            default:
                img = R.drawable.sb_bluete_foto_ahorn;
                break;
        }
        return img;
    }
}
