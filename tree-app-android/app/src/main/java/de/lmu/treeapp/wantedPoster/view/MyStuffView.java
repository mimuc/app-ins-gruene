package de.lmu.treeapp.wantedPoster.view;

import android.content.Context;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.daos.app.GameStateTakePictureDao;
import de.lmu.treeapp.contentData.database.entities.app.GameStateDescription;
import de.lmu.treeapp.contentData.database.entities.app.GameStateInputString;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureRelations;

public class MyStuffView extends LinearLayout {

    private final TextView pageTitle;
    private final ConstraintLayout lockedLayout;
    private final ImageView lockedPicture;
    private final TextView noPictureText;
    private final ImageView[] takenTreePictures;
    private final ConstraintLayout[] layouts;
    private final LinearLayout myStuff;
    private final ConstraintLayout ideasLayout;
    private final ConstraintLayout craftingLayout;
    private final TextView textCloud1;
    private final TextView textCloud2;
    private final ImageButton cameraButton;
    private final ImageButton writingButton;
    private final ImageButton craftingButton;
    private final ImageView pictureCrafting;
    private boolean locked;

    private final ConstraintLayout popupLayout;
    private final ImageView pictureBig;

    private Integer cameraActiveId;
    private Integer cameraInactiveId;
    private Integer writingActiveId;
    private Integer writingInactiveId;
    private Integer craftingActiveId;
    private Integer craftingInactiveId;

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
        craftingLayout = findViewById(R.id.crafting_layout);
        textCloud1 = findViewById(R.id.text_cloud1);
        textCloud2 = findViewById(R.id.text_cloud2);
        cameraButton = findViewById(R.id.camera_button);
        writingButton = findViewById(R.id.writing_button);
        craftingButton = findViewById(R.id.crafting_button);
        pictureCrafting = findViewById(R.id.picture_crafting);

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

    public void setMyStuff(Context context, int treeId,
                           List<GameStateInputString> treeInputStrings,
                           List<GameStateDescription> treeDescriptions,
                           List<String> imageStrings) {
        pageTitle.setText(R.string.wanted_poster_my_stuff);

        cameraInactiveId = context.getApplicationContext().getResources().getIdentifier(
                "sb_icon_camera", "drawable", context.getApplicationContext().getPackageName());
        cameraActiveId = context.getApplicationContext().getResources().getIdentifier(
                "sb_icon_cameraclicked", "drawable", context.getApplicationContext().getPackageName());
        writingInactiveId = context.getApplicationContext().getResources().getIdentifier(
                "sb_icon_writing", "drawable", context.getApplicationContext().getPackageName());
        writingActiveId = context.getApplicationContext().getResources().getIdentifier(
                "sb_icon_writingclicked", "drawable", context.getApplicationContext().getPackageName());
        craftingInactiveId = context.getApplicationContext().getResources().getIdentifier(
                "sb_icon_crafting", "drawable", context.getApplicationContext().getPackageName());
        craftingActiveId = context.getApplicationContext().getResources().getIdentifier(
                "sb_icon_craftingclicked", "drawable", context.getApplicationContext().getPackageName());

        writingButton.setOnClickListener(view -> {
            cameraButton.setBackgroundResource(cameraInactiveId);
            writingButton.setBackgroundResource(writingActiveId);
            craftingButton.setBackgroundResource(craftingInactiveId);
            setVisibility(View.GONE, craftingLayout, lockedLayout, layouts[0], layouts[1], layouts[2], layouts[3]);
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

        });
        textCloud1.setMovementMethod(new ScrollingMovementMethod());
        textCloud2.setMovementMethod(new ScrollingMovementMethod());

        DataManager.getInstance(getContext()).getGameStateList(treeId, GameStateTakePictureDao.class).subscribe(gameStateTakePictureRelations -> {

            GameStateTakePictureImage craftTaskPicture = GameStateTakePictureDao.getLatestCraftImage(gameStateTakePictureRelations);
            craftingButton.setOnClickListener(view -> {
                cameraButton.setBackgroundResource(cameraInactiveId);
                writingButton.setBackgroundResource(writingInactiveId);
                craftingButton.setBackgroundResource(craftingActiveId);
                if (craftTaskPicture == null) {
                    setVisibility(View.GONE, ideasLayout, craftingLayout, layouts[0], layouts[1], layouts[2], layouts[3]);
                    setVisibility(View.VISIBLE, lockedLayout);
                    lockedPicture.setImageResource(R.drawable.sb_crafting_symbol);
                    noPictureText.setText(getResources().getString(R.string.wanted_poster_no_crafting_pictures));
                } else {
                    setVisibility(View.GONE, lockedLayout, ideasLayout, layouts[0], layouts[1], layouts[2], layouts[3]);
                    setVisibility(View.VISIBLE, craftingLayout);
                    Uri img = setImage(craftTaskPicture.imagePath);
                    Glide.with(context).load(img).into(pictureCrafting);
                }
            });


            cameraButton.setBackgroundResource(cameraActiveId);
            writingButton.setBackgroundResource(writingInactiveId);
            craftingButton.setBackgroundResource(craftingInactiveId);
            if (gameStateTakePictureRelations.size() > 0) { // check here if only crafting image is set
                locked = false;
                setVisibility(View.GONE, ideasLayout, craftingLayout, lockedLayout);

                Tree.GameCategories[] validGameCategories = {
                        Tree.GameCategories.other,
                        Tree.GameCategories.leaf,
                        Tree.GameCategories.fruit,
                        Tree.GameCategories.trunk,
                };
                for (int i = 0; i < validGameCategories.length; i++) {
                    Tree.GameCategories gameCategory = validGameCategories[i];
                    GameStateTakePictureRelations gameStateTakePictureRelationsFiltered = GameStateTakePictureDao.filterImages(gameStateTakePictureRelations, gameCategory);
                    if (gameStateTakePictureRelationsFiltered != null && gameStateTakePictureRelationsFiltered.getSelectedImage() != null) {
                        takenTreePictures[i].setPadding(0, 0, 0, 0);
                        Uri img = setImage(gameStateTakePictureRelationsFiltered.getSelectedImage().imagePath);
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
                    } else {
                        //set Background if image is null
                        takenTreePictures[i].setImageResource(context.getApplicationContext().getResources().getIdentifier(imageStrings.get(i), "drawable", context.getApplicationContext().getPackageName()));
                        takenTreePictures[i].setAlpha(0.3f);
                    }
                }
            } else {
                setVisibility(View.VISIBLE, lockedLayout);
                setVisibility(View.GONE, ideasLayout, craftingLayout, layouts[0], layouts[1], layouts[2], layouts[3]);
                lockedPicture.setImageResource(R.drawable.sb_nopictures);
                noPictureText.setText(getResources().getString(R.string.wanted_poster_no_taken_pictures));
                locked = true;
            }

            cameraButton.setOnClickListener(view -> {
                cameraButton.setBackgroundResource(cameraActiveId);
                writingButton.setBackgroundResource(writingInactiveId);
                craftingButton.setBackgroundResource(craftingInactiveId);
                if (locked) {
                    setVisibility(View.GONE, ideasLayout, craftingLayout, layouts[0], layouts[1], layouts[2], layouts[3]);
                    setVisibility(View.VISIBLE, lockedLayout);
                    lockedPicture.setImageResource(R.drawable.sb_nopictures);
                    noPictureText.setText(getResources().getString(R.string.wanted_poster_no_taken_pictures));
                } else {
                    setVisibility(View.GONE, lockedLayout, ideasLayout, craftingLayout);
                    setVisibility(View.VISIBLE, layouts[0], layouts[1], layouts[2], layouts[3]);
                }
            });
        });
    }

    public static void setVisibility(int visibility, View... views) {
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }

    public void resetMyStuffView() {
        Handler handler = new Handler(Looper.getMainLooper());
        // Wait until click is performed
        handler.postDelayed(cameraButton::performClick, 1500);

    }

    public void performCraftingClick() {
        craftingButton.performClick();
    }

    /**
     * Gets the imagePath from room database of this tree component and return a URI image
     * of this tree component, in order to detect the correct rotation (contrary to a BitmapDrawable).
     *
     * @param imagePath String of the image-name (coming from room database, when user have taken
     *                  pic of the tree)
     * @return A URI of the image
     */
    private Uri setImage(String imagePath) {
        File imgFile = new File(imagePath);
        return Uri.fromFile(imgFile);
    }
}
