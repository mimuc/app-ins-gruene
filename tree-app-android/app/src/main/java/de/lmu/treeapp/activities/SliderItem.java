package de.lmu.treeapp.activities;

import android.graphics.drawable.Drawable;

public class SliderItem {
    private int image;
    private Drawable drawable;

    SliderItem(Integer image, Drawable drawable) {
        if (drawable != null) {
            this.drawable = drawable;
        } else {
            this.image = image;
        }
    }

    public int getImage() {
        return image;
    }

    public Drawable getDrawable() {
        return drawable;
    }
}
