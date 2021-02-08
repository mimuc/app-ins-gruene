package de.lmu.treeapp.activities.minigames.dragDrop;

import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropItem;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropRelations;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropZone;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupType;
import de.lmu.treeapp.popup.PopupInterface;

public class GameActivity_DragDrop extends GameActivity_Base implements PopupInterface {
    private final List<ImageView> itemViews = new ArrayList<>();
    protected Popup popup;
    private GameDragDropRelations dragDropGame;
    private ConstraintLayout container;
    private int zoneNormalColor;
    private int zoneDroppableColor;
    private int zoneFalseColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dragDropGame = (GameDragDropRelations) gameContent;
        container = findViewById(R.id.cl);
        container.setOnDragListener(new LayoutDragListener());

        zoneNormalColor = ColorUtils.setAlphaComponent(getResources().getColor(android.R.color.white), 128);
        zoneDroppableColor = ColorUtils.setAlphaComponent(getResources().getColor(R.color.droppable), 128);
        zoneFalseColor = ColorUtils.setAlphaComponent(getResources().getColor(R.color.falseDrop), 128); // color for false assignment

        ImageView backgroundBox = findViewById(R.id.game_dragdrop_background);
        int backgroundImage = getResources().getIdentifier(dragDropGame.getImageResource(), "drawable", getPackageName());
        Glide.with(this).load(backgroundImage).into(backgroundBox);

        // Load zones visually before the items.
        for (GameDragDropZone zone : dragDropGame.getZones()) {
            LinearLayout ll = new LinearLayout(this);
            ll.setBackgroundColor(zoneNormalColor);
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.width = zone.w;
            lp.height = zone.h;
            lp.topToTop = lp.leftToLeft = lp.rightToRight = lp.bottomToBottom = container.getId();

            lp.horizontalBias = zone.x;
            lp.verticalBias = zone.y;

            ll.setTag(zone);
            ll.setLayoutParams(lp);
            container.addView(ll);

            ll.setOnDragListener(new DropZoneListener());
        }

        for (GameDragDropItem item : dragDropGame.getItems()) {
            ImageView iv = new ImageView(this);
            int imageId = getResources().getIdentifier(item.content, "drawable", getPackageName());
            Glide.with(this).load(imageId).into(iv);
            iv.setTag(item);

            setItemStartPosition(iv, item);
            itemViews.add(iv);
            iv.setOnTouchListener(new DragDropItemTouchListener());
        }

        Button sendButton = findViewById(R.id.game_dragdrop_sendButton);

        sendButton.setOnClickListener(view -> {
            if(checkGameState()){
                onSuccess();
            }else{
                onFail();
                reset();
            }
        });

        popup = new Popup(this, treeId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__drag_drop;
    }

    private void clearOtherZones(GameDragDropItem _item) {
        for (GameDragDropZone zone : dragDropGame.getZones()) {
            if (zone.currentItem == _item)
                zone.currentItem = null;
        }
    }

    private void setItemStartPosition(ImageView _iv, GameDragDropItem _item) {
        clearOtherZones(_item);
        _item.x = ThreadLocalRandom.current().nextFloat();
        _item.y = ThreadLocalRandom.current().nextFloat();
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.topToTop = lp.leftToLeft = lp.rightToRight = lp.bottomToBottom = container.getId();
        lp.width = _item.w;
        lp.height = _item.h;
        lp.horizontalBias = _item.x;
        lp.verticalBias = _item.y;

        _iv.setLayoutParams(lp);
        _iv.setVisibility(View.VISIBLE);
        container.addView(_iv);
    }

    private void updateItemPosition(ImageView _iv, GameDragDropItem _item, float x, float y) {
        ViewGroup owner = (ViewGroup) _iv.getParent();
        owner.removeView(_iv);
        clearOtherZones(_item);

        // Set position, but clamp to layout boundaries
        int posX = (int) MathUtils.clamp(x - _item.w / 2f, 0, container.getWidth() - _item.w);
        int posY = (int) MathUtils.clamp(y - _item.h / 2f, 0, container.getHeight() - _item.h);

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.topToTop = lp.leftToLeft = container.getId();
        lp.width = _item.w;
        lp.height = _item.h;
        lp.leftMargin = posX;
        lp.topMargin = posY;

        _iv.setLayoutParams(lp);
        _iv.setVisibility(View.VISIBLE);
        container.addView(_iv);
    }

    private void reset() {
        for (ImageView itemView : itemViews) {
            ViewGroup owner = (ViewGroup) itemView.getParent();
            owner.removeView(itemView);
            setItemStartPosition(itemView, (GameDragDropItem) itemView.getTag());
        }
        for (GameDragDropZone zone : dragDropGame.getZones()) {
            zone.currentItem = null;
        }
    }

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (type == PopupType.POSITIVE_ANIMATION || type == PopupType.POSITIVE) {
            super.onSuccess();
        }
    }

    @Override
    protected void onSuccess() {
        popup.show(PopupType.POSITIVE_ANIMATION);
    }

    @Override
    protected void onFail() {
        popup.show(PopupType.NEGATIVE_ANIMATION);
    }

    private final class DragDropItemTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
            }

            return true;
        }
    }


    class DropZoneListener implements View.OnDragListener {
        //Drawable enterShape = getResources().getDrawable(R.drawable.ic_completed);
        //Drawable normalShape = getResources().getDrawable(R.drawable.button_background); // use other drawable than button_background (means a white background)

        @Override
        public boolean onDrag(View zoneView, DragEvent event) {

            View itemView = (View) event.getLocalState();

            if (zoneView == null || itemView == null) {
                return false;
            }

            GameDragDropZone zone = (GameDragDropZone) zoneView.getTag();
            GameDragDropItem item = (GameDragDropItem) itemView.getTag();

            if ((zone.isFilled() && zone.currentItem != item)) return false;
            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    zone.setValidMatch(Objects.equals(zone.matchId, item.matchId));
                    zoneView.setBackgroundColor(zone.validMatch ? zoneDroppableColor : zoneFalseColor);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    zone.currentItem = null;
                    zoneView.setBackgroundColor(zoneNormalColor);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    ViewGroup owner = (ViewGroup) itemView.getParent();
                    owner.removeView(itemView);
                    clearOtherZones(item);
                    zone.currentItem = item;

                    LinearLayout zoneLayout = (LinearLayout) zoneView;
                    // reset layout params of canvas
                    itemView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    zoneLayout.addView(itemView);
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    itemView.setVisibility(View.VISIBLE);
                    zoneView.setBackgroundColor(zoneNormalColor);
                default:
                    break;
            }
            return true;
        }
    }

    class LayoutDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                case DragEvent.ACTION_DRAG_ENTERED:
                case DragEvent.ACTION_DRAG_EXITED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DROP: {
                    // Dropped, reassign View to ViewGroup
                    View itemView = (View) event.getLocalState();
                    updateItemPosition((ImageView) itemView, (GameDragDropItem) itemView.getTag(), event.getX(), event.getY());
                    break;
                }
                case DragEvent.ACTION_DRAG_ENDED: {
                    // do nothing
                    break;
                }
                default:
                    break;
            }
            return true;
        }
    }

    private boolean checkGameState() {
        for (GameDragDropZone zone : dragDropGame.getZones()) {
            if (!zone.validMatch) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (checkGameState()) super.onSuccess();
        else super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (checkGameState()) super.onSuccess();
        else finish();
        return true;
    }
}
