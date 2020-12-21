package de.lmu.treeapp.utils.glide;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Glide target in order to change View background safely.
 */
public class BackgroundTarget extends CustomTarget<Drawable> {

    protected View mView;

    public BackgroundTarget(View view) {
        mView = view;
    }

    @Override
    public void onResourceReady(@NonNull Drawable resource, Transition<? super Drawable> transition) {
        mView.setBackground(resource);
    }

    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {
        mView.setBackground(placeholder);
    }
}
