package de.lmu.treeapp.activities.minigames.contour;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.entities.content.GameContourCheckpoint;
import de.lmu.treeapp.contentData.database.entities.content.GameContourRelations;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;

/**
 * Everything that happens while playing the "Blattkünstler" game
 */
public class GameActivity_Contour extends GameActivity_Base implements PopupInterface {

    /**
     * Instance variables
     */
    // Views
    protected DrawingViewDefault drawingView;
    protected ImageView backgroundBox;

    // Buttons
    ImageButton eraser;
    ImageButton pen;
    Button sendButton;

    //Random
    GameContourRelations contourGame;
    Popup popup;


    /**
     * Constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.drawingView = findViewById(R.id.single_touch_view);
        this.contourGame = (GameContourRelations) gameContent;
        this.backgroundBox = findViewById(R.id.game_contourGame_background);
        int backgroundImage = getResources().getIdentifier(this.contourGame.getImageResource(), "drawable", getPackageName());
        Glide.with(this).load(backgroundImage).into(backgroundBox);
        this.sendButton = findViewById(R.id.game_contour_sendButton);
        this.eraser = findViewById(R.id.button2);
        this.pen = findViewById(R.id.button1);
        this.popup = new Popup(this, treeId);

        // Finish button listener
        sendButton.setOnClickListener(view -> {
            drawingView.setDrawColor(Color.rgb(3, 130, 21), 1);
            if (!drawingView.isFalseCheckpoint()
                    && drawingView.isCrossedAllCheckpoints()
                    && drawingView.isAppearedAllCheckpoints()) {
                // you are here? Game is finished :)
                setDone(true);
                popup.showWithButtonText(PopupType.POSITIVE_ANIMATION, getString(R.string.popup_btn_finished));

            } else if (drawingView.isFalseCheckpoint()) {
                // Üff not kritzeln please
                popup.show(PopupType.NEGATIVE, getString(R.string.game_contour_do_not_scribble), null);

            } else {
                //You are here? Ough .. not so good at drawing?
                popup.showWithButtonText(PopupType.NEGATIVE, getString(R.string.popup_neutral_ok), getString(R.string.game_contour_try_to_hit_circles));
            }
        });

        eraser.setOnClickListener(v -> {
            drawingView.setDrawColor(Color.rgb(255, 255, 255), 3);
            drawingView.setEraser(true);
        });

        pen.setOnClickListener(v -> {
            drawingView.setDrawColor(Color.rgb(3, 130, 21), 1);
            drawingView.setEraser(false);
        });

    }

    /**
     * @param width  of image view with leaf image
     * @param height of image view with leaf image
     */
    public void setCheckpoints(int width, int height) {

        //Sets width of checkpoint circle in regard of screensize
        drawingView.setCheckpointThreshold((int) (width * 0.06));

        //Drawing view is only left half
        int halfThreshold = width / 2;

        //Points needed for array
        List<GameContourCheckpoint> checkpoints = contourGame.getCheckpoints();
        List<PointF> validCheckpoints = new ArrayList<>();
        List<PointF> invalidCheckpoints = new ArrayList<>();
        for (GameContourCheckpoint checkpoint : checkpoints) {
            PointF item;
            if (checkpoint.isValid) {
                item = new PointF((checkpoint.xRel + 1) * halfThreshold, checkpoint.yRel * height);
                validCheckpoints.add(item);
            } else {
                item = new PointF(checkpoint.xRel * width, checkpoint.yRel * height);
                invalidCheckpoints.add(item);
            }
        }

        //set checkpoints in drawing view for showing them in canvas
        drawingView.setCheckpoints(validCheckpoints.toArray(new PointF[0]));
        drawingView.setFalseCheckpoints(invalidCheckpoints.toArray(new PointF[0]));
    }

    /**
     * needed for responsive functionality
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        int width = this.backgroundBox.getWidth();
        int height = this.backgroundBox.getHeight();
        int heightDrawingView = drawingView.getHeight();
        int widthDrawingView = drawingView.getWidth();

        //if these are the same then go an make the points
        if (width == widthDrawingView && height == heightDrawingView) {
            setCheckpoints(width, height);
        } else {
            //This shouldn't happen
            Log.w("Resolution Check: ", "Height and Width not compatible");
        }
    }

    private void backToGame() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            //Clear the old drawing path
            drawingView.setClear(true);
            drawingView.setTouchable(false);
            //Draw the checkpoints for easier drawing
            drawingView.setAllCheckpointsAppeared(true);
            drawingView.setShowfirstStaticCheckpoint(true);
            drawingView.setFalseCheckpoint(false);
            drawingView.invalidate();
        }, 500);

        Handler handler2 = new Handler();
        handler2.postDelayed(() -> {
            //Draw the checkpoints for easier drawing
            drawingView.setAllCheckpointsAppeared(false);
            drawingView.setShowfirstStaticCheckpoint(false);
            drawingView.setClear(true);
            drawingView.setTouchable(true);
            drawingView.invalidate();
        }, 2500);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__contour;
    }

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (type == PopupType.POSITIVE_ANIMATION) {
            onSuccess();
        } else if (type == PopupType.NEGATIVE) {
            backToGame();
        } else {
            onSuccess();
        }
    }
}
