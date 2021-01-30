package de.lmu.treeapp.wantedPoster.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.*;

import androidx.constraintlayout.widget.Guideline;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.database.entities.content.WantedPoster;
import de.lmu.treeapp.contentData.database.entities.content.WantedPosterImage;
import de.lmu.treeapp.wantedPoster.WantedPosterTab;
import de.lmu.treeapp.wantedPoster.WantedPosterTextType;

import java.util.List;

public class AgeInfoView extends LinearLayout {

    private final TextView pageTitle;
    private final TextView description;
    private final ImageView ageImage;
    private final ImageButton ageButton;
    private final ImageButton woodButton;
    private final ImageButton colorButton;

    private String title;
    private String placeholder;
    private String ageInfo;
    private String woodInfo;
    private String colorInfo;
    private String imageName;

    public AgeInfoView(Context context) {
        super(context);
    }

    public AgeInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AgeInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setWillNotDraw(false);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        inflate(getContext(), R.layout.view_age_info, this);

        pageTitle = findViewById(R.id.age_page_title);
        description = findViewById(R.id.description);
        ageImage = findViewById(R.id.age_image);
        ageButton = findViewById(R.id.age_button);
        woodButton = findViewById(R.id.wood_button);
        colorButton = findViewById(R.id.color_button);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setAgeInfo(Context context, List<WantedPoster> wpList,
                           List<WantedPosterImage> wpiList, Integer buttonActiveId,
                           Integer buttonInactiveId, Integer questionMarkActiveId, Integer questionMarkInactiveId) {

        for (WantedPoster wp : wpList) {
            if (wp.tab == WantedPosterTab.WOOD) {
                if (wp.name == WantedPosterTextType.TITLE) {
                    title = wp.description;
                } else if (wp.name == WantedPosterTextType.WOOD_AGE) {
                    ageInfo = wp.description;
                } else if (wp.name == WantedPosterTextType.WOOD_COLOR) {
                    colorInfo = wp.description;
                } else if (wp.name == WantedPosterTextType.WOOD_INFO) {
                    woodInfo = wp.description;
                }
            }
            for (WantedPosterImage wpi : wpiList) {
                if (wpi.tab == WantedPosterTab.WOOD) {
                    imageName = wpi.imageResource;
                    break;
                }
            }
            placeholder = getResources().getString(R.string.wanted_poster_description);


            // set card content and make buttons clickable
            pageTitle.setText(title);
            int imageId = context.getApplicationContext().getResources().getIdentifier(imageName,
                    "drawable", context.getApplicationContext().getPackageName());
            ageImage.setImageResource(imageId);
            ageImage.setOnClickListener(view -> {
                ageButton.setBackgroundResource(buttonInactiveId);
                woodButton.setBackgroundResource(questionMarkInactiveId);
                colorButton.setBackgroundResource(buttonInactiveId);
                description.setText(placeholder);
                description.scrollTo(0, 0);
            });
            ageButton.setOnClickListener(view -> {
                ageButton.setBackgroundResource(buttonActiveId);
                woodButton.setBackgroundResource(questionMarkInactiveId);
                colorButton.setBackgroundResource(buttonInactiveId);
                description.setText(ageInfo);
                description.scrollTo(0, 0);
            });
            woodButton.setOnClickListener(view -> {
                ageButton.setBackgroundResource(buttonInactiveId);
                woodButton.setBackgroundResource(questionMarkActiveId);
                colorButton.setBackgroundResource(buttonInactiveId);
                description.setText(woodInfo);
                description.scrollTo(0, 0);
            });
            colorButton.setOnClickListener(view -> {
                ageButton.setBackgroundResource(buttonInactiveId);
                woodButton.setBackgroundResource(questionMarkInactiveId);
                colorButton.setBackgroundResource(buttonActiveId);
                description.setText(colorInfo);
                description.scrollTo(0, 0);
            });
            description.setMovementMethod(new ScrollingMovementMethod());

            // move buttons to correct position for every tree
            int treeID = wpList.get(0).treeId;
            Guideline ageTop = findViewById(R.id.age_guideline_top);
            Guideline ageLeft = findViewById(R.id.age_guideline_left);
            Guideline colorTop = findViewById(R.id.color_guideline_top);
            Guideline colorLeft = findViewById(R.id.color_guideline_left);
            switch (treeID) {
                case 1: //Buche
                    ageTop.setGuidelinePercent(0.6f);
                    ageLeft.setGuidelinePercent(0.22f);
                    colorTop.setGuidelinePercent(0.4f);
                    colorLeft.setGuidelinePercent(0.6f);
                    break;
                case 2: //Linde
                    ageTop.setGuidelinePercent(0.15f);
                    ageLeft.setGuidelinePercent(0.12f);
                    colorTop.setGuidelinePercent(0.45f);
                    colorLeft.setGuidelinePercent(0.6f);
                    break;
                case 3: //Kiefer
                    ageTop.setGuidelinePercent(0.15f);
                    ageLeft.setGuidelinePercent(0.1f);
                    colorTop.setGuidelinePercent(0.5f);
                    colorLeft.setGuidelinePercent(0.6f);
                    break;
                case 4: //Eiche
                    ageTop.setGuidelinePercent(0.15f);
                    ageLeft.setGuidelinePercent(0f);
                    colorTop.setGuidelinePercent(0.65f);
                    colorLeft.setGuidelinePercent(0.45f);
                    break;
                case 5: //Hasel
                    ageTop.setGuidelinePercent(0.4f);
                    ageLeft.setGuidelinePercent(0.2f);
                    colorTop.setGuidelinePercent(0.5f);
                    colorLeft.setGuidelinePercent(0.57f);
                    break;
                case 6: //Birke
                    ageTop.setGuidelinePercent(0.4f);
                    ageLeft.setGuidelinePercent(0.2f);
                    colorTop.setGuidelinePercent(0.5f);
                    colorLeft.setGuidelinePercent(0.6f);
                    break;
                case 7: //Eberesche
                    ageTop.setGuidelinePercent(0.18f);
                    ageLeft.setGuidelinePercent(0.35f);
                    colorTop.setGuidelinePercent(0.5f);
                    colorLeft.setGuidelinePercent(0.6f);
                    break;
                case 8: //Tanne
                    ageTop.setGuidelinePercent(0.22f);
                    ageLeft.setGuidelinePercent(0.15f);
                    colorTop.setGuidelinePercent(0.45f);
                    colorLeft.setGuidelinePercent(0.6f);
                    break;
                case 9: //Fichte
                    ageTop.setGuidelinePercent(0.33f);
                    ageLeft.setGuidelinePercent(0.13f);
                    colorTop.setGuidelinePercent(0.65f);
                    colorLeft.setGuidelinePercent(0.35f);
                    break;
                default: //Ahorn
                    ageTop.setGuidelinePercent(0.33f);
                    ageLeft.setGuidelinePercent(0.11f);
                    colorTop.setGuidelinePercent(0.6f);
                    colorLeft.setGuidelinePercent(0.65f);
                    break;
            }
        }
    }

    public void resetAgeView(Integer buttonInactiveId, Integer questionMarkInactiveId) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            // Wait until click is performed
            ageButton.setBackgroundResource(buttonInactiveId);
            woodButton.setBackgroundResource(questionMarkInactiveId);
            colorButton.setBackgroundResource(buttonInactiveId);
            description.setText(placeholder);
            description.scrollTo(0, 0);
        }, 1500);

    }
}