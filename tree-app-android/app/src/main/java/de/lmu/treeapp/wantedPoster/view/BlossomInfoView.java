package de.lmu.treeapp.wantedPoster.view;

import android.content.Context;
import android.graphics.Canvas;
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
import androidx.constraintlayout.widget.Guideline;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.database.entities.content.WantedPoster;
import de.lmu.treeapp.contentData.database.entities.content.WantedPosterImage;
import de.lmu.treeapp.wantedPoster.WantedPosterImageType;
import de.lmu.treeapp.wantedPoster.WantedPosterTab;
import de.lmu.treeapp.wantedPoster.WantedPosterTextType;

import java.util.List;

public class BlossomInfoView extends LinearLayout {

    private final TextView pageTitle;
    private final TextView description;
    private final ImageView blossomImage;
    private final ImageButton timeButton;
    private final ImageButton cameraButton;
    private final ImageButton symbolButton;

    private String title;
    private String placeholder;
    private String blossomTime;
    private String symbolInfo;
    private String graphic;
    private int treeID;

    // Pop-up Content
    private final ImageButton closeCameraView;
    private final LinearLayout description_Layout;
    private final ConstraintLayout popupLayout;
    private final ImageView picture;
    private final TextView picturetext;
    private final LinearLayout popupLayoutMW;
    private final ImageView pictureM;
    private final TextView picturetextM;
    private final ImageView pictureW;
    private final TextView picturetextW;
    private String imageInfo = "noImageInfo";
    private String maleImageInfo = "noImageInfo";
    private String femaleImageInfo = "noImageInfo";
    private String popUpImage = "noImage";
    private String malepopUpImage = "noImage";
    private String femalepopUpImage = "noImage";

    public BlossomInfoView(Context context) {
        super(context);
    }

    public BlossomInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlossomInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setWillNotDraw(false);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        inflate(getContext(), R.layout.view_blossom_info, this);

        pageTitle = findViewById(R.id.page_title);
        description = findViewById(R.id.description);
        blossomImage = findViewById(R.id.blossom_image);
        timeButton = findViewById(R.id.time_button);
        cameraButton = findViewById(R.id.camera_button);
        symbolButton = findViewById(R.id.symbol_button);

        closeCameraView = findViewById(R.id.closeCameraView);
        description_Layout = findViewById(R.id.linearLayout3);
        popupLayout = findViewById(R.id.popup_layout);
        picture = findViewById(R.id.picture);
        picturetext = findViewById(R.id.picturetext);
        popupLayoutMW = findViewById(R.id.popup_layout_mw);
        pictureM = findViewById(R.id.picture_m);
        picturetextM = findViewById(R.id.picturetext_m);
        pictureW = findViewById(R.id.picture_w);
        picturetextW = findViewById(R.id.picturetext_w);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setBlossomInfo(Context context, List<WantedPoster> wpList,
                               List<WantedPosterImage> wpiList, Integer buttonActiveId,
                               Integer buttonInactiveId, Integer cameraActiveId, Integer cameraInactiveId) {

        int imageIdtest = context.getApplicationContext().getResources().getIdentifier("sb_bluete_foto_ahorn",
                "drawable", context.getApplicationContext().getPackageName());
        picture.setImageResource(imageIdtest);

        // Save data from database in local variables
        for (WantedPoster wp : wpList) {
            if (wp.tab == WantedPosterTab.BLOSSOM) {
                if (wp.name == WantedPosterTextType.TITLE) {
                    title = wp.description;
                } else if (wp.name == WantedPosterTextType.BLOOM) {
                    blossomTime = wp.description;
                } else if (wp.name == WantedPosterTextType.SYMBOL) {
                    symbolInfo = wp.description;
                } else if (wp.name == WantedPosterTextType.PHOTO) {
                    imageInfo = wp.description;
                } else if (wp.name == WantedPosterTextType.PHOTO_M) {
                    maleImageInfo = wp.description;
                } else if (wp.name == WantedPosterTextType.PHOTO_W) {
                    femaleImageInfo = wp.description;
                }
            } else if (wp.tab == WantedPosterTab.ALL && wp.name == WantedPosterTextType.STANDARD) {
                placeholder = wp.description;
            }
        }
        for (WantedPosterImage wpi : wpiList) {
            if (wpi.tab == WantedPosterTab.BLOSSOM) {
                if (wpi.usage == WantedPosterImageType.GRAPHIC_0) {
                    graphic = wpi.imageResource;
                } else if (wpi.usage == WantedPosterImageType.PHOTO) {
                    popUpImage = wpi.imageResource;
                } else if (wpi.usage == WantedPosterImageType.PHOTO_M) {
                    malepopUpImage = wpi.imageResource;
                } else if (wpi.usage == WantedPosterImageType.PHOTO_W) {
                    femalepopUpImage = wpi.imageResource;
                }
            }
        }

        // set card content and make buttons clickable
        treeID = wpList.get(0).treeId;
        pageTitle.setText(title);
        int imageId = context.getApplicationContext().getResources().getIdentifier(graphic,
                "drawable", context.getApplicationContext().getPackageName());
        blossomImage.setImageResource(imageId);
        blossomImage.setOnClickListener(view -> {
            if (treeID == 1 || treeID == 4) {
                symbolButton.setVisibility(GONE);
            }
            timeButton.setBackgroundResource(buttonInactiveId);
            cameraButton.setBackgroundResource(cameraInactiveId);
            symbolButton.setBackgroundResource(buttonInactiveId);
            description.setText(placeholder);
            description.scrollTo(0, 0);
        });
        timeButton.setOnClickListener(view -> {
            description.setText(blossomTime);
            timeButton.setBackgroundResource(buttonActiveId);
            cameraButton.setBackgroundResource(cameraInactiveId);
            symbolButton.setBackgroundResource(buttonInactiveId);
            description.scrollTo(0, 0);
        });
        symbolButton.setOnClickListener(view -> {
            description.setText(symbolInfo);
            timeButton.setBackgroundResource(buttonInactiveId);
            cameraButton.setBackgroundResource(cameraInactiveId);
            symbolButton.setBackgroundResource(buttonActiveId);
            description.scrollTo(0, 0);
        });
        description.setMovementMethod(new ScrollingMovementMethod());

        // move buttons to correct position for every tree
        Guideline timeTop = findViewById(R.id.time_guideline_top);
        Guideline timeLeft = findViewById(R.id.time_guideline_left);
        Guideline symbolBottom = findViewById(R.id.symbol_guideline_bottom);
        Guideline symbolRight = findViewById(R.id.symbol_guideline_right);
        switch (treeID) {
            case 1: //Buche
                symbolButton.setVisibility(View.GONE);
                timeTop.setGuidelinePercent(0.45f);
                timeLeft.setGuidelinePercent(0.5f);
                break;
            case 2: //Linde
                timeTop.setGuidelinePercent(0.53f);
                timeLeft.setGuidelinePercent(0.23f);
                symbolBottom.setGuidelinePercent(0.98f);
                symbolRight.setGuidelinePercent(1f);
                break;
            case 3: //Kiefer
                timeTop.setGuidelinePercent(0.45f);
                timeLeft.setGuidelinePercent(0.6f);
                symbolBottom.setGuidelinePercent(0.91f);
                symbolRight.setGuidelinePercent(0.97f);
                break;
            case 4: //Eiche
                symbolButton.setVisibility(View.GONE);
                timeTop.setGuidelinePercent(0.45f);
                timeLeft.setGuidelinePercent(0.48f);
                break;
            case 5: //Hasel
                timeTop.setGuidelinePercent(0.45f);
                timeLeft.setGuidelinePercent(0.6f);
                symbolBottom.setGuidelinePercent(0.99f);
                symbolRight.setGuidelinePercent(0.73f);
                break;
            case 6: //Birke
                timeTop.setGuidelinePercent(0.27f);
                timeLeft.setGuidelinePercent(0.58f);
                symbolBottom.setGuidelinePercent(0.97f);
                symbolRight.setGuidelinePercent(0.92f);
                break;
            case 7: //Eberesche
                timeTop.setGuidelinePercent(0.57f);
                timeLeft.setGuidelinePercent(0.25f);
                symbolBottom.setGuidelinePercent(0.88f);
                symbolRight.setGuidelinePercent(0.97f);
                break;
            case 8: //Tanne
                timeTop.setGuidelinePercent(0.3f);
                timeLeft.setGuidelinePercent(0.58f);
                symbolBottom.setGuidelinePercent(1f);
                symbolRight.setGuidelinePercent(0.99f);
                break;
            case 9: //Fichte
                timeTop.setGuidelinePercent(0.45f);
                timeLeft.setGuidelinePercent(0.6f);
                symbolBottom.setGuidelinePercent(0.97f);
                symbolRight.setGuidelinePercent(0.93f);
                break;
            default: //Ahorn
                timeTop.setGuidelinePercent(0.5f);
                timeLeft.setGuidelinePercent(0.37f);
                symbolBottom.setGuidelinePercent(0.94f);
                symbolRight.setGuidelinePercent(0.91f);
                break;
        }


        // set Images for Pop-up view
        // Pop-up view with one image
        if (maleImageInfo.equalsIgnoreCase("noImageInfo")) {
            int popupimageId = context.getApplicationContext().getResources().getIdentifier(popUpImage,
                    "drawable", context.getApplicationContext().getPackageName());
            picture.setImageResource(popupimageId);
            picturetext.setText(imageInfo);
        }
        // Pop-up view with two images
        else if (imageInfo.equalsIgnoreCase("noImageInfo")) {
            int malepopupimageId = context.getApplicationContext().getResources().getIdentifier(malepopUpImage,
                    "drawable", context.getApplicationContext().getPackageName());
            pictureM.setImageResource(malepopupimageId);
            picturetextM.setText(maleImageInfo);
            int femalepopupimageId = context.getApplicationContext().getResources().getIdentifier(femalepopUpImage,
                    "drawable", context.getApplicationContext().getPackageName());
            pictureW.setImageResource(femalepopupimageId);
            picturetextW.setText(femaleImageInfo);
        }

        cameraButton.setOnClickListener(view -> {
            // Pop-up view with one image
            if (maleImageInfo.equalsIgnoreCase("noImageInfo")) {
                popupLayout.setVisibility(VISIBLE);
            }
            // Pop-up view with two images
            else if (imageInfo.equalsIgnoreCase("noImageInfo")) {
                popupLayoutMW.setVisibility(VISIBLE);
            }
            description_Layout.setVisibility(GONE);
            symbolButton.setVisibility(GONE);
            timeButton.setVisibility(GONE);
            blossomImage.setAlpha(0f);
            cameraButton.setBackgroundResource(cameraActiveId);
            closeCameraView.setVisibility(VISIBLE);

            closeCameraView.setOnClickListener(viewCamera -> {
                closeCameraView.setVisibility(GONE);
                description_Layout.setVisibility(VISIBLE);
                symbolButton.setVisibility(VISIBLE);
                timeButton.setVisibility(VISIBLE);
                blossomImage.setAlpha(1f);
                // Pop-up view with one image
                if (maleImageInfo.equalsIgnoreCase("noImageInfo")) {
                    popupLayout.setVisibility(GONE);
                }
                // Pop-up view with two images
                else if (imageInfo.equalsIgnoreCase("noImageInfo")) {
                    popupLayoutMW.setVisibility(GONE);
                }
                blossomImage.performClick();
            });
        });
    }

    public void resetBlossomView(Integer buttonInactiveId, Integer cameraInactiveId) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            // Wait until click is performed
            blossomImage.setAlpha(1f);
            closeCameraView.setVisibility(GONE);
            popupLayout.setVisibility(GONE);
            popupLayoutMW.setVisibility(GONE);
            description_Layout.setVisibility(VISIBLE);
            symbolButton.setVisibility(VISIBLE);
            timeButton.setVisibility(VISIBLE);

            if (treeID == 1 || treeID == 4) {
                symbolButton.setVisibility(GONE);
            }
            timeButton.setBackgroundResource(buttonInactiveId);
            cameraButton.setBackgroundResource(cameraInactiveId);
            symbolButton.setBackgroundResource(buttonInactiveId);
            description.setText(placeholder);
            description.scrollTo(0, 0);
        }, 1500);

    }
}