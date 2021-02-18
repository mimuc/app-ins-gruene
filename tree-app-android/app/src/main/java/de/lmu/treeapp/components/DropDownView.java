package de.lmu.treeapp.components;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.AdapterView;

public class DropDownView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    protected int position;

    public DropDownView(Context context) {
        this(context, null);
    }

    public DropDownView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setInputType(InputType.TYPE_NULL);
        this.setOnItemClickListener((parent, view, position, id) -> {
        });
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        super.setOnItemClickListener((parent, view, position, id) -> {
            this.position = position;
            l.onItemClick(parent, view, position, id);
        });
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        setText((String) getAdapter().getItem(position), false);
    }
}
