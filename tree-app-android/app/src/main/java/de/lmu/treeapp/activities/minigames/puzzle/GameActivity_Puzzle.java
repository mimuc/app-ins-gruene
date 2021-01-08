package de.lmu.treeapp.activities.minigames.puzzle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentData.database.entities.content.GamePuzzleRelations;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;

import static java.lang.Math.abs;

public class GameActivity_Puzzle extends GameActivity_Base implements PopupInterface {
    ArrayList<PuzzlePiece> pieces;
    private GamePuzzleRelations game;
    Popup popup;
    String time;
    Boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = (GamePuzzleRelations) gameContent;
        final RelativeLayout layout = findViewById(R.id.layout);
        ImageView imageView = findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(game.getImageResource(), "drawable", getPackageName()));
        imageView.setImageBitmap(bitmap);
        imageView.post(() -> {
            pieces = splitImage();
            TouchListener touchListener = new TouchListener(GameActivity_Puzzle.this);
            Collections.shuffle(pieces);
            for (PuzzlePiece piece : pieces) {
                piece.setOnTouchListener(touchListener);
                layout.addView(piece);
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                // move piece to random position on bottom
                lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.pieceWidth);
                lParams.topMargin = layout.getHeight() - piece.pieceHeight;
                piece.setLayoutParams(lParams);
            }
        });

        popup = new Popup(this);
        popup.setWinTitle(getString(R.string.popup_win_title_done));
        popup.setNeutralTitle(getString(R.string.popup_puzzle_neutral_text));
        popup.showWithButtonText(PopupType.NEUTRAL, getString(R.string.popup_btn_continue), getString(R.string.game_puzzle_instructions));
    }

    private ArrayList<PuzzlePiece> splitImage() {
        // game difficulty could be reduced or increased by changing pieceNumber / rows / cols -> The higher the more difficult
        int piecesNumber = 12;
        int rows = 4;
        int cols = 3;

        ImageView imageView = findViewById(R.id.imageView);
        ArrayList<PuzzlePiece> pieces = new ArrayList<>(piecesNumber);

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimensions = getBitmapPositionInsideImageView(imageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        int croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);

        // scale
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        // crop
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);

        int pieceWidth = croppedImageWidth / cols;
        int pieceHeight = croppedImageHeight / rows;

        // calc right coords for piece
        int yCoord = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                int offsetX = 0;
                int offsetY = 0;
                if (col > 0) {
                    offsetX = pieceWidth / 3;
                }
                if (row > 0) {
                    offsetY = pieceHeight / 3;
                }

                // create final piece from cropped
                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord - offsetX, yCoord - offsetY, pieceWidth + offsetX, pieceHeight + offsetY);
                PuzzlePiece piece = new PuzzlePiece(getApplicationContext());
                piece.xCoord = xCoord - offsetX + imageView.getLeft();
                piece.yCoord = yCoord - offsetY + imageView.getTop();
                piece.pieceWidth = pieceWidth + offsetX;
                piece.pieceHeight = pieceHeight + offsetY;

                Bitmap puzzlePiece = Bitmap.createBitmap(pieceWidth + offsetX, pieceHeight + offsetY, Bitmap.Config.ARGB_8888);
                int bumpSize = pieceHeight / 4;
                Canvas canvas = new Canvas(puzzlePiece);
                Path path = new Path();
                path.moveTo(offsetX, offsetY);

                // drawing typical jigsaw borders around pieces
                if (row != 0) {
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3f, offsetY);
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6f, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6f * 5, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3f * 2, offsetY);
                }
                path.lineTo(pieceBitmap.getWidth(), offsetY);

                if (col != cols - 1) {
                    path.lineTo(pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3f);
                    path.cubicTo(pieceBitmap.getWidth() - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6f, pieceBitmap.getWidth() - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6f * 5, pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3f * 2);
                }
                path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());

                if (row != rows - 1) {
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3f * 2, pieceBitmap.getHeight());
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6f * 5, pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6f, pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3f, pieceBitmap.getHeight());
                }
                path.lineTo(offsetX, pieceBitmap.getHeight());

                if (col != 0) {
                    path.lineTo(offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3f * 2);
                    path.cubicTo(offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6f * 5, offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6f, offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3f);
                }
                path.close();

                Paint paint = new Paint();
                paint.setColor(0XFF000000);
                paint.setStyle(Paint.Style.FILL);

                canvas.drawPath(path, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(pieceBitmap, 0, 0, paint);

                // Borders around piece
                Paint border = new Paint();
                border.setColor(0X80FFFFFF);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(8.0f);
                canvas.drawPath(path, border);

                border = new Paint();
                border.setColor(0X80000000);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(5.0f);
                canvas.drawPath(path, border);

                piece.setImageBitmap(puzzlePiece);

                // add piece to array
                pieces.add(piece);
                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }

        return pieces;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game_puzzle;
    }

    private int[] getBitmapPositionInsideImageView(ImageView imageView) {
        // calculate scaled img dimension and position
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (imgViewH - actH) / 2;
        int left = (imgViewW - actW) / 2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }

    public void checkGameOver() {
        if (isGameOver()) {
            isTimerRunning = false;
            popup.showWithButtonText(PopupType.POSITIVE, getString(R.string.popup_btn_finished), getString(R.string.popup_puzzle_won_text, time));
        }
    }

    private boolean isGameOver() {
        for (PuzzlePiece piece : pieces) {
            if (piece.canMove) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (type == PopupType.POSITIVE) {
            // close game on gameover
            onSuccess();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        // displaying stopwatch
        isTimerRunning = true;
        {
            final TextView timeView = findViewById(R.id.time_view);

            final Handler handler_ = new Handler(getMainLooper());

            handler_.post(new Runnable() {
                int seconds = 0;

                @Override
                public void run() {
                    int hours = seconds / 3600;
                    int minutes = ((seconds % 3600) / 60) + hours * 60;
                    int secs = seconds % 60;

                    time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);

                    timeView.setText(time);
                    if (isTimerRunning) {
                        seconds++;
                    }

                    handler_.postDelayed(this, 1000);
                }
            });
        }
    }
}


