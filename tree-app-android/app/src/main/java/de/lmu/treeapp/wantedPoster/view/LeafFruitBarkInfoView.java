package de.lmu.treeapp.wantedPoster.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.database.entities.content.WantedPoster;
import de.lmu.treeapp.wantedPoster.WantedPosterTab;
import de.lmu.treeapp.wantedPoster.WantedPosterTextType;

import java.util.List;

public class LeafFruitBarkInfoView extends LinearLayout {

    private final TextView pageTitle;
    private final TextView description;
    private final ImageView image;

    private String title;
    private String magnifierTree;

    public LeafFruitBarkInfoView(Context context) {
        super(context);
    }

    public LeafFruitBarkInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LeafFruitBarkInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setWillNotDraw(false);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        inflate(getContext(), R.layout.view_leaf_fruit_bark_info, this);

        pageTitle = findViewById(R.id.page_title);
        description = findViewById(R.id.description);
        image = findViewById(R.id.leaf_fruit_bark_image);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setLeafInfo(List<WantedPoster> wpList, Integer imageId) {
        for (WantedPoster wp : wpList) {
            if (wp.tab == WantedPosterTab.LEAF) {
                if (wp.name == WantedPosterTextType.TITLE) {
                    title = wp.description;
                } else if (wp.name == WantedPosterTextType.LEAF_INFO) {
                    magnifierTree = wp.description;
                }
            }
        }
        pageTitle.setText(title);
        image.setImageResource(imageId);
        description.setText(magnifierTree);
    }

    public void setFruitInfo(List<WantedPoster> wpList, Integer imageId) {
        for (WantedPoster wp : wpList) {
            if (wp.tab == WantedPosterTab.FRUIT) {
                if (wp.name == WantedPosterTextType.TITLE) {
                    title = wp.description;
                } else if (wp.name == WantedPosterTextType.FRUIT_INFO) {
                    magnifierTree = wp.description;
                }
            }
        }
        pageTitle.setText(title);
        image.setImageResource(imageId);
        description.setText(magnifierTree);
    }

    public void setBarkInfo(List<WantedPoster> wpList, Integer imageId) {
        for (WantedPoster wp : wpList) {
            if (wp.tab == WantedPosterTab.BARK) {
                if (wp.name == WantedPosterTextType.TITLE) {
                    title = wp.description;
                } else if (wp.name == WantedPosterTextType.BARK_INFO) {
                    magnifierTree = wp.description;
                }
            }
        }
        pageTitle.setText(title);
        image.setImageResource(imageId);
        description.setText(magnifierTree);
    }
}