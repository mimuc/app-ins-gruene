package de.lmu.treeapp.activities.minigames.dragDrop;

import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.math.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropItem;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropRelations;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropZone;

public class DragDropHelper {
    private final List<ImageView> itemViews = new ArrayList<>();
    private Boolean isAlternateFirst;
    private final GameDragDropRelations gameDragDrop;
    private final ConstraintLayout container;
    private final boolean dragDrop;
    final float scale;


    public DragDropHelper(GameDragDropRelations dragDropRelations,
                          ConstraintLayout container, ImageView backgroundBox,
                          boolean game) {
        this(dragDropRelations, container, backgroundBox, ColorUtils.setAlphaComponent(container.getResources().getColor(android.R.color.white), 153), game);
    }

    public DragDropHelper(GameDragDropRelations dragDropRelations,
                          ConstraintLayout container, ImageView backgroundBox,
                          int color1, boolean game) {
        gameDragDrop = dragDropRelations;
        this.container = container;
        isAlternateFirst = null;
        dragDrop = game;

        container.setOnDragListener(new LayoutDragListener());

        //constrain imageView to actual image
        // get actual height and width of image
        final float actualHeight, actualWidth;
        final float imageViewHeight = backgroundBox.getHeight(), imageViewWidth = backgroundBox.getWidth();
        final float bitmapHeight = backgroundBox.getDrawable().getIntrinsicHeight(), bitmapWidth = backgroundBox.getDrawable().getIntrinsicWidth();
        if (imageViewHeight * bitmapWidth <= imageViewWidth * bitmapHeight) {
            actualWidth = bitmapWidth * imageViewHeight / bitmapHeight;
            actualHeight = imageViewHeight;
        } else {
            actualHeight = bitmapHeight * imageViewWidth / bitmapWidth;
            actualWidth = imageViewWidth;
        }

        //set new constraints to ImageView
        ConstraintSet set = new ConstraintSet();
        set.clone(container);
        set.clear(container.getId());

        //guidelineTop
        int guideLineTop = 101;
        set.create(guideLineTop, ConstraintSet.HORIZONTAL_GUIDELINE);
        float top = ((imageViewHeight - actualHeight) / 2) / imageViewHeight;
        set.setGuidelinePercent(guideLineTop, top);

        //guideLineBottom
        int guideLineBottom = 102;
        set.create(guideLineBottom, ConstraintSet.HORIZONTAL_GUIDELINE);
        float bottom = (imageViewHeight - (imageViewHeight - actualHeight) / 2) / imageViewHeight;
        set.setGuidelinePercent(guideLineBottom, bottom);

        //guidelineLeft
        int guideLineLeft = 103;
        set.create(guideLineLeft, ConstraintSet.VERTICAL_GUIDELINE);
        float left = ((imageViewWidth - actualWidth) / 2) / imageViewWidth;
        set.setGuidelinePercent(guideLineLeft, left);

        //guidelineRight
        int guideLineRight = 104;
        set.create(guideLineRight, ConstraintSet.VERTICAL_GUIDELINE);
        float right = (imageViewWidth - (imageViewWidth - actualWidth) / 2) / imageViewWidth;
        set.setGuidelinePercent(guideLineRight, right);

        //constrain ImageView to guidelines
        set.connect(backgroundBox.getId(), ConstraintSet.START, guideLineLeft, ConstraintSet.END, 0);
        set.connect(backgroundBox.getId(), ConstraintSet.END, guideLineRight, ConstraintSet.START, 0);
        set.connect(backgroundBox.getId(), ConstraintSet.BOTTOM, guideLineBottom, ConstraintSet.TOP, 0);
        set.connect(backgroundBox.getId(), ConstraintSet.TOP, guideLineTop, ConstraintSet.BOTTOM, 0);

        //apply new constraints to constraintlayout
        set.applyTo(container);

        if (actualHeight > actualWidth) { // some constant to set icon/dropzone size dependent on image height / width dependent of image
            scale = container.getHeight() / 1500f;
        } else {
            scale = container.getWidth() / 1200f;
        }

        // Load zones visually before the items.
        for (GameDragDropZone zone : gameDragDrop.getZones()) {
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            lp.width = (int) (zone.w * scale);
            lp.height = (int) (zone.h * scale);
            lp.topToTop = lp.leftToLeft = lp.rightToRight = lp.bottomToBottom = backgroundBox.getId();
            lp.horizontalBias = zone.x;
            lp.verticalBias = zone.y;

            LinearLayout ll = new LinearLayout(backgroundBox.getContext());
            ll.setTag(zone);
            ll.setLayoutParams(lp);
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(container.getContext(), R.drawable.round_neutral);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, color1);
            ll.setBackground(unwrappedDrawable);

            container.addView(ll);
            ll.setOnDragListener(new DragDropHelper.DropZoneListener());
        }
    }

    private void clearOtherZones(GameDragDropItem _item) {
        for (GameDragDropZone zone : gameDragDrop.getZones()) {
            if (zone.currentItem == _item)
                zone.currentItem = null;
        }
    }

    public void setItemPosition(ImageView iv, GameDragDropItem item, float x, float y) {
        clearOtherZones(item);
        item.x = x;
        item.y = y;
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lp.topToTop = lp.leftToLeft = lp.rightToRight = lp.bottomToBottom = container.getId();
        lp.width = (int) (item.w * scale);
        lp.height = (int) (item.h * scale);
        lp.horizontalBias = item.x;
        lp.verticalBias = item.y;
        iv.setLayoutParams(lp);
        iv.setVisibility(View.VISIBLE);
        container.addView(iv);
    }

    private void updateItemPosition(ConstraintLayout container, ImageView iv, GameDragDropItem item, float x, float y) {
        ViewGroup owner = (ViewGroup) iv.getParent();
        owner.removeView(iv);
        clearOtherZones(item);

        int itemWidth = (int) (item.w * scale);
        int itemHeight = (int) (item.h * scale);
        // Set position, but clamp to layout boundaries
        int posX = (int) MathUtils.clamp(x - itemWidth / 2f, 0, container.getWidth() - itemWidth);
        int posY = (int) MathUtils.clamp(y - itemHeight / 2f, 0, container.getHeight() - itemHeight);

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lp.topToTop = lp.leftToLeft = container.getId();
        lp.width = itemWidth;
        lp.height = itemHeight;
        lp.leftMargin = posX;
        lp.topMargin = posY;

        iv.setLayoutParams(lp);
        iv.setVisibility(View.VISIBLE);
        container.addView(iv);
    }

    public void reset() {
        for (int i = 0; i < itemViews.size(); i++) {
            ImageView itemView = itemViews.get(i);
            ViewGroup owner = (ViewGroup) itemView.getParent();
            if (owner != null) {
                owner.removeView(itemView);
            }
            float x, y;
            if (dragDrop) {
                x = i % 2 == 0 ? 0 : 1f;
                y = i / (float) itemViews.size();
            } else {
                x = ThreadLocalRandom.current().nextFloat();
                y = 1.0f;
            }
            setItemPosition(itemView, (GameDragDropItem) itemView.getTag(), x, y);
        }
        for (GameDragDropZone zone : gameDragDrop.getZones()) {
            zone.currentItem = null;
            zone.setValidMatch(false);
        }
        isAlternateFirst = null;
    }

    public void addItemView(ImageView view) {
        view.setOnTouchListener(new DragDropItemTouchListener());
        itemViews.add(view);
    }

    public boolean checkGameState() {
        if (gameDragDrop.isAlternate() && isAlternateFirst == null) {
            return false;
        }
        for (GameDragDropZone zone : gameDragDrop.getZones()) {
            if (gameDragDrop.isAlternate()) {
                if (!zone.validMatch) {
                    return false;
                }
            } else {
                if (!zone.validMatch || zone.currentItem == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public static class DragDropItemTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                // add rotation and scaling also to view while dragging
                // source: https://stackoverflow.com/questions/17049938/how-to-drag-a-rotated-dragshadow
                GameDragDropItem item = (GameDragDropItem) view.getTag();
                if (item.matchId == 1) {
                    view.setScaleX(1f);
                }
                double rotationRad = Math.toRadians(view.getRotation());
                final int w = (int) (view.getWidth() * view.getScaleX());
                final int h = (int) (view.getHeight() * view.getScaleY());
                double s = Math.abs(Math.sin(rotationRad));
                double c = Math.abs(Math.cos(rotationRad));
                final int width = (int) (w * c + h * s);
                final int height = (int) (w * s + h * c);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view) {
                    @Override
                    public void onDrawShadow(Canvas canvas) {
                        canvas.rotate(view.getRotation(), width / 2f, height / 2f);
                        if (item.matchId == 1) {
                            view.setScaleX(-1f);
                        }
                        canvas.scale(view.getScaleX(), view.getScaleY(), width / 2f,
                                height / 2f);
                        canvas.translate((width - view.getWidth()) / 2f,
                                (height - view.getHeight()) / 2f);
                        super.onDrawShadow(canvas);
                    }

                    @Override
                    public void onProvideShadowMetrics(Point shadowSize,
                                                       Point shadowTouchPoint) {
                        shadowSize.set(width, height);
                        shadowTouchPoint.set(shadowSize.x / 2, shadowSize.y / 2);
                    }
                };
                ClipData data = ClipData.newPlainText("", "");
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
            }
            return true;
        }
    }

    public class DropZoneListener implements View.OnDragListener {

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
                case DragEvent.ACTION_DRAG_ENTERED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    zone.currentItem = null;
                    if (gameDragDrop.isAlternate()) {
                        // Reset isAlternateFirst, if all leafs are removed
                        boolean isStart = true;
                        for (GameDragDropZone dropZone : gameDragDrop.getZones()) {
                            if (dropZone.currentItem != null) {
                                isStart = false;
                                break;
                            }
                        }
                        if (isStart) {
                            isAlternateFirst = null;
                            for (GameDragDropZone dropZone : gameDragDrop.getZones()) {
                                dropZone.validMatch = false;
                            }
                        } else if ((zone.matchId + zone.position) % 2 == (isAlternateFirst ? 1 : 0)) {
                            zone.validMatch = true;
                        } else {
                            zone.setValidMatch(false);
                        }
                    }
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup

                    // update matchIDs according to where you put your first leaf
                    if (gameDragDrop.isAlternate()) {
                        // matchId + position is always even if first match (0) is even or second match (1) is not even.
                        boolean isEvenFirstMatch = (zone.matchId + zone.position) % 2 == 0;
                        if (isAlternateFirst == null) {
                            isAlternateFirst = isEvenFirstMatch;

                            // Make empty zones valid
                            for (GameDragDropZone dropZone : gameDragDrop.getZones()) {
                                if ((dropZone.matchId + dropZone.position) % 2 == (isAlternateFirst ? 1 : 0)) {
                                    dropZone.validMatch = true;
                                }
                            }
                        }
                        zone.setValidMatch(Objects.equals(zone.matchId, item.matchId) && isEvenFirstMatch == isAlternateFirst);
                    } else {
                        zone.setValidMatch(Objects.equals(zone.matchId, item.matchId));
                    }

                    ViewGroup owner = (ViewGroup) itemView.getParent();
                    owner.removeView(itemView);
                    clearOtherZones(item);
                    zone.currentItem = item;
                    LinearLayout zoneLayout = (LinearLayout) zoneView;
                    // reset layout params of canvas
                    itemView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                    zoneLayout.addView(itemView);
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    itemView.setVisibility(View.VISIBLE);
                default:
                    break;
            }
            return true;
        }
    }

    public class LayoutDragListener implements View.OnDragListener {
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
                    updateItemPosition(container, (ImageView) itemView, (GameDragDropItem) itemView.getTag(), event.getX(), event.getY());
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
}


