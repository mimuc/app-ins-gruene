package de.lmu.treeapp.popup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import java.util.List;

import de.lmu.treeapp.R;

/**
 * Displays every popup that is used within the app.
 * Includes an interface "PopupInterface" that gives the possibility to listen to the
 * "accept button" and "secondary button" from outer classes.
 * These have to implement the Popup interface to do so.
 */
public class Popup {

    /**
     * Instance variables for the popups
     */
    //Buttons
    protected Button btnAccept;
    protected Button btnSecondary;
    // Strings for Buttons
    protected String btnAcceptText;
    protected String btnSecondaryText;
    // Strings for titles
    protected String winTitle;
    protected String neutralTitle;
    protected String looseTitle;
    // Views
    protected TextView popupTitle;
    protected TextView popupText;
    protected ImageView squirrelBar;
    // Listener Object
    protected PopupInterface listener;
    // Others
    protected Dialog popupWindow;
    protected Context context;
    protected List<View> childViews;
    protected boolean isButtonSecondary;
    protected boolean isSquirrel = true;

    /**
     * Popup Constructor
     *
     * @param context The context to create the Popup for.
     */
    public <T extends Context & PopupInterface> Popup(T context) {

        //Create popup objects
        popupWindow = new Dialog(context);
        this.listener = context;
        this.context = context;

        //Texts are imported from strings.xml, go there for title changes
        this.winTitle = context.getString(R.string.popup_win_title);
        this.neutralTitle = context.getString(R.string.popup_neutral_title);
        this.looseTitle = context.getString(R.string.popup_loose_title);

        this.btnAcceptText = context.getString(R.string.popup_btn_continue);
        this.btnSecondaryText = context.getString(R.string.button_repeat);
    }

    public String getWinTitle() {
        return winTitle;
    }

    public void setWinTitle(String winTitle) {
        this.winTitle = winTitle;
    }

    public String getNeutralTitle() {
        return neutralTitle;
    }

    public void setNeutralTitle(String neutralTitle) {
        this.neutralTitle = neutralTitle;
    }

    public String getLooseTitle() {
        return looseTitle;
    }

    public void setLooseTitle(String looseTitle) {
        this.looseTitle = looseTitle;
    }

    public String getButtonAcceptText() {
        return btnAcceptText;
    }

    public void setButtonAcceptText(String btnAcceptText) {
        this.btnAcceptText = btnAcceptText;
    }


    public boolean isButtonSecondary() {
        return isButtonSecondary;
    }

    public void setButtonSecondary(boolean buttonSecondary) {
        isButtonSecondary = buttonSecondary;
    }

    public String getButtonSecondaryText() {
        return btnSecondaryText;
    }

    public void setButtonSecondaryText(String btnSecondaryText) {
        this.btnSecondaryText = btnSecondaryText;
    }

    /**
     * Generic neutral popup with simple message and default button
     *
     * @param message The message to show.
     */
    public void show(String message) {
        show(PopupType.NEUTRAL, message, null);
    }

    /**
     * Generic popup positive or negative with "Weiter!" button
     *
     * @param type the popup type
     */
    public void show(PopupType type) {
        show(type, null, null);
    }

    /**
     * Positive / negative popup with default message and default button text
     *
     * @param type       the popup type
     * @param message    default message to choose by caller
     * @param childViews the views to show on popup
     */
    public void show(PopupType type, String message, List<View> childViews) {

        popupWindow.setContentView(R.layout.popup_universal);
        btnAccept = popupWindow.findViewById(R.id.button_primary);
        popupTitle = popupWindow.findViewById(R.id.popup_title);
        popupText = popupWindow.findViewById(R.id.popup_text);
        squirrelBar = popupWindow.findViewById(R.id.imageViewBar);
        btnSecondary = popupWindow.findViewById(R.id.button_secondary);

        this.childViews = childViews;
        btnAccept.setText(btnAcceptText);

        if (message != null) {
            popupText.setVisibility(View.VISIBLE);
            popupText.setText(message);
            ViewCompat.animate(popupText).setStartDelay(400).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();
        }
        if (childViews != null && !childViews.isEmpty()) {
            LinearLayout parentLayout = popupWindow.findViewById(R.id.popup_content);
            for (View childView : childViews) {
                childView.setId(View.generateViewId());
                parentLayout.addView(childView);
            }
            ViewCompat.animate(parentLayout).setStartDelay(400).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();
        }

        //Hide Squirrel
        if (!isSquirrel) {
            ConstraintLayout squirrelframe = popupWindow.findViewById(R.id.mascot_wrapper);
            squirrelframe.setVisibility(View.GONE);
        }

        if (type == PopupType.POSITIVE) {
            popupTitle.setText(winTitle);
            //Squirrel animation
            (new Handler(Looper.getMainLooper())).postDelayed(this::squirrelTailAnimation, 700);
            (new Handler(Looper.getMainLooper())).postDelayed(this::squirrelBarAnimation, 800);
            leafAnimation(true);
        } else if (type == PopupType.NEGATIVE) {
            this.squirrelBar.setImageResource(R.drawable.ic_mascott_false_squirrel_bar);
            popupTitle.setText(looseTitle);
            //Squirrel animation
            (new Handler(Looper.getMainLooper())).postDelayed(this::squirrelTailAnimationNegative, 700);
            (new Handler(Looper.getMainLooper())).postDelayed(this::squirrelBarAnimationNegative, 700);
            leafAnimation(false);
        } else {
            leafAnimation(false);
            popupTitle.setText(neutralTitle);
        }

        //pulsating button
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                btnAccept,
                PropertyValuesHolder.ofFloat("scaleX", 1.04f),
                PropertyValuesHolder.ofFloat("scaleY", 1.04f));
        scaleDown.setDuration(510);
        scaleDown.setInterpolator(new FastOutSlowInInterpolator());
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();

        btnAccept.setOnClickListener(v -> {
            onClose();
            listener.onPopupAction(type, PopupAction.ACCEPT);
        });

        if (isButtonSecondary) {
            btnSecondary.setVisibility(View.VISIBLE);
            btnSecondary.setText(btnSecondaryText);
            btnSecondary.setOnClickListener(v -> {
                onClose();
                listener.onPopupAction(type, PopupAction.SECONDARY);
            });
        }

        popupWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.show();
        Window window = popupWindow.getWindow();
        window.setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
    }

    /**
     * Generic popup positive or negative with default button text
     *
     * @param type       the popup type
     * @param buttonText default button text to choose by caller
     */
    public void showWithButtonText(PopupType type, String buttonText) {
        setButtonAcceptText(buttonText);
        show(type);
    }


    public void showWithButtonText(PopupType type, String buttonText, String message) {
        setButtonAcceptText(buttonText);
        show(type, message, null);
    }

    /**
     * Positive / negative popup with default message and default button text
     *
     * @param type       the popup type
     * @param buttonText default button text to choose by caller
     * @param message    default message to choose by caller
     * @param childViews the views to show on popup
     */
    public void showWithButtonText(PopupType type, String buttonText, String message, List<View> childViews) {
        setButtonAcceptText(buttonText);
        show(type, message, childViews);
    }

    public void hideSquirrel() {
        isSquirrel = false;
    }

    public void onClose() {
        if (childViews != null && !childViews.isEmpty()) {
            // Remove all additional views after closing popup
            LinearLayout parentLayout = popupWindow.findViewById(R.id.popup_content);
            parentLayout.removeAllViews();
            childViews = null;
        }
        //send the neutral button click event to the host activity where it is implemented
        popupWindow.dismiss();
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    /**
     * Animation methods
     */
    private void leafAnimation(boolean won) {
        ImageView starImageView = popupWindow.findViewById(R.id.imageViewStar1);
        ImageView starImageView2 = popupWindow.findViewById(R.id.imageViewStar2);
        ImageView starImageView3 = popupWindow.findViewById(R.id.imageViewStar3);
        ImageView starImageView4 = popupWindow.findViewById(R.id.imageViewStar4);
        ImageView starImageView5 = popupWindow.findViewById(R.id.imageViewStar5);

        if (won) {
            starImageView.animate()
                    .translationY(2000)
                    .setDuration(2800)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            starImageView.setVisibility(View.GONE);
                        }
                    });

            starImageView2.animate()
                    .translationY(2000)
                    .setDuration(3800)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            starImageView2.setVisibility(View.GONE);
                        }
                    });

            starImageView3.animate()
                    .translationY(2000)
                    .setDuration(2500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            starImageView3.setVisibility(View.GONE);
                        }
                    });

            starImageView4.animate()
                    .translationY(2000)
                    .setDuration(2200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            starImageView4.setVisibility(View.GONE);
                        }
                    });

            starImageView5.animate()
                    .translationY(2000)
                    .setDuration(3800)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            starImageView5.setVisibility(View.GONE);
                        }
                    });
        } else {
            starImageView.setVisibility(View.GONE);
            starImageView2.setVisibility(View.GONE);
            starImageView3.setVisibility(View.GONE);
            starImageView4.setVisibility(View.GONE);
            starImageView5.setVisibility(View.GONE);
        }
    }

    private void squirrelBarAnimation() {
        this.squirrelBar = popupWindow.findViewById(R.id.imageViewBar);
        squirrelBar.animate().translationX(-15).translationY(10).setDuration(400);
        new Handler(Looper.getMainLooper()).postDelayed(() -> squirrelBar.animate().translationX(15).translationY(-10).setDuration(400), 400);
    }

    private void squirrelTailAnimation() {
        ImageView squirrelTail = popupWindow.findViewById(R.id.imageViewTail);
        squirrelTail.animate().rotationBy(10).setDuration(400);
        new Handler(Looper.getMainLooper()).postDelayed(() -> squirrelTail.animate().rotationBy(-10).setDuration(400), 400);
    }

    private void squirrelBarAnimationNegative() {
        this.squirrelBar = popupWindow.findViewById(R.id.imageViewBar);
        squirrelBar.animate().translationX(15).translationY(10).setDuration(400);
        new Handler(Looper.getMainLooper()).postDelayed(() -> squirrelBar.animate().rotationBy(10).translationX(15).setDuration(400), 10);

    }

    private void squirrelTailAnimationNegative() {
        ImageView squirrelTail = popupWindow.findViewById(R.id.imageViewTail);
        squirrelTail.animate().rotationBy(-10).setDuration(400);

    }
}
