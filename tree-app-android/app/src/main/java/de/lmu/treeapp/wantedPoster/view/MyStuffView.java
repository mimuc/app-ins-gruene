package de.lmu.treeapp.wantedPoster.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.database.entities.app.GameStateDescription;
import de.lmu.treeapp.contentData.database.entities.app.GameStateInputString;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;

import java.io.File;
import java.util.List;

public class MyStuffView extends LinearLayout {

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
    private final ConstraintLayout layout1;
    private final ConstraintLayout layout2;
    private final ConstraintLayout layout3;
    private final ConstraintLayout layout4;
    private final LinearLayout myStuff;
    private final ConstraintLayout ideasLayout;
    private final TextView textCloud1;
    private final TextView textCloud2;
    private boolean locked;

    private final ConstraintLayout popupLayout;
    private final ImageView pictureBig;

    public MyStuffView(Context context) {
        super(context);
    }

    public MyStuffView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyStuffView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setWillNotDraw(false);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        inflate(getContext(), R.layout.view_my_stuff, this);

        pageTitle = findViewById(R.id.page_title);
        lockedLayout = findViewById(R.id.locked_layout);
        lockedPicture = findViewById(R.id.my_stuff_locked);
        noPictureText = findViewById(R.id.noPicture_text);
        takenTreePicture1 = findViewById(R.id.tree_image_1);
        takenTreePicture2 = findViewById(R.id.tree_image_2);
        takenTreePicture3 = findViewById(R.id.tree_image_3);
        takenTreePicture4 = findViewById(R.id.tree_image_4);
        noImage1 = findViewById(R.id.noImage1);
        noImage2 = findViewById(R.id.noImage2);
        noImage3 = findViewById(R.id.noImage3);
        noImage4 = findViewById(R.id.noImage4);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);
        myStuff = findViewById(R.id.linearLayoutMyStuff);
        ideasLayout = findViewById(R.id.ideas_layout);
        textCloud1 = findViewById(R.id.text_cloud1);
        textCloud2 = findViewById(R.id.text_cloud2);

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

    private void closeIdeas() {
        setVisibility(View.VISIBLE, lockedLayout, layout1, layout2, layout3, layout4);
        setVisibility(View.GONE, ideasLayout);
    }

    public void setMyStuff(Context context, int treeId,
                           List<GameStateTakePictureImage> takenPictureImages,
                           List<GameStateInputString> treeInputStrings,
                           List<GameStateDescription> treeDescriptions) {
        pageTitle.setText(R.string.wanted_poster_my_stuff);
        myStuff.setOnClickListener(view -> {
            setVisibility(View.GONE, lockedLayout, layout1, layout2, layout3, layout4);
            setVisibility(View.VISIBLE, ideasLayout);
            if (treeInputStrings.size() != 0) {
                for (GameStateInputString inputString : treeInputStrings) {
                    if (inputString.treeId == treeId) {
                        // context is required because otherwise the string is only displayed as an integer
                        String rhymeDesc = context.getResources().getString(R.string.wanted_poster_rhyme_cloud)
                                + inputString.inputString;
                        textCloud1.setText(rhymeDesc);
                    }
                }
                for (GameStateDescription treeDescription : treeDescriptions) {
                    if (treeDescription.treeId == treeId) {
                        String treeDesc = context.getResources().getString(R.string.wanted_poster_tree_description)
                                + treeDescription.description;
                        textCloud2.setText(treeDesc);
                    }
                }
            }
            ideasLayout.setOnClickListener(close -> closeIdeas());
            textCloud1.setOnClickListener(close -> closeIdeas());
            textCloud2.setOnClickListener(close -> closeIdeas());
        });
        textCloud1.setMovementMethod(new ScrollingMovementMethod());
        textCloud2.setMovementMethod(new ScrollingMovementMethod());

        if (takenPictureImages.size() != 0) {
            locked = false;
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
            locked = true;
        }
    }

    public static void setVisibility(int visibility, View... views) {
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }

    public void resetMyStuffView() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            // Wait until click is performed
            if (locked) {
                setVisibility(View.VISIBLE, lockedLayout, noPictureText, lockedPicture);
                lockedPicture.setImageResource(R.drawable.sb_nopictures);
            } else {
                setVisibility(View.VISIBLE, takenTreePicture1, takenTreePicture2,
                        takenTreePicture3, takenTreePicture4);
            }
            setVisibility(View.VISIBLE, myStuff, layout1, layout2, layout3, layout4);
            setVisibility(View.GONE, popupLayout, ideasLayout);
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
