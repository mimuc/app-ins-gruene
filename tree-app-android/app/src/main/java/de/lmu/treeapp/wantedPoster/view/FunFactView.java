package de.lmu.treeapp.wantedPoster.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
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

public class FunFactView extends LinearLayout {

    private final TextView pageTitle;
    private final TextView description1;
    private final TextView description2;
    private final ImageView image1;
    private final ImageView image2;

    private String title;
    private String fact1;
    private String fact2;
    private String graphic1;
    private String graphic2;

    public FunFactView(Context context) {
        super(context);
    }

    public FunFactView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FunFactView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setWillNotDraw(false);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        inflate(getContext(), R.layout.view_fun_fact, this);

        pageTitle = findViewById(R.id.page_title);
        description1 = findViewById(R.id.description);
        image1 = findViewById(R.id.fact1_image);
        description2 = findViewById(R.id.description2);
        image2 = findViewById(R.id.fact2_image);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setFunFact(Context context, List<WantedPoster> wpList, List<WantedPosterImage> wpiList) {
        for (WantedPoster wp : wpList) {
            if (wp.tab == WantedPosterTab.FUNFACT) {
                if (wp.name == WantedPosterTextType.TITLE) {
                    title = wp.description;
                } else if (wp.name == WantedPosterTextType.FACT_1) {
                    fact1 = wp.description;
                } else if (wp.name == WantedPosterTextType.FACT_2) {
                    fact2 = wp.description;
                }
            }
        }
        for (WantedPosterImage wpi : wpiList) {
            if (wpi.tab == WantedPosterTab.FUNFACT) {
                if (wpi.usage == WantedPosterImageType.GRAPHIC_1) {
                    graphic1 = wpi.imageResource;
                } else if (wpi.usage == WantedPosterImageType.GRAPHIC_2) {
                    graphic2 = wpi.imageResource;
                }

            }
        }

        pageTitle.setText(title);
        int image1Id = context.getApplicationContext().getResources().getIdentifier(graphic1,
                "drawable", context.getApplicationContext().getPackageName());
        image1.setImageResource(image1Id);
        description1.setText(fact1);
        int image2Id = context.getApplicationContext().getResources().getIdentifier(graphic2,
                "drawable", context.getApplicationContext().getPackageName());
        image2.setImageResource(image2Id);
        description2.setText(fact2);
        description1.setMovementMethod(new ScrollingMovementMethod());
        description2.setMovementMethod(new ScrollingMovementMethod());


        int treeID = wpList.get(0).treeId;
        if (treeID == 5 || treeID == 6) { //Hasel + Birke
            image2.setPadding(40, 40, 40, 40);
        } else if (treeID == 3) { //Kiefer
            image1.setPadding(20, 20, 20, 20);
        }
    }
}