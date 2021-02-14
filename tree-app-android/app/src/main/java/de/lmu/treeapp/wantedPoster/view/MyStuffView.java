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

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.entities.app.GameStateDescription;
import de.lmu.treeapp.contentData.database.entities.app.GameStateInputString;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;

public class MyStuffView extends LinearLayout {

    private final TextView pageTitle;
    private final ConstraintLayout lockedLayout;
    private final ImageView lockedPicture;
    private final TextView noPictureText;
    private final ImageView[] takenTreePictures;
    private final ConstraintLayout[] layouts;
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
        takenTreePictures = new ImageView[4];
        takenTreePictures[0] = findViewById(R.id.tree_image_1);
        takenTreePictures[1] = findViewById(R.id.tree_image_2);
        takenTreePictures[2] = findViewById(R.id.tree_image_3);
        takenTreePictures[3] = findViewById(R.id.tree_image_4);
        layouts = new ConstraintLayout[4];
        layouts[0] = findViewById(R.id.layout1);
        layouts[1] = findViewById(R.id.layout2);
        layouts[2] = findViewById(R.id.layout3);
        layouts[3] = findViewById(R.id.layout4);
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
        setVisibility(View.VISIBLE, lockedLayout, layouts[0], layouts[1], layouts[2], layouts[3]);
        setVisibility(View.GONE, ideasLayout);
    }

    public void setMyStuff(Context context, int treeId,
                           List<GameStateTakePictureImage> takenPictureImages,
                           List<GameStateInputString> treeInputStrings,
                           List<GameStateDescription> treeDescriptions) {
        pageTitle.setText(R.string.wanted_poster_my_stuff);
        myStuff.setOnClickListener(view -> {
            setVisibility(View.GONE, lockedLayout, layouts[0], layouts[1], layouts[2], layouts[3]);
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

            Tree.GameCategories[] validGameCategories = {
                    Tree.GameCategories.leaf,
                    Tree.GameCategories.fruit,
                    Tree.GameCategories.trunk,
                    Tree.GameCategories.other
            };
            for (int i = 0; i < validGameCategories.length; i++) {
                Tree.GameCategories gameCategory = validGameCategories[i];
                GameStateTakePictureImage takePictureImage = GameStateTakePictureImage.getLatestTakePictureImage(takenPictureImages, gameCategory);
                if (takePictureImage != null) {
                    takenTreePictures[i].setPadding(0, 0, 0, 0);
                    Drawable img = setImage(takePictureImage.imagePath);
                    Glide.with(context).load(img).into(takenTreePictures[i]);
                    takenTreePictures[i].setOnClickListener(view -> {
                        setVisibility(View.GONE, myStuff,
                                takenTreePictures[0], takenTreePictures[1],
                                takenTreePictures[2], takenTreePictures[3]);
                        popupLayout.setVisibility(VISIBLE);
                        Glide.with(context).load(img).into(pictureBig);
                        popupLayout.setOnClickListener(viewClose -> {
                            setVisibility(View.VISIBLE, myStuff,
                                    takenTreePictures[0], takenTreePictures[1],
                                    takenTreePictures[2], takenTreePictures[3]);
                            popupLayout.setVisibility(View.GONE);
                        });
                    });
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
                setVisibility(View.VISIBLE,
                        takenTreePictures[0], takenTreePictures[1],
                        takenTreePictures[2], takenTreePictures[3]);
            }
            setVisibility(View.VISIBLE, myStuff, layouts[0], layouts[1], layouts[2], layouts[3]);
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
