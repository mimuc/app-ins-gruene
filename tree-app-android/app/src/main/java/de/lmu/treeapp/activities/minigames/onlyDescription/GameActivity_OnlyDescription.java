package de.lmu.treeapp.activities.minigames.onlyDescription;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.activities.minigames.takePicture.GameActivity_TakePicture;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.entities.content.GameOnlyDescriptionTextItem;
import de.lmu.treeapp.contentData.database.entities.content.GameOnlyDescriptionImageItem;
import de.lmu.treeapp.contentData.database.entities.content.GameOnlyDescriptionRelations;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;

import static com.google.android.flexbox.FlexWrap.WRAP;

public class GameActivity_OnlyDescription extends GameActivity_Base implements PopupInterface {
    ArrayList<OnlyDescriptionElement> sList;
    private final List<GameOnlyDescriptionImageItem> correctTreeObjs = new ArrayList<>();
    private GameOnlyDescriptionRelations game;
    ArrayList<ArrayList<Integer>> grid = new ArrayList<>();
    List<GameOnlyDescriptionImageItem> items;
    RecyclerAdapter adapter;
    Popup popup;
    Popup popupDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = (GameOnlyDescriptionRelations) gameContent;

        // get text items from database
        sList = getListItemData();

        ViewFlipper viewFlipper = findViewById(R.id.viewFlipperOnlyDescription);
        // set the animation type to ViewFlipper
        viewFlipper.setInAnimation(this, R.anim.fragment_fade_enter);
        viewFlipper.setOutAnimation(this, R.anim.fragment_fade_exit);

        // set images according to tree and game as background
        setOnlyDescriptionBackground();

        Button nextButton1 = findViewById(R.id.game_onlyDescription_nextButton1);
        Button nextButton2 = findViewById(R.id.game_onlyDescription_nextButton2);
        Button prevButton1 = findViewById(R.id.game_onlyDescription_prevButton1);
        Button prevButton2 = findViewById(R.id.game_onlyDescription_prevButton2);
        Button doneButton = findViewById(R.id.game_onlyDescription_doneButton);

        setOnlyDescriptionRecycler(sList.subList(0, 1), R.id.game_onlyDescription_content);
        nextButton1.setOnClickListener(view -> {
            viewFlipper.showNext();
            setOnlyDescriptionRecycler(sList.subList(1, sList.size() - 1),
                    R.id.game_onlyDescription_content2);
        });
        nextButton2.setOnClickListener(view -> {
            viewFlipper.showNext();
            setOnlyDescriptionRecycler(sList.subList(sList.size() - 1, sList.size()),
                    R.id.game_onlyDescription_content3);
        });
        prevButton1.setOnClickListener(view -> {
            viewFlipper.showPrevious();
            setOnlyDescriptionRecycler(sList.subList(1, sList.size() - 1),
                    R.id.game_onlyDescription_content2);
        });
        doneButton.setOnClickListener(view -> {
            popupDone = new Popup(this, treeId);
            popupDone.setWinTitle(getString(R.string.popup_win_title_done));
            if (gameId == 502) {
                popupDone.setButtonSecondary(true);
                popupDone.setButtonSecondaryText(getString(R.string.popup_btn_finished));
                popupDone.showWithButtonText(PopupType.POSITIVE, getString(R.string.popup_btn_foto),
                        getString(R.string.popup_only_description_foto_text));
            } else {
                popupDone.showWithButtonText(PopupType.POSITIVE_ANIMATION, getString(R.string.popup_btn_finished));
            }
        });
        prevButton2.setOnClickListener(view -> {
            viewFlipper.showPrevious();
            setOnlyDescriptionRecycler(sList.subList(1, sList.size() - 1),
                    R.id.game_onlyDescription_content2);
        });

        // Popup with game description
        popup = new Popup(this, treeId);
        popup.setNeutralTitle(game.getName());
        popup.showWithButtonText(PopupType.NEUTRAL, getString(R.string.popup_btn_continue), game.getDescription());
    }

    // for background image grid
    private ArrayList<Integer> getXPosition(int imageSize) {
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            positions.add((imageSize * i) + 50);
        }
        return positions;
    }

    // for background image grid
    private ArrayList<Integer> getYPosition(int imageSize) {
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            positions.add((imageSize * i) + 10);
        }
        return positions;
    }

    // set images according to tree and game as background
    private void setOnlyDescriptionBackground() {
        ConstraintLayout container = findViewById(R.id.only_description);

        grid.add(getXPosition(220));
        grid.add(getYPosition(180));

        // get image items from database
        items = game.getImageItems();
        for (GameOnlyDescriptionImageItem obj : items) {
            if (obj.treeId == treeId) {
                correctTreeObjs.add(obj);
            }
        }
        Collections.shuffle(correctTreeObjs);

        ArrayList<Integer> xPosition = new ArrayList<>();
        ArrayList<Integer> yPosition = new ArrayList<>();
        int uneven = 0;
        int x = grid.get(0).size();
        int y = grid.get(1).size();
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                if (uneven % 2 == 0) {
                    xPosition.add(grid.get(0).get(i));
                    yPosition.add(grid.get(1).get(j));
                }
                uneven++;
            }
        }
        for (int i = 0; i < correctTreeObjs.size(); i++) {
            GameOnlyDescriptionImageItem item = correctTreeObjs.get(i);
            ImageView iv = new ImageView(this);
            int imageId = getResources().getIdentifier(item.content,
                    "drawable", getPackageName());
            Glide.with(this).load(imageId).into(iv);
            iv.setTag(item);
            iv.setRotation(ThreadLocalRandom.current().nextInt(360));
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.
                    LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT
                    , ConstraintLayout.LayoutParams.WRAP_CONTENT);
            item.x = xPosition.get(i) / 1000f;
            item.y = yPosition.get(i) / 1000f;
            lp.width = item.w;
            lp.height = item.h;
            lp.topToTop = lp.leftToLeft = lp.rightToRight = lp.bottomToBottom = container.getId();
            lp.horizontalBias = item.x;
            lp.verticalBias = item.y;
            iv.setLayoutParams(lp);
            iv.setVisibility(View.VISIBLE);
            iv.setTranslationZ(0);
            container.addView(iv);
        }
    }

    // set description texts
    private void setOnlyDescriptionRecycler(List<OnlyDescriptionElement> list,
                                            int containerId) {

        // Recycler View Content
        RecyclerView recyclerViewContent = findViewById(containerId);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this) {
        };
        layoutManager.setFlexWrap(WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setAlignItems(AlignItems.CENTER);
        recyclerViewContent.setLayoutManager(layoutManager);
        recyclerViewContent.setPadding(8, 30, 8, 30);

        adapter = new de.lmu.treeapp.activities.minigames.onlyDescription.
                RecyclerAdapter(list, 0, this);
        recyclerViewContent.setAdapter(adapter);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__only_description;
    }

    // get text items from database
    private ArrayList<OnlyDescriptionElement> getListItemData() {
        ArrayList<OnlyDescriptionElement> listTextItems = new ArrayList<>();
        for (GameOnlyDescriptionTextItem onlyDescriptionItem : game.getTextItems()) {
            listTextItems.add(new OnlyDescriptionElement(onlyDescriptionItem.content));
        }
        return listTextItems;
    }

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (type == PopupType.POSITIVE && action == PopupAction.ACCEPT) {
            DataManager.getInstance(getApplicationContext()).setGameCompleted(parentCategory, gameContent.getId(), parentTree).subscribe();
            Intent intent = new Intent(this, GameActivity_TakePicture.class);
            intent.putExtra("TreeId", treeId);
            intent.putExtra("Category", parentCategory);
            intent.putExtra("GameId", 303);
            intent.putExtra("GameName", gameContent.getName());
            this.startActivity(intent);
            finish();
        }
        if (action == PopupAction.SECONDARY || type == PopupType.POSITIVE_ANIMATION) {
            super.onSuccess();
        }
    }
}



