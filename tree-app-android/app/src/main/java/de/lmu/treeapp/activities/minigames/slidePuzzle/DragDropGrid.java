package de.lmu.treeapp.activities.minigames.slidePuzzle;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.customview.widget.ViewDragHelper;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.R;

public class DragDropGrid extends RelativeLayout {
    private ViewDragHelper viewDragHelper;
    private MoveCalculator calculator;
    private int mDrawableId;
    private int dimensions;
    private int mHeight;
    private int mWidth;
    private int mItemWidth;
    private int mItemHeight;
    private boolean isImg;
    private final List<ImageView> tiles = new ArrayList<>();
    private OnCompleteCallback mOnCompleteCallback;

    public DragDropGrid(Context context) {
        super(context);
        init();
    }


    public DragDropGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragDropGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mHeight = getHeight();
                mWidth = getWidth();
                getViewTreeObserver().removeOnPreDrawListener(this);
                if (mDrawableId != 0 && dimensions != 0) {
                    createChildren();
                }
                return false;
            }
        });
        calculator = new MoveCalculator();

        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                int index = indexOfChild(child);
                return calculator.getScrollDirection(index) != MoveCalculator.INVALID;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {

                int index = indexOfChild(child);
                int position = calculator.getModel(index).pos;
                int selfLeft = (position % dimensions) * mItemWidth;
                int leftEdge = selfLeft - mItemWidth;
                int rightEdge = selfLeft + mItemWidth;
                int direction = calculator.getScrollDirection(index);
                switch (direction) {
                    case MoveCalculator.LEFT:
                        if (left <= leftEdge)
                            return leftEdge;
                        else return Math.min(left, selfLeft);

                    case MoveCalculator.RIGHT:
                        if (left >= rightEdge)
                            return rightEdge;
                        else return Math.max(left, selfLeft);
                    default:
                        return selfLeft;
                }
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                int index = indexOfChild(child);
                Tile model = calculator.getModel(index);
                int position = model.pos;

                int selfTop = (position / dimensions) * mItemHeight;
                int topEdge = selfTop - mItemHeight;
                int bottomEdge = selfTop + mItemHeight;
                int direction = calculator.getScrollDirection(index);

                switch (direction) {
                    case MoveCalculator.TOP:
                        if (top <= topEdge)
                            return topEdge;
                        else return Math.min(top, selfTop);
                    case MoveCalculator.BOTTOM:
                        if (top >= bottomEdge)
                            return bottomEdge;
                        else return Math.max(top, selfTop);
                    default:
                        return selfTop;
                }
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                int index = indexOfChild(releasedChild);
                boolean isCompleted = calculator.swapValueWithInvisibleModel(index);
                Tile item = calculator.getModel(index);
                viewDragHelper.settleCapturedViewAt(item.x * mItemWidth, item.y * mItemHeight);
                View invisibleView = getChildAt(0);
                ViewGroup.LayoutParams layoutParams = invisibleView.getLayoutParams();
                invisibleView.setLayoutParams(releasedChild.getLayoutParams());
                releasedChild.setLayoutParams(layoutParams);
                invalidate();
                if (isCompleted) {
                    invisibleView.setVisibility(VISIBLE);
                    mOnCompleteCallback.onComplete();
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return viewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    /**
     * @param isImg Defining wether the grid contains the puzzle-image (isImg = true)
     *              or displaying false tiles (isImg = false)
     **/
    public void setImage(int drawableId, int squareRootNum, boolean isImg) {
        this.dimensions = squareRootNum;
        this.mDrawableId = drawableId;
        this.isImg = isImg;
        if (mWidth != 0 && mHeight != 0) {
            createChildren();
        }
    }

    private void createChildren() {
        removeAllViews();
        calculator.setSquareRootNum(dimensions);
        Bitmap bitmap = null;
        if (isImg) {
            DisplayMetrics dm = getResources().getDisplayMetrics();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDensity = dm.densityDpi;

            Bitmap resource = BitmapFactory.decodeResource(getResources(), mDrawableId, options);
            bitmap = zoomImg(resource, mWidth, mHeight);
            resource.recycle();
        }

        mItemWidth = mWidth / dimensions;

        mItemHeight = mHeight / dimensions;
        
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                ImageView iv = new ImageView(getContext());
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = j * mItemWidth;
                lp.topMargin = i * mItemHeight;
                iv.setLayoutParams(lp);
                if (isImg) { // the puzzle-grid
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    Bitmap b = Bitmap.createBitmap(bitmap, lp.leftMargin, lp.topMargin, mItemWidth, mItemHeight);
                    iv.setImageBitmap(b);
                } else { // the grid marking false tiles
                    int padding_in_dp = 10;
                    final float scale = getResources().getDisplayMetrics().density;
                    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                    iv.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
                    iv.setImageResource(R.drawable.ic_cross_circle_red);
                    iv.setScaleType(ImageView.ScaleType.FIT_END);
                    iv.setVisibility(INVISIBLE);
                }
                tiles.add(iv);
                addView(iv);
            }
        }
        if (isImg) randomOrder();
        else this.setVisibility(GONE);
    }

    public Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    public void randomOrder() {
        int num = dimensions * dimensions * 8;
        View invisibleView = getChildAt(0);
        View neighbor;
        for (int i = 0; i < num; i++) {
            int neighborPosition = calculator.findNeighborIndexOfInvisibleModel();
            ViewGroup.LayoutParams invisibleLp = invisibleView.getLayoutParams();
            neighbor = getChildAt(neighborPosition);
            invisibleView.setLayoutParams(neighbor.getLayoutParams());
            neighbor.setLayoutParams(invisibleLp);
            calculator.swapValueWithInvisibleModel(neighborPosition);
        }
        invisibleView.setVisibility(INVISIBLE);
    }

    /**
     * Marks all incorrect tiles with an icon.
     * @param falseTiles List containing ids of false tiles
     * **/
    public void markFalseTiles(List<Integer> falseTiles) {
        this.setVisibility(VISIBLE);
        for (int tileId : falseTiles) {
            tiles.get(tileId).setVisibility(VISIBLE);
        }

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            for (ImageView tile : tiles) {
                tile.setVisibility(INVISIBLE);
            }
            this.setVisibility(GONE);
        }, 1100);
    }

    List<Integer> getFalseTiles() {
        return calculator.getFalseTiles();
    }

    public void setOnCompleteCallback(OnCompleteCallback onCompleteCallback) {
        mOnCompleteCallback = onCompleteCallback;
    }

    public interface OnCompleteCallback {
        void onComplete();
    }
}

