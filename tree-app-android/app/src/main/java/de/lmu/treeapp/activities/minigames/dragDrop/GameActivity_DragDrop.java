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

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_DragDrop;
import de.lmu.treeapp.contentClasses.minigames.components.DragDropItem;
import de.lmu.treeapp.contentClasses.minigames.components.DragDropZone;

public class GameActivity_DragDrop extends GameActivity_Base {
    private Minigame_DragDrop dragDropGame;
    private ConstraintLayout layout;
    private int contentBoxId;
    private final List<ImageView> itemViews = new ArrayList<>();

    private int zoneNormalColor = 0;
    private int zoneDroppableColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_game__drag_drop);
        super.onCreate(savedInstanceState);
        dragDropGame = (Minigame_DragDrop) gameContent;
        layout = findViewById(R.id.cl);
        layout.setOnDragListener(new FreeDropListener());

        zoneNormalColor = getResources().getColor(R.color.white);
        zoneDroppableColor = getResources().getColor(R.color.droppable);

        ImageView contentBox = findViewById(R.id.game_dragdrop_content);
        int backgroundImage = getResources().getIdentifier(dragDropGame.image, "drawable", getPackageName());
        contentBox.setImageResource(backgroundImage);
        contentBoxId = contentBox.getId();

        for (DragDropItem item : dragDropGame.items) {
            ImageView iv = new ImageView(this);
            int imageId = getResources().getIdentifier(item.content, "drawable", getPackageName());
            iv.setImageResource(imageId);
            iv.setTag(item.match);

            SetItemStartPosition(iv, item);
            itemViews.add(iv);
            iv.setOnTouchListener(new DragDropItemTouchListener());
        }

        for (DragDropZone zone : dragDropGame.zones) {
            LinearLayout ll = new LinearLayout(this);
            ll.setBackgroundColor(zoneNormalColor);
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.width = zone.w;
            lp.height = zone.h;
            lp.topToTop = contentBoxId;
            lp.leftToLeft = contentBoxId;
            lp.rightToRight = contentBoxId;
            lp.bottomToBottom = contentBoxId;

            lp.horizontalBias = zone.x;
            lp.verticalBias = zone.y;

            ll.setTag(zone.match);
            ll.setLayoutParams(lp);
            layout.addView(ll);

            ll.setOnDragListener(new DropZoneListener());
        }

        Button sendButton = findViewById(R.id.game_dragdrop_sendButton);

        sendButton.setOnClickListener(view -> {
            for (DragDropZone zone : dragDropGame.zones) {
                if (!zone.IsMatchedRight()) {
                    onFail();
                    Reset();
                    return;
                }
            }
            Reset();
            onSuccess();
        });
    }

    private void ClearOtherZones(DragDropItem _item) {
        for (DragDropZone zone : dragDropGame.zones) {
            if (zone.currentItem == _item)
                zone.currentItem = null;
        }
    }

    private void SetItemStartPosition(ImageView _iv, DragDropItem _item) {
        ClearOtherZones(_item);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.width = _item.w;
        lp.height = _item.h;
        lp.topToTop = contentBoxId;
        lp.leftToLeft = contentBoxId;
        lp.rightToRight = contentBoxId;
        lp.bottomToBottom = contentBoxId;
        lp.horizontalBias = _item.x;
        lp.verticalBias = _item.y;

        _iv.setLayoutParams(lp);
        _iv.setVisibility(View.VISIBLE);
        layout.addView(_iv);
    }

    private void Reset() {
        for (ImageView itemView : itemViews) {
            ViewGroup owner = (ViewGroup) itemView.getParent();
            owner.removeView(itemView);
            SetItemStartPosition(itemView, dragDropGame.items.get(Integer.parseInt(itemView.getTag().toString())));
        }
        for (DragDropZone zone : dragDropGame.zones) {
            zone.currentItem = null;
        }
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
        //Drawable normalShape = getResources().getDrawable(R.drawable.button_background);

        @Override
        public boolean onDrag(View v, DragEvent event) {

            View view = (View) event.getLocalState();
            DragDropZone zone = dragDropGame.zones.get(Integer.parseInt(v.getTag().toString()));
            DragDropItem item = dragDropGame.items.get(Integer.parseInt(view.getTag().toString()));
            if (zone.IsFilled() && zone.currentItem != item) return false;
            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(zoneDroppableColor);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    zone.currentItem = null;
                    v.setBackgroundColor(zoneNormalColor);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    ClearOtherZones(item);
                    zone.currentItem = item;
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    view.setVisibility(View.VISIBLE);
                    v.setBackgroundColor(zoneNormalColor);
                default:
                    break;
            }
            return true;
        }
    }

    class FreeDropListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                case DragEvent.ACTION_DRAG_ENTERED:
                case DragEvent.ACTION_DRAG_EXITED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    SetItemStartPosition((ImageView) view, dragDropGame.items.get(Integer.parseInt(view.getTag().toString())));

                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    View view2 = (View) event.getLocalState();
                    view2.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            return true;
        }
    }

}
