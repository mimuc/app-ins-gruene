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

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.database.entities.content.WantedPoster;
import de.lmu.treeapp.contentData.database.entities.content.WantedPosterImage;
import de.lmu.treeapp.wantedPoster.WantedPosterImageType;
import de.lmu.treeapp.wantedPoster.WantedPosterTab;
import de.lmu.treeapp.wantedPoster.WantedPosterTextType;

import java.util.List;

public class LifecycleInfoView extends LinearLayout {

    private final TextView pageTitle;
    private final ImageView cycle;
    private final ImageView leafAndFruit;
    private final ImageButton leafButton;
    private final ImageButton fruitButton;
    private final TextView description;

    private String title;
    private int cycleImage;
    private int leafImage;
    private int fruitImage;
    private int leaf_button_id;
    private int fruit_button_id;
    private int leafClickedButtonId;
    private int fruitClickedButtonId;
    private String placeholder;
    private String leafDescription;
    private String fruitDescription;

    public LifecycleInfoView(Context context) {
        super(context);
    }

    public LifecycleInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LifecycleInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setWillNotDraw(false);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        inflate(getContext(), R.layout.view_lifecycle_info, this);

        pageTitle = findViewById(R.id.page_title);
        cycle = findViewById(R.id.cycle);
        leafAndFruit = findViewById(R.id.leaf_and_fruit);
        leafButton = findViewById(R.id.leaf_button);
        fruitButton = findViewById(R.id.fruit_button);
        description = findViewById(R.id.description);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setLifecycleInfo(Context context, List<WantedPoster> wpList, List<WantedPosterImage> wpiList) {
        // Save data from database in local variables
        for (WantedPoster wp : wpList) {
            if (wp.tab == WantedPosterTab.CYCLE) {
                if (wp.name == WantedPosterTextType.TITLE) {
                    title = wp.description;
                } else if (wp.name == WantedPosterTextType.CYCLE_LEAF) {
                    leafDescription = wp.description;
                } else if (wp.name == WantedPosterTextType.CYCLE_FRUIT) {
                    fruitDescription = wp.description;
                }
            }
        }
        for (WantedPosterImage wpi : wpiList) {
            if (wpi.tab == WantedPosterTab.CYCLE) {
                if (wpi.usage == WantedPosterImageType.CIRCLE) {
                    cycleImage = context.getApplicationContext().getResources().getIdentifier(
                            wpi.imageResource, "drawable", context.getApplicationContext().getPackageName());
                } else if (wpi.usage == WantedPosterImageType.CYCLE_LEAF) {
                    leafImage = context.getApplicationContext().getResources().getIdentifier(
                            wpi.imageResource, "drawable", context.getApplicationContext().getPackageName());
                } else if (wpi.usage == WantedPosterImageType.CYCLE_FRUIT) {
                    fruitImage = context.getApplicationContext().getResources().getIdentifier(
                            wpi.imageResource, "drawable", context.getApplicationContext().getPackageName());
                } else if (wpi.usage == WantedPosterImageType.BUTTON_LEAF) {
                    leaf_button_id = context.getApplicationContext().getResources().getIdentifier(
                            wpi.imageResource, "drawable", context.getApplicationContext().getPackageName());
                } else if (wpi.usage == WantedPosterImageType.BUTTON_LEAF_CLICKED) {
                    leafClickedButtonId = context.getApplicationContext().getResources().getIdentifier(
                            wpi.imageResource, "drawable", context.getApplicationContext().getPackageName());
                } else if (wpi.usage == WantedPosterImageType.BUTTON_FRUIT) {
                    fruit_button_id = context.getApplicationContext().getResources().getIdentifier(
                            wpi.imageResource, "drawable", context.getApplicationContext().getPackageName());
                } else if (wpi.usage == WantedPosterImageType.BUTTON_FRUIT_CLICKED) {
                    fruitClickedButtonId = context.getApplicationContext().getResources().getIdentifier(
                            wpi.imageResource, "drawable", context.getApplicationContext().getPackageName());
                }
            }
        }

        placeholder = "Klicke doch mal auf die Symbole, um mehr zu erfahren!";
        pageTitle.setText(title);
        description.setText(placeholder);
        cycle.setImageResource(cycleImage);
        leafButton.setBackgroundResource(leaf_button_id);
        fruitButton.setBackgroundResource(fruit_button_id);

        leafButton.setOnClickListener(view -> {
            leafButton.setBackgroundResource(leafClickedButtonId);
            fruitButton.setBackgroundResource(fruit_button_id);
            leafAndFruit.setVisibility(VISIBLE);
            leafAndFruit.setImageResource(leafImage);
            description.setText(leafDescription);
            description.scrollTo(0, 0);
            leafAndFruit.setOnClickListener(v -> {
                leafButton.setBackgroundResource(leaf_button_id);
                fruitButton.setBackgroundResource(fruit_button_id);
                leafAndFruit.setVisibility(GONE);
                description.setText(placeholder);
                description.scrollTo(0, 0);
            });
        });
        fruitButton.setOnClickListener(view -> {
            leafButton.setBackgroundResource(leaf_button_id);
            fruitButton.setBackgroundResource(fruitClickedButtonId);
            leafAndFruit.setVisibility(VISIBLE);
            leafAndFruit.setImageResource(fruitImage);
            description.setText(fruitDescription);
            description.scrollTo(0, 0);
            leafAndFruit.setOnClickListener(v -> {
                leafButton.setBackgroundResource(leaf_button_id);
                fruitButton.setBackgroundResource(fruit_button_id);
                leafAndFruit.setVisibility(GONE);
                description.setText(placeholder);
                description.scrollTo(0, 0);
            });
        });
        description.setMovementMethod(new ScrollingMovementMethod());
    }

    public void resetLifecycleInfoView() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            // Wait until click is performed
            leafButton.setBackgroundResource(leaf_button_id);
            fruitButton.setBackgroundResource(fruit_button_id);
            leafAndFruit.setVisibility(GONE);
            description.setText(placeholder);
            description.scrollTo(0, 0);
        }, 1500);

    }
}
