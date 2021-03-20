package de.lmu.treeapp.activities.minigames.contour;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Drawing view - where the user is able to draw the contour of a leaf
 */
public class DrawingViewDefault extends View {

    // paint variables for drawing
    private final ArrayList<Paint> paintPenList = new ArrayList<>();
    public ArrayList<Path> pathPenList = new ArrayList<>();
    public Paint currentPaint = new Paint();
    public Path currentPath = new Path();
    public int currentColor;
    public float currentStrokeWidth;
    public float checkpointCrossedThreshold;
    public Paint circlePaint = new Paint();

    // contains all checkpoints
    private PointF[] checkpoints;

    // false checkpoints coordinates
    private PointF[] falseCheckpoints;

    // radius of circle
    private int checkpointThreshold;

    // booleans for showing checkpoints and checking when to do so
    private boolean[] checkpointsAppeared;
    private boolean[] checkpointsCrossed;
    private boolean clear;
    private boolean eraser;
    private boolean touchable;

    private boolean showfirstStaticCheckpoint;
    private boolean falseCheckpoint;

    // screen values
    private final float screenWidth;

    // Constructor
    public DrawingViewDefault(Context context, AttributeSet attrs) {

        super(context, attrs);

        this.screenWidth = this.getResources().getDisplayMetrics().widthPixels;

        // sets drawing values
        this.currentColor = Color.rgb(3, 130, 21);
        this.currentStrokeWidth = screenWidth * 0.02f;
        this.checkpointCrossedThreshold = this.currentStrokeWidth;

        //set various things for the checkpoints
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setStrokeWidth((float) (currentStrokeWidth / 1.5));
        this.circlePaint.setStyle(Paint.Style.STROKE);
        this.circlePaint.setStrokeJoin(Paint.Join.ROUND);
        this.circlePaint.setColor(Color.rgb(3, 130, 21));
        this.circlePaint.setAlpha(70);

        // start: everything has to be false
        this.showfirstStaticCheckpoint = false;
        this.falseCheckpoint = false;
        this.eraser = false;
        this.touchable = true;
        this.clear = false;
    }

    /**
     * Draws everything
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (this.clear) {
            pathPenList.clear();
            paintPenList.clear();
            this.clear = false;
        }

        if (!pathPenList.isEmpty()) {
            for (int i = 0; i < paintPenList.size(); i++) {
                canvas.drawPath(pathPenList.get(i), paintPenList.get(i));
            }
        }

        // draws checkpoints if crossed
        if (showfirstStaticCheckpoint) {
            canvas.drawCircle((int) (this.getWidth() / 2 * 1.10), ((float) checkpointThreshold * (float) 1.10), checkpointThreshold+checkpointCrossedThreshold, circlePaint);
        }
        if (checkpointsAppeared != null) {
            for (int i = 0; i < checkpointsAppeared.length; i++) {
                if (checkpointsAppeared[i]) {
                    canvas.drawCircle(checkpoints[i].x, checkpoints[i].y, checkpointThreshold+checkpointCrossedThreshold, circlePaint);
                }
            }
        }
    }

    /**
     * Everything that happens while touching
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (touchable) {

            float eventX = event.getX();
            float eventY = event.getY();

            // Checks if checkpoints are crossed
            for (int i = 0; i < checkpoints.length; i++) {
                if ((eventX > checkpoints[i].x - checkpointThreshold && eventX < checkpoints[i].x + checkpointThreshold) &&
                        (eventY > checkpoints[i].y - checkpointThreshold && eventY < checkpoints[i].y + checkpointThreshold)) {
                    if (!eraser) {
                        setCheckpointAppeared(true, i);
                        setCheckpointCrossed(true, i);
                    } else {
                        setCheckpointAppeared(false, i);
                        setCheckpointCrossed(false, i);
                    }
                }
            }

            // Checks if test/false checkpoint is crossed
            for (PointF checkpoint : falseCheckpoints) {
                if ((eventX > checkpoint.x - checkpointThreshold && eventX < checkpoint.x + checkpointThreshold) &&
                        (eventY > checkpoint.y - checkpointThreshold && eventY < checkpoint.y + checkpointThreshold)) {
                    setFalseCheckpoint(!eraser);
                }
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setPaintAndPen(currentColor, currentStrokeWidth);
                    currentPath.moveTo(eventX, eventY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    currentPath.lineTo(eventX, eventY);
                    break;
                default:
                    return false;
            }
            invalidate();
        }
        return true;
    }

    /**
     * Set paint and pen values
     */
    private void setPaintAndPen(int color, float strokeWidth) {
        currentPaint = getNewPaintPen(color, strokeWidth);
        currentPath = getNewPathPen();
        paintPenList.add(currentPaint);
        pathPenList.add(currentPath);
    }

    /**
     * Gets/creates a new drawing path
     */
    private Path getNewPathPen() {
        return new Path();
    }

    /**
     * New paint for the pen
     */
    private Paint getNewPaintPen(int color, float strokeWidth) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(color);
        return paint;
    }

    /**
     * used for the eraser tool (e.g. white)
     */
    public void setDrawColor(int color, int multiplicator) {
        currentColor = color;
        currentStrokeWidth = this.screenWidth * 0.02f;
        currentStrokeWidth = currentStrokeWidth * multiplicator;
    }


    // --- Usual getter and setter below ---

    public void setCheckpoints(PointF[] checkpoints) {
        this.checkpoints = checkpoints;
        this.checkpointsAppeared = new boolean[checkpoints.length];
        this.checkpointsCrossed = new boolean[checkpoints.length];
        Arrays.fill(this.checkpointsAppeared, false);
        Arrays.fill(this.checkpointsCrossed, false);

    }

    public void setCheckpointAppeared(boolean checkpointAppeared, int checkpoint) {
        this.checkpointsAppeared[checkpoint] = checkpointAppeared;
    }

    public void setAllCheckpointsAppeared(boolean checkpointAppeared) {
        Arrays.fill(this.checkpointsAppeared, checkpointAppeared);
    }

    public void setCheckpointCrossed(boolean checkpointCrossed, int checkpoint) {
        this.checkpointsCrossed[checkpoint] = checkpointCrossed;
    }

    public boolean isCrossedAllCheckpoints() {
        boolean isCrossed = true;
        for (boolean checkpoint : checkpointsCrossed) {
            if (!checkpoint) {
                isCrossed = false;
                break;
            }
        }
        return isCrossed;
    }

    public boolean isAppearedAllCheckpoints() {
        boolean isAppeared = true;
        for (boolean checkpoint : checkpointsAppeared) {
            if (!checkpoint) {
                isAppeared = false;
                break;
            }
        }
        return isAppeared;
    }

    public boolean isFalseCheckpoint() {
        return falseCheckpoint;
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }

    public void setCheckpointThreshold(int checkpointThreshold) {
        this.checkpointThreshold = checkpointThreshold;
    }

    public void setShowfirstStaticCheckpoint(boolean showfirstStaticCheckpoint) {
        this.showfirstStaticCheckpoint = showfirstStaticCheckpoint;
    }

    public void setFalseCheckpoints(PointF[] falseCheckpoints) {
        this.falseCheckpoints = falseCheckpoints;
    }

    public void setFalseCheckpoint(boolean falseCheckpoint) {
        this.falseCheckpoint = falseCheckpoint;
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public void setEraser(boolean eraser) {
        this.eraser = eraser;
    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

}
