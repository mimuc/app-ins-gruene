package de.lmu.treeapp.wantedPoster.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;

import java.io.File;
import java.util.List;

public class GalleryView extends LinearLayout {

    private final TextView pageTitle;
    private final ConstraintLayout lockedLayout;
    private final ImageView lockedPicture;
    private final TextView noPictureText;
    private final ImageView takenTreePicture1;
    private final ImageView takenTreePicture2;
    private final ImageView takenTreePicture3;
    private final ImageView takenTreePicture4;
    private final ImageView noImage1;
    private final ImageView noImage2;
    private final ImageView noImage3;
    private final ImageView noImage4;
    private final LinearLayout myStuff;

    private final ConstraintLayout popupLayout;
    private final ImageView pictureBig;

    public GalleryView(Context context) {
        super(context);
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setWillNotDraw(false);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        inflate(getContext(), R.layout.view_gallery, this);

        pageTitle = findViewById(R.id.page_title);
        lockedLayout = findViewById(R.id.locked_layout);
        lockedPicture = findViewById(R.id.gallery_locked);
        noPictureText = findViewById(R.id.noPicture_text);
        takenTreePicture1 = findViewById(R.id.tree_image_1);
        takenTreePicture2 = findViewById(R.id.tree_image_2);
        takenTreePicture3 = findViewById(R.id.tree_image_3);
        takenTreePicture4 = findViewById(R.id.tree_image_4);
        noImage1 = findViewById(R.id.noImage1);
        noImage2 = findViewById(R.id.noImage2);
        noImage3 = findViewById(R.id.noImage3);
        noImage4 = findViewById(R.id.noImage4);
        myStuff = findViewById(R.id.linearLayoutmyStuff);

        popupLayout = findViewById(R.id.popup_layout);
        pictureBig = findViewById(R.id.picture_big);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setGallery(List<GameStateTakePictureImage> takenPictureImages) {
        //TODO: Pop-up für Reime und Adjektive, wenn man auf "Meine Ideen" klickt.
        pageTitle.setText(R.string.wanted_poster_gallery);
        if (takenPictureImages.size() != 0) {

            setVisibility(View.GONE, lockedLayout, noPictureText, lockedPicture);
            noImage1.setImageResource(R.drawable.sb_nopictures);
            noImage2.setImageResource(R.drawable.sb_nopictures);
            noImage3.setImageResource(R.drawable.sb_nopictures);
            noImage4.setImageResource(R.drawable.sb_nopictures);

            for (GameStateTakePictureImage takenImg : takenPictureImages) {
                switch (takenImg.gameId) {
                    case 300:
                        noImage2.setVisibility(GONE);
                        Drawable img2 = setImage(takenImg.imagePath);
                        takenTreePicture2.setRotation(-90);
                        takenTreePicture2.setImageDrawable(img2);
                        takenTreePicture2.setOnClickListener(view -> {
                            setVisibility(View.GONE, takenTreePicture1, takenTreePicture2,
                                    takenTreePicture3, takenTreePicture4, myStuff);
                            popupLayout.setVisibility(VISIBLE);
                            pictureBig.setRotation(-90);
                            pictureBig.setImageDrawable(img2);
                            popupLayout.setOnClickListener(viewClose -> {
                                setVisibility(View.VISIBLE, takenTreePicture1, takenTreePicture2,
                                        takenTreePicture3, takenTreePicture4, myStuff);
                                popupLayout.setVisibility(View.GONE);
                            });
                        });
                        break;
                    case 301:
                        noImage3.setVisibility(GONE);
                        Drawable img3 = setImage(takenImg.imagePath);
                        takenTreePicture3.setRotation(-90);
                        takenTreePicture3.setImageDrawable(img3);
                        takenTreePicture3.setOnClickListener(view -> {
                            setVisibility(View.GONE, takenTreePicture1, takenTreePicture2,
                                    takenTreePicture3, takenTreePicture4, myStuff);
                            popupLayout.setVisibility(VISIBLE);
                            pictureBig.setRotation(-90);
                            pictureBig.setImageDrawable(img3);
                            popupLayout.setOnClickListener(viewClose -> {
                                setVisibility(View.VISIBLE, takenTreePicture1, takenTreePicture2,
                                        takenTreePicture3, takenTreePicture4, myStuff);
                                popupLayout.setVisibility(View.GONE);
                            });
                        });
                        break;
                    case 302:
                        noImage4.setVisibility(GONE);
                        Drawable img4 = setImage(takenImg.imagePath);
                        takenTreePicture4.setRotation(-90);
                        takenTreePicture4.setImageDrawable(img4);
                        takenTreePicture4.setOnClickListener(view -> {
                            setVisibility(View.GONE, takenTreePicture1, takenTreePicture2,
                                    takenTreePicture3, takenTreePicture4, myStuff);
                            popupLayout.setVisibility(VISIBLE);
                            pictureBig.setRotation(-90);
                            pictureBig.setImageDrawable(img4);
                            popupLayout.setOnClickListener(viewClose -> {
                                setVisibility(View.VISIBLE, takenTreePicture1, takenTreePicture2,
                                        takenTreePicture3, takenTreePicture4, myStuff);
                                popupLayout.setVisibility(View.GONE);
                            });
                        });
                        break;
                    case 303:
                        noImage1.setVisibility(GONE);
                        Drawable img1 = setImage(takenImg.imagePath);
                        takenTreePicture1.setRotation(-90);
                        takenTreePicture1.setImageDrawable(img1);
                        takenTreePicture1.setOnClickListener(view -> {
                            setVisibility(View.GONE, takenTreePicture1, takenTreePicture2,
                                    takenTreePicture3, takenTreePicture4, myStuff);
                            popupLayout.setVisibility(VISIBLE);
                            pictureBig.setRotation(-90);
                            pictureBig.setImageDrawable(img1);
                            popupLayout.setOnClickListener(viewClose -> {
                                setVisibility(View.VISIBLE, takenTreePicture1, takenTreePicture2,
                                        takenTreePicture3, takenTreePicture4, myStuff);
                                popupLayout.setVisibility(View.GONE);
                            });
                        });
                        break;
                }
            }
        } else {
            setVisibility(View.VISIBLE, lockedLayout, noPictureText, lockedPicture);
            lockedPicture.setImageResource(R.drawable.sb_nopictures);
        }
    }

    public static void setVisibility(int visibility, View... views) {
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }

    public void resetGalleryView() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            // Wait until click is performed
            setVisibility(View.VISIBLE, takenTreePicture1, takenTreePicture2,
                    takenTreePicture3, takenTreePicture4, myStuff);
            popupLayout.setVisibility(View.GONE);
        }, 1500);

    }

    /**
     * Gets the imagePath from room database of this tree component and return a drawable image
     * of this tree component.
     *
     * @param imagePath String of the image-name (coming from room database, when user have taken
     *                  pic of the tree)
     * @return A Drawable of the image
     */
    private BitmapDrawable setImage(String imagePath) {
        File imgFile = new File(imagePath);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        return new BitmapDrawable(getResources(), myBitmap);
    }
}
