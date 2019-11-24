package de.lmu.treeapp.activities.minigames.dragDrop;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
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
    private List<ImageView> itemViews = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_game__drag_drop);
        super.onCreate(savedInstanceState);
        dragDropGame = (Minigame_DragDrop) gameContent;
        layout = findViewById(R.id.cl);
        layout.setOnDragListener(new FreeDropListener());

        ImageView contentBox = findViewById(R.id.game_dragdrop_content);
        int backgroundImage = getResources().getIdentifier(dragDropGame.image, "drawable", getPackageName());
        contentBox.setImageResource(backgroundImage);
        contentBoxId = contentBox.getId();

        for (DragDropItem item : dragDropGame.items){
            ImageView iv = new ImageView(this);
            int imageId = getResources().getIdentifier(item.content, "drawable", getPackageName());
            iv.setImageResource(imageId);
            iv.setTag(item.match);
            iv.setBackgroundColor(getResources().getColor(R.color.lightGrey));

            SetItemStartPosition(iv,item);
            itemViews.add(iv);
            iv.setOnTouchListener(new DragDropItemTouchListener());
        }

        for (DragDropZone zone : dragDropGame.zones){
            LinearLayout ll = new LinearLayout(this);
            ll.setBackgroundResource(R.drawable.button_background);
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.width = 200;
            lp.height = 200;
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

        sendButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (DragDropZone zone : dragDropGame.zones){
                    if (!zone.IsMatchedRight()){
                        onFail();
                        Reset();
                        return;
                    }
                }
                Reset();
                onSuccess();
            }
        });
    }

    private void ClearOtherZones(DragDropItem _item){
        for (DragDropZone zone : dragDropGame.zones){
            if (zone.currentItem == _item)
                zone.currentItem = null;
        }
    }

    private void SetItemStartPosition(ImageView _iv, DragDropItem _item){
        ClearOtherZones(_item);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.width = 200;
        lp.height = 200;
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

    private void Reset(){
        for (ImageView itemView : itemViews){
            ViewGroup owner = (ViewGroup) itemView.getParent();
            owner.removeView(itemView);
            SetItemStartPosition(itemView, dragDropGame.items.get(Integer.parseInt(itemView.getTag().toString())));
        }
        for (DragDropZone zone : dragDropGame.zones){
            zone.currentItem = null;
        }
    }

    private final class DragDropItemTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("","");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
            }

            return true;
        }
    }


    class DropZoneListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(R.drawable.ic_completed);
        Drawable normalShape = getResources().getDrawable(R.drawable.button_background);

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
                    v.setBackground(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    zone.currentItem = null;
                    v.setBackground(normalShape);
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
                    v.setBackground(normalShape);
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
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
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
