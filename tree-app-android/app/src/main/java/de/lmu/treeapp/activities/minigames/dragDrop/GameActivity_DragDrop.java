package de.lmu.treeapp.activities.minigames.dragDrop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropItem;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropRelations;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropZone;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;

public class GameActivity_DragDrop extends GameActivity_Base implements PopupInterface {
    protected Popup popup;
    private GameDragDropRelations dragDropGame;
    protected DragDropHelper dragDropHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dragDropGame = (GameDragDropRelations) gameContent;
        ConstraintLayout container = findViewById(R.id.cl);
        ImageView backgroundBox = findViewById(R.id.game_dragdrop_background);
        int backgroundImage = getResources().getIdentifier(dragDropGame.getImageResource(), "drawable", getPackageName());
        Glide.with(this).load(backgroundImage).into(backgroundBox);
        backgroundBox.setImageResource(backgroundImage);

        container.post(() -> {
            dragDropHelper = new DragDropHelper(dragDropGame, container, backgroundBox, true);

            List<GameDragDropItem> items = dragDropGame.getItems();
            Collections.shuffle(items);
            for (int i = 0; i < items.size(); i++) {
                GameDragDropItem item = items.get(i);
                ImageView iv = new ImageView(this);
                int imageId = getResources().getIdentifier(item.content, "drawable", getPackageName());
                Glide.with(this).load(imageId).into(iv);
                iv.setTag(item);
                dragDropHelper.addItemView(iv);
            }
            dragDropHelper.reset();
        });

        Button sendButton = findViewById(R.id.game_dragdrop_sendButton);
        sendButton.setOnClickListener(view -> {
            if (dragDropHelper.checkGameState()) {
                setDone(dragDropHelper.checkGameState());
                popup.setButtonAcceptText(getResources().getString(R.string.popup_btn_finished));
                popup.show(PopupType.POSITIVE_ANIMATION);
            } else {
                popup.setLooseTitle(getString(R.string.popup_negative_title_close));
                popup.showWithButtonText(PopupType.NEGATIVE_ANIMATION, getString(R.string.popup_neutral_ok), getString(R.string.popup_try_again_short));
                dragDropHelper.reset();
            }
        });
        popup = new Popup(this, treeId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__drag_drop;
    }

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (type == PopupType.POSITIVE_ANIMATION || type == PopupType.POSITIVE) {
            onSuccess();
        }
    }
}