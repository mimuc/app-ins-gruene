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

import androidx.constraintlayout.widget.Guideline;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.database.entities.content.WantedPoster;
import de.lmu.treeapp.contentData.database.entities.content.WantedPosterImage;
import de.lmu.treeapp.wantedPoster.WantedPosterTab;
import de.lmu.treeapp.wantedPoster.WantedPosterTextType;

public class TreeDetailInfoView extends LinearLayout {

    private final TextView pageTitle;
    private final TextView description;
    private final ImageView generalImage;
    private final ImageButton treeButton;
    private final ImageButton backgroundButton;
    private final ImageButton symbolButton;

    private String title;
    private String placeholder;
    private String magnifierTree;
    private String magnifierBackground;
    private String magnifierSymbol;
    private String imageName;

    public TreeDetailInfoView(Context context) {
        super(context);
    }

    public TreeDetailInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TreeDetailInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setWillNotDraw(false);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        inflate(getContext(), R.layout.view_tree_detail_info, this);

        pageTitle = findViewById(R.id.page_title);
        description = findViewById(R.id.description);
        generalImage = findViewById(R.id.general_image);
        treeButton = findViewById(R.id.tree_button);
        backgroundButton = findViewById(R.id.background_button);
        symbolButton = findViewById(R.id.symbol_button);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setTreeDetailInfo(Context context, List<WantedPoster> wpList,
                                  List<WantedPosterImage> wpiList, Integer buttonActiveId,
                                  Integer buttonInactiveId) {
        // Save data from database in local variables
        for (WantedPoster wp : wpList) {
            if (wp.tab == WantedPosterTab.GENERAL) {
                if (wp.name == WantedPosterTextType.TITLE) {
                    title = wp.description;
                } else if (wp.name == WantedPosterTextType.HABITAT) {
                    magnifierBackground = wp.description;
                } else if (wp.name == WantedPosterTextType.SYMBOL) {
                    magnifierSymbol = wp.description;
                } else if (wp.name == WantedPosterTextType.TREE) {
                    magnifierTree = wp.description;
                }
            } else if (wp.tab == WantedPosterTab.ALL && wp.name == WantedPosterTextType.STANDARD) {
                placeholder = wp.description;
            }
        }
        for (WantedPosterImage wpi : wpiList) {
            if (wpi.tab == WantedPosterTab.GENERAL) {
                imageName = wpi.imageResource;
            }
        }

        // set card content and make buttons clickable
        pageTitle.setText(title);
        int imageId = context.getApplicationContext().getResources().getIdentifier(imageName,
                "drawable", context.getApplicationContext().getPackageName());
        generalImage.setImageResource(imageId);
        generalImage.setOnClickListener(view -> {
            treeButton.setBackgroundResource(buttonInactiveId);
            backgroundButton.setBackgroundResource(buttonInactiveId);
            symbolButton.setBackgroundResource(buttonInactiveId);
            description.setText(placeholder);
            description.scrollTo(0, 0);
        });
        treeButton.setOnClickListener(view -> {
            description.setText(magnifierTree);
            treeButton.setBackgroundResource(buttonActiveId);
            backgroundButton.setBackgroundResource(buttonInactiveId);
            symbolButton.setBackgroundResource(buttonInactiveId);
            description.scrollTo(0, 0);
        });
        backgroundButton.setOnClickListener(view -> {
            description.setText(magnifierBackground);
            treeButton.setBackgroundResource(buttonInactiveId);
            backgroundButton.setBackgroundResource(buttonActiveId);
            symbolButton.setBackgroundResource(buttonInactiveId);
            description.scrollTo(0, 0);
        });
        symbolButton.setOnClickListener(view -> {
            description.setText(magnifierSymbol);
            treeButton.setBackgroundResource(buttonInactiveId);
            backgroundButton.setBackgroundResource(buttonInactiveId);
            symbolButton.setBackgroundResource(buttonActiveId);
            description.scrollTo(0, 0);
        });
        description.setMovementMethod(new ScrollingMovementMethod());

        // move buttons to correct position for every tree
        int treeID = wpList.get(0).treeId;
        Guideline treeTop = findViewById(R.id.tree_guideline_top);
        Guideline treeLeft = findViewById(R.id.tree_guideline_left);
        Guideline symbolTop = findViewById(R.id.symbol_guideline_top);
        Guideline symbolLeft = findViewById(R.id.symbol_guideline_left);
        Guideline backgroundTop = findViewById(R.id.background_guideline_top);
        Guideline backgroundLeft = findViewById(R.id.background_guideline_left);
        switch (treeID) {
            case 1: //Buche
                backgroundButton.setVisibility(View.GONE);
                treeTop.setGuidelinePercent(0.74f);
                treeLeft.setGuidelinePercent(0.58f);
                symbolTop.setGuidelinePercent(0.35f);
                symbolLeft.setGuidelinePercent(0.8f);
                break;
            case 2: //Linde
                treeButton.setVisibility(View.GONE);
                backgroundTop.setGuidelinePercent(0.53f);
                backgroundLeft.setGuidelinePercent(0.65f);
                symbolTop.setGuidelinePercent(0.12f);
                symbolLeft.setGuidelinePercent(0.27f);
                break;
            case 3: //Kiefer
                backgroundButton.setVisibility(View.GONE);
                treeTop.setGuidelinePercent(0.5f);
                treeLeft.setGuidelinePercent(0.3f);
                symbolTop.setGuidelinePercent(0.68f);
                symbolLeft.setGuidelinePercent(0.80f);
                break;
            case 4: //Eiche
                backgroundButton.setVisibility(View.GONE);
                treeTop.setGuidelinePercent(0.45f);
                treeLeft.setGuidelinePercent(0.1f);
                symbolTop.setGuidelinePercent(0.32f);
                symbolLeft.setGuidelinePercent(0.75f);
                break;
            case 5: //Hasel
                treeTop.setGuidelinePercent(0.65f);
                treeLeft.setGuidelinePercent(0.32f);
                backgroundTop.setGuidelinePercent(0.65f);
                backgroundLeft.setGuidelinePercent(0.65f);
                symbolTop.setGuidelinePercent(0.15f);
                symbolLeft.setGuidelinePercent(0.72f);
                break;
            case 6: //Birke
                treeTop.setGuidelinePercent(0.6f);
                treeLeft.setGuidelinePercent(0.58f);
                backgroundTop.setGuidelinePercent(0.74f);
                backgroundLeft.setGuidelinePercent(0.16f);
                symbolTop.setGuidelinePercent(0.2f);
                symbolLeft.setGuidelinePercent(0.79f);
                break;
            case 7: //Eberesche
                treeButton.setVisibility(View.GONE);
                backgroundTop.setGuidelinePercent(0.72f);
                backgroundLeft.setGuidelinePercent(0.45f);
                symbolTop.setGuidelinePercent(0.25f);
                symbolLeft.setGuidelinePercent(0.55f);
                break;
            case 8: //Tanne
                backgroundButton.setVisibility(View.GONE);
                treeTop.setGuidelinePercent(0.67f);
                treeLeft.setGuidelinePercent(0.61f);
                symbolTop.setGuidelinePercent(0.02f);
                symbolLeft.setGuidelinePercent(0.78f);
                break;
            case 9: //Fichte
                treeTop.setGuidelinePercent(0.65f);
                treeLeft.setGuidelinePercent(0.75f);
                backgroundTop.setGuidelinePercent(0.57f);
                backgroundLeft.setGuidelinePercent(0.06f);
                symbolTop.setGuidelinePercent(0.25f);
                symbolLeft.setGuidelinePercent(0.2f);
                break;
            default: //Ahorn
                treeTop.setGuidelinePercent(0.6f);
                treeLeft.setGuidelinePercent(0.75f);
                backgroundTop.setGuidelinePercent(0.5f);
                backgroundLeft.setGuidelinePercent(0.15f);
                symbolTop.setGuidelinePercent(0.16f);
                symbolLeft.setGuidelinePercent(0.8f);
                break;
        }
    }

    public void resetTreeView(Integer buttonInactiveId) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            // Wait until click is performed
            treeButton.setBackgroundResource(buttonInactiveId);
            backgroundButton.setBackgroundResource(buttonInactiveId);
            symbolButton.setBackgroundResource(buttonInactiveId);
            description.setText(placeholder);
            description.scrollTo(0, 0);
        }, 1500);
    }
}
