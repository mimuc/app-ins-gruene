package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AutofitRecyclerView extends RecyclerView {

    private GridLayoutManager manager;
    private int columnWidth = -1;
    private boolean isImage = false;

    public AutofitRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public AutofitRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AutofitRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.columnWidth
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            columnWidth = getResources().getDisplayMetrics().widthPixels;
            array.recycle();
        }
        manager = new GridLayoutManager(getContext(), 1);
        setLayoutManager(manager);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (columnWidth > 0) {
            int spanCount;
            if (isImage) {
                spanCount = 2;
            } else {
                spanCount = 1;
            }
            manager.setSpanCount(spanCount);
        }
    }

    public void setIsImage(boolean image) {
        isImage = image;
    }
}

