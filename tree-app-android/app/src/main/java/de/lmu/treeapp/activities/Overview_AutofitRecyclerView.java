package de.lmu.treeapp.activities;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.lmu.treeapp.adapter.OverviewRecyclerViewAdapter;

public class Overview_AutofitRecyclerView extends RecyclerView {

    private GridLayoutManager manager;
    private int columnWidth = -1;

    public Overview_AutofitRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public Overview_AutofitRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Overview_AutofitRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.columnWidth
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            columnWidth = array.getDimensionPixelSize(0, -1);
            array.recycle();
        }
        manager = new GridLayoutManager(getContext(), 3);
        setLayoutManager(manager);
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (columnWidth > 0) {
            int spanCount;
            if(OverviewRecyclerViewAdapter.isImprint==true){
                spanCount = 1;
            }
            else{
                spanCount = 3;
            }
            manager.setSpanCount(spanCount);
            OverviewRecyclerViewAdapter.isImprint = false;
        }
    }
}
