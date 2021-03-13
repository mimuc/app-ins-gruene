package de.lmu.treeapp.activities.minigames.leafOrder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.util.concurrent.ThreadLocalRandom;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.activities.minigames.dragDrop.DragDropHelper;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropItem;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropRelations;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropZone;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;

public class GameActivity_LeafOrder extends GameActivity_Base implements PopupInterface {
    protected Popup popup;
    private GameDragDropRelations leafOrderGame;
    private ConstraintLayout container;
    protected DragDropHelper dragDropHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        leafOrderGame = (GameDragDropRelations) gameContent;
        container = findViewById(R.id.cl_ast);

        ImageView backgroundBox = findViewById(R.id.game_leaforder_background);
        int backgroundImage = getResources().getIdentifier(leafOrderGame.getImageResource(), "drawable", getPackageName());
        Glide.with(this).load(backgroundImage).dontTransform().into(backgroundBox);

        popup = new Popup(this, treeId);

        container.post(() -> {
            dragDropHelper = new DragDropHelper(leafOrderGame, container, false);

            for (GameDragDropItem item : leafOrderGame.getItems()) {
                ImageView iv = setImageView(item);
                animateItem(iv, item);
            }
        });

        Button sendButton = findViewById(R.id.game_leaforder_sendButton);

        sendButton.setOnClickListener(view -> {
            if (dragDropHelper.checkGameState()) {
                onSuccess();
            } else {
                onFail();
                dragDropHelper.reset();
            }

        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__leaf_order;
    }

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (type == PopupType.POSITIVE_ANIMATION && action == PopupAction.ACCEPT) {
            super.onSuccess();
        }
    }

    @Override
    protected void onSuccess() {
        popup.setButtonAcceptText(getResources().getString(R.string.popup_btn_finished));
        popup.show(PopupType.POSITIVE_ANIMATION);
        for (GameDragDropZone zone : leafOrderGame.getZones()) {
            zone.validMatch = false;
        }
    }

    @Override
    protected void onFail() {
        popup.setLooseTitle(getString(R.string.popup_negative_title_close));
        popup.showWithButtonText(PopupType.NEGATIVE_ANIMATION, getString(R.string.popup_neutral_ok), getString(R.string.popup_try_again_short));
    }

    private ImageView setImageView(GameDragDropItem item) {
        final Activity activity = this;
        if (activity.isDestroyed() || activity.isFinishing()) {
            return new ImageView(this);
        }
        ImageView iv = new ImageView(this);
        int imageId = getResources().getIdentifier(item.content, "drawable", getPackageName());
        Glide.with(this).load(imageId).into(iv);
        if (item.matchId == 1) {
            iv.setScaleX(-1f);
        }
        iv.setRotation(item.angle);
        iv.setTag(item);
        return iv;
    }

    private void animateItem(ImageView iv, GameDragDropItem item) {
        float x = ThreadLocalRandom.current().nextFloat();
        dragDropHelper.setItemPosition(iv, item, x, 0.0f);
        int bottomOfScreen = getResources().getDisplayMetrics()
                .heightPixels - 770;
        ObjectAnimator animation = ObjectAnimator.ofFloat(iv, "translationY", bottomOfScreen);
        animation.setDuration(2000);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                container.removeView(iv);
                ImageView iv = setImageView(item);
                dragDropHelper.setItemPosition(iv, item, x, 1.0f);
                dragDropHelper.addItemView(iv);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                animation.cancel();
            }
        });
        animation.start();
    }


    @Override
    public void onBackPressed() {
        if (dragDropHelper.checkGameState()) super.onSuccess();
        else super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (dragDropHelper.checkGameState()) super.onSuccess();
        else super.onSupportNavigateUp();
        return true;
    }
}