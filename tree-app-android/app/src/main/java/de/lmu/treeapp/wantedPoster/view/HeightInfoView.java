package de.lmu.treeapp.wantedPoster.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.*;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.database.entities.content.WantedPoster;
import de.lmu.treeapp.contentData.database.entities.content.WantedPosterImage;
import de.lmu.treeapp.wantedPoster.WantedPosterTab;
import de.lmu.treeapp.wantedPoster.WantedPosterTextType;

import java.util.List;

public class HeightInfoView extends LinearLayout {

    private final TextView pageTitle;
    private final TextView description;
    private final ImageView heightImage;
    private final ImageButton heightTreeButton;
    private final ImageButton heightHumanButton;

    private String title;
    private String placeholder;
    private String magnifierTree;
    private String magnifierHuman;
    private String imageName;

    public HeightInfoView(Context context) {
        super(context);
    }

    public HeightInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeightInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setWillNotDraw(false);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        inflate(getContext(), R.layout.view_height_info, this);

        pageTitle = findViewById(R.id.page_title);
        description = findViewById(R.id.description);
        heightImage = findViewById(R.id.height_image);
        heightHumanButton = findViewById(R.id.human_button);
        heightTreeButton = findViewById(R.id.tree_button);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setHeightInfo(Context context, List<WantedPoster> wpList,
                              List<WantedPosterImage> wpiList, Integer buttonActiveId,
                              Integer buttonInactiveId) {
        // Save data from database in local variables
        for (WantedPoster wp : wpList) {
            if (wp.tab == WantedPosterTab.HEIGHT) {
                if (wp.name == WantedPosterTextType.TITLE) {
                    title = wp.description;
                } else if (wp.name == WantedPosterTextType.TREE) {
                    magnifierTree = wp.description;
                } else if (wp.name == WantedPosterTextType.HEIGHT_HUMAN) {
                    magnifierHuman = wp.description;
                }
            } else if (wp.tab == WantedPosterTab.ALL && wp.name == WantedPosterTextType.STANDARD) {
                placeholder = wp.description;
            }
        }
        for (WantedPosterImage wpi : wpiList) {
            if (wpi.tab == WantedPosterTab.HEIGHT) {
                imageName = wpi.imageResource;
                break;
            }
        }

        // set card content and make buttons clickable
        pageTitle.setText(title);
        int imageId = context.getApplicationContext().getResources().getIdentifier(imageName,
                "drawable", context.getApplicationContext().getPackageName());
        heightImage.setImageResource(imageId);
        heightImage.setOnClickListener(view -> {
            heightTreeButton.setBackgroundResource(buttonInactiveId);
            heightHumanButton.setBackgroundResource(buttonInactiveId);
            description.setText(placeholder);
            description.scrollTo(0, 0);
        });
        heightTreeButton.setOnClickListener(view -> {
            heightTreeButton.setBackgroundResource(buttonActiveId);
            heightHumanButton.setBackgroundResource(buttonInactiveId);
            description.setText(magnifierTree);
            description.scrollTo(0, 0);
        });
        heightHumanButton.setOnClickListener(view -> {
            heightTreeButton.setBackgroundResource(buttonInactiveId);
            heightHumanButton.setBackgroundResource(buttonActiveId);
            description.setText(magnifierHuman);
            description.scrollTo(0, 0);
        });
        description.setMovementMethod(new ScrollingMovementMethod());

        // move text field to top for Hasel
        int treeID = wpList.get(0).treeId;
        if (treeID == 5) {
            LinearLayout textLayout = findViewById(R.id.height_text_layout);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) textLayout.getLayoutParams();
            params.width = 0;
            Guideline humanLeft = findViewById(R.id.human_guideline_left);
            humanLeft.setGuidelinePercent(0.24f);
        }
    }

    public void resetHeightView(Integer buttonInactiveId) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            // Wait until click is performed
            heightTreeButton.setBackgroundResource(buttonInactiveId);
            heightHumanButton.setBackgroundResource(buttonInactiveId);
            description.setText(placeholder);
            description.scrollTo(0, 0);
        }, 1500);

    }
}