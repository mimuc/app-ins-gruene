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
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import de.lmu.treeapp.R;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

    // Strings for animations
    protected int currentTree;

    // Views
    protected TextView popupTitle;
    protected TextView popupText;
    protected ImageView squirrelBar;
    protected ImageView squirrelTail;
    protected ImageView squirrel;
    protected ImageView tear;

    // Listener Object
    protected PopupInterface listener;

    // Others
    protected Dialog popupWindow;
    protected Context context;
    protected List<View> childViews;
    protected boolean isButtonSecondary;

    //ImgViews
    protected ImageView[] leafImageViews;

    protected boolean isSquirrel = true;

    /**
     * Popup Constructor
     *
     * @param context The context to create the Popup for.
     */
    public <T extends Context & PopupInterface> Popup(T context) {
        this(context, 0);
    }

    /**
     * Second Constructor for animation with tree id
     *
     * @param context The context to create the Popup for.
     * @param treeId  The treeId.
     */
    public <T extends Context & PopupInterface> Popup(T context, int treeId) {
        this(context, context, treeId);
    }

    public Popup(Context context, PopupInterface listener, int treeId) {

        //Create popup objects
        this.popupWindow = new Dialog(context);
        this.popupWindow.setContentView(R.layout.popup_universal);
        this.listener = listener;
        this.context = context;

        //Texts are imported from strings.xml, go there for title changes
        this.winTitle = context.getString(R.string.popup_win_title);
        this.neutralTitle = context.getString(R.string.popup_neutral_title);
        this.looseTitle = context.getString(R.string.popup_loose_title_short);
        this.btnAcceptText = context.getString(R.string.popup_btn_continue);
        this.btnSecondaryText = context.getString(R.string.button_repeat);

        this.currentTree = treeId;
        this.btnAccept = popupWindow.findViewById(R.id.button_primary);
        this.popupTitle = popupWindow.findViewById(R.id.popup_title);
        this.popupText = popupWindow.findViewById(R.id.popup_text);
        this.squirrelBar = popupWindow.findViewById(R.id.imageViewBar);
        this.btnSecondary = popupWindow.findViewById(R.id.button_secondary);
        this.squirrel = popupWindow.findViewById(R.id.imageViewSquirrel);
        this.tear = popupWindow.findViewById(R.id.imageViewTraene);
        this.squirrelTail = popupWindow.findViewById(R.id.imageViewTail);

        //Image Views falling leaves
        this.leafImageViews = new ImageView[5];
        this.leafImageViews[0] = popupWindow.findViewById(R.id.imageViewStar1);
        this.leafImageViews[1] = popupWindow.findViewById(R.id.imageViewStar2);
        this.leafImageViews[2] = popupWindow.findViewById(R.id.imageViewStar3);
        this.leafImageViews[3] = popupWindow.findViewById(R.id.imageViewStar4);
        this.leafImageViews[4] = popupWindow.findViewById(R.id.imageViewStar5);

        //Invisible at instantiation
        for (ImageView leafImageView : leafImageViews) {
            leafImageView.setVisibility(View.INVISIBLE);
        }
        this.tear.setVisibility(View.INVISIBLE);

        // TODO load from DB
        switch (currentTree) {
            case 0:
                setFallingLeavesRessource(R.drawable.ic_ahorn_blatt);
                break;
            case 1:
                setFallingLeavesRessource(R.drawable.ic_buche_blatt);
                break;
            case 2:
                setFallingLeavesRessource(R.drawable.ic_linde_blatt);
                break;
            case 3:
                setFallingLeavesRessource(R.drawable.ic_kiefer_blatt);
                break;
            case 4:
                setFallingLeavesRessource(R.drawable.ic_eiche_blatt);
                break;
            case 5:
                setFallingLeavesRessource(R.drawable.ic_hasel_blatt);
                break;
            case 6:
                setFallingLeavesRessource(R.drawable.ic_birke_blatt);
                break;
            case 7:
                setFallingLeavesRessource(R.drawable.ic_eberesche_blatt);
                break;
            case 8:
                setFallingLeavesRessource(R.drawable.ic_tanne_blatt);
                break;
            case 9:
                setFallingLeavesRessource(R.drawable.ic_fichte_blatt);
                break;
            default:
                //Do not change anything
                break;
        }
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

        this.childViews = childViews;
        btnAccept.setText(btnAcceptText);

        if (message != null) {
            popupText.setVisibility(View.VISIBLE);
            popupText.setText(message);
            ViewCompat.animate(popupText).setStartDelay(400).alpha(1).setDuration(300)
                    .setInterpolator(new DecelerateInterpolator(1.2f)).start();
        } else {
            popupText.setText("");
            popupText.setVisibility(View.GONE);
        }

        if (childViews != null && !childViews.isEmpty()) {
            LinearLayout parentLayout = popupWindow.findViewById(R.id.popup_content);
            for (View childView : childViews) {
                childView.setId(View.generateViewId());
                parentLayout.addView(childView);
            }
            ViewCompat.animate(parentLayout).setStartDelay(400).alpha(1).setDuration(300)
                    .setInterpolator(new DecelerateInterpolator(1.2f)).start();
        }

        // Hide Squirrel
        if (!isSquirrel) {
            ConstraintLayout squirrelFrame = popupWindow.findViewById(R.id.mascot_wrapper);
            squirrelFrame.setVisibility(View.GONE);
        }

        if (type == PopupType.POSITIVE) {
            popupTitle.setText(winTitle);
            squirrelBar.setImageResource(R.drawable.ic_mascott_true_only_bar);
            squirrel.setImageResource(R.drawable.ic_mascott_true_only_squirrel);
            // Light positive squirrel animation
            (new Handler(Looper.getMainLooper())).postDelayed(this::squirrelAnimationPositiveLight, 200);
        } else if (type == PopupType.POSITIVE_ANIMATION) {
            popupTitle.setText(winTitle);
            squirrelBar.setImageResource(R.drawable.ic_mascott_true_only_bar);
            squirrel.setImageResource(R.drawable.ic_mascott_true_only_squirrel);
            // Strong positive squirrel animation
            (new Handler(Looper.getMainLooper())).postDelayed(this::squirrelAnimationPositive, 600);
            // Leaves animation
            View popupContainer = popupWindow.findViewById(R.id.popupContainer);
            if (ViewCompat.isLaidOut(popupContainer)) {
                leafAnimation();
            } else {
                // Wait for laying out / building the view, to be able to measure heights and widths
                ViewTreeObserver.OnGlobalLayoutListener layoutListener;
                layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        leafAnimation();
                        popupContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                };
                popupContainer.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
            }
        } else if (type == PopupType.NEGATIVE_ANIMATION) {
            popupTitle.setText(looseTitle);
            this.squirrelBar.setImageResource(R.drawable.ic_mascott_false_squirrel_bar);
            this.squirrel.setImageResource(R.drawable.ic_mascott_false_only_squirrel);
            // Strong negative squirrel animation
            (new Handler(Looper.getMainLooper())).postDelayed(this::squirrelAnimationNegative, 700);
        } else if (type == PopupType.NEGATIVE) {
            popupTitle.setText(looseTitle);
            this.squirrelBar.setImageResource(R.drawable.ic_mascott_false_squirrel_bar);
            this.squirrel.setImageResource(R.drawable.ic_mascott_false_only_squirrel);
            // Light negative squirrel animation
            (new Handler(Looper.getMainLooper())).postDelayed(this::squirrelAnimationNegativeLight, 500);
        } else {
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
            onClose(type);
            listener.onPopupAction(type, PopupAction.ACCEPT);
        });

        if (isButtonSecondary) {
            btnSecondary.setVisibility(View.VISIBLE);
            btnSecondary.setText(btnSecondaryText);
            btnSecondary.setOnClickListener(v -> {
                onClose(type);
                listener.onPopupAction(type, PopupAction.SECONDARY);
            });
        } else {
            btnSecondary.setVisibility(View.GONE);
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

    public void showWithButtonText(PopupType type, String buttonTextPositive, String buttonTextWantedPoster, String message) {
        setButtonAcceptText(buttonTextPositive);
        setButtonSecondaryText(buttonTextWantedPoster);
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

    /**
     * On Popup close handler.
     *
     * @param type the Popup type
     */
    public void onClose(PopupType type) {
        if (childViews != null && !childViews.isEmpty()) {
            // Remove all additional views after closing popup
            LinearLayout parentLayout = popupWindow.findViewById(R.id.popup_content);
            parentLayout.removeAllViews();
            childViews = null;
        }

        if (type == PopupType.NEGATIVE_ANIMATION || type == PopupType.POSITIVE_ANIMATION) {
            resetAnimationViews();
        }

        //send the neutral button click event to the host activity where it is implemented
        popupWindow.dismiss();
    }

    /**
     * Reset animated image views.
     */
    private void resetAnimationViews() {
        for (ImageView leafImageView : leafImageViews) {
            leafImageView.animate().translationX(0).translationY(0).rotation(0).setDuration(0);
        }
        squirrelBar.animate().translationX(0).translationY(0).rotation(0).setDuration(0);
        squirrelTail.animate().translationX(0).translationY(0).rotation(0).setDuration(0);
    }

    /**
     * Dismiss Popup.
     */
    public void dismiss() {
        popupWindow.dismiss();
    }

    private void setFallingLeavesRessource(int id) {
        for (ImageView leafImageView : leafImageViews) {
            leafImageView.setImageResource(id);
        }
    }

    /**
     * Leaf animation.
     */
    private void leafAnimation() {
        View popupContainer = popupWindow.findViewById(R.id.popupContainer);

        int heightStartOverflow = 30;
        int heightOverflow = 400;
        int popupWindowHeight = popupContainer.getHeight() + leafImageViews[0].getHeight();

        int minDuration = 3000;
        int durationOverflow = 1000;

        int minRotation = 0;
        int rotationOverflow = 360;

        Interpolator interpolator = new AccelerateInterpolator();

        // Set random start position and make them visible
        for (ImageView leafImageView : leafImageViews) {
            leafImageView.setVisibility(View.VISIBLE);
            leafImageView.setTranslationY(ThreadLocalRandom.current().nextInt(-heightStartOverflow, 0));
            leafImageView.setRotation(ThreadLocalRandom.current().nextInt(-rotationOverflow, rotationOverflow));
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            for (ImageView leafImageView : leafImageViews) {
                leafImageView.animate()
                        .setInterpolator(interpolator)
                        .translationY(popupWindowHeight + ThreadLocalRandom.current().nextInt(heightOverflow))
                        .setDuration(minDuration + ThreadLocalRandom.current().nextInt(durationOverflow))
                        .rotationBy(minRotation + ThreadLocalRandom.current().nextInt(-rotationOverflow, rotationOverflow))
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                leafImageView.setVisibility(View.GONE);
                            }
                        });
            }
        }, 1500);
    }

    /**
     * Positive popup squirrel animation.
     */
    private void squirrelAnimationPositive() {

        // EYES
        squirrel.setImageResource(R.drawable.ic_mascott_true_only_squirrel_eyeclosed);

        FastOutSlowInInterpolator fastOutSlowInInterpolator = new FastOutSlowInInterpolator();

        // 1. SQUIRREL: jump
        ObjectAnimator jump = ObjectAnimator.ofPropertyValuesHolder(
                squirrel,
                PropertyValuesHolder.ofFloat("translationY", -90f));
        jump.setDuration(215);
        jump.setInterpolator(fastOutSlowInInterpolator);
        jump.setRepeatCount(5);
        jump.setRepeatMode(ObjectAnimator.REVERSE);

        // 1. TAIL: jump delayed
        ObjectAnimator jumpT = ObjectAnimator.ofPropertyValuesHolder(
                squirrelTail,
                PropertyValuesHolder.ofFloat("rotation", -2f),
                PropertyValuesHolder.ofFloat("translationY", -100f));
        jumpT.setDuration(215);
        jumpT.setInterpolator(fastOutSlowInInterpolator);
        jumpT.setRepeatCount(5);
        jumpT.setRepeatMode(ObjectAnimator.REVERSE);
        jumpT.setStartDelay(17);

        // 1. BAR: jump delayed
        ObjectAnimator jumpB = ObjectAnimator.ofPropertyValuesHolder(
                squirrelBar,
                PropertyValuesHolder.ofFloat("rotation", 2f),
                PropertyValuesHolder.ofFloat("translationY", -100f));
        jumpB.setDuration(215);
        jumpB.setInterpolator(fastOutSlowInInterpolator);
        jumpB.setRepeatCount(5);
        jumpB.setRepeatMode(ObjectAnimator.REVERSE);
        jumpB.setStartDelay(17);

        // 2. START jump animation
        jump.start();
        jumpT.start();
        jumpB.start();

        // 3. BAR pulling up animation
        Handler handlerB = new Handler(Looper.getMainLooper());
        handlerB.postDelayed(() -> {
            //Blinking
            squirrel.setImageResource(R.drawable.ic_mascott_true_only_squirrel);
            squirrelBar.animate().translationX(23).translationY(-23).rotationBy(-12).setDuration(250);
            new Handler(Looper.getMainLooper()).postDelayed(() -> squirrelBar.animate().translationX(15).translationY(10).rotationBy(20).setDuration(250), 1750);
        }, 1400);

        // 4. BLINK 2 times
        (new Handler(Looper.getMainLooper())).postDelayed(this::blinkSquirrelPositive, 3900);
        (new Handler(Looper.getMainLooper())).postDelayed(this::blinkSquirrelPositive, 4080);
    }

    /**
     * Positive animation eye blinking
     */
    private void blinkSquirrelPositive() {
        this.squirrel.setImageResource(R.drawable.ic_mascott_true_only_squirrel_eyeclosed);
        Handler handlerB = new Handler(Looper.getMainLooper());
        handlerB.postDelayed(() -> squirrel.setImageResource(R.drawable.ic_mascott_true_only_squirrel), 120);
    }

    /**
     * Negative popup squirrel animation.
     */
    private void squirrelAnimationNegative() {

        // 1. BAR start animation
        this.squirrelBar = popupWindow.findViewById(R.id.imageViewBar);
        squirrelBar.animate().translationX(15).translationY(10).setDuration(400);
        new Handler(Looper.getMainLooper()).postDelayed(() -> squirrelBar.animate().rotationBy(10)
                .translationX(15).setDuration(400), 10);

        // 2. TAIL start animation
        ImageView squirrelTail = popupWindow.findViewById(R.id.imageViewTail);
        squirrelTail.animate().rotationBy(-10).setDuration(400);

        // 3. TEAR start animation
        (new Handler(Looper.getMainLooper())).postDelayed(this::squirrelTearAnimationNegative, 900);

        // 4. BLINK 2 times
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::blinkSquirrelNegative, 2150);

        Handler handlerC = new Handler(Looper.getMainLooper());
        handlerC.postDelayed(this::blinkSquirrelNegative, 2280);
    }

    /**
     * Falling tear animation.
     */
    private void squirrelTearAnimationNegative() {

        // 1. Close eye
        this.squirrel.setImageResource(R.drawable.ic_mascott_false_only_squirrel_eyeclosed);

        // 2. TEAR fade in
        tear.setAlpha(0.00f);
        tear.setVisibility(View.VISIBLE);
        tear.animate().alpha(1).setDuration(380).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tear.setAlpha(1.00f);
            }
        });

        // 3. TEAR falling
        Handler handlerB = new Handler(Looper.getMainLooper());
        handlerB.postDelayed(() -> {
            tear.animate().translationY(800).setDuration(1000);
        }, 381);

        // 4. Open eye again
        Handler handlerC = new Handler(Looper.getMainLooper());
        handlerC.postDelayed(() -> squirrel.setImageResource(R.drawable.ic_mascott_false_only_squirrel), 1581);
    }

    /**
     * Negative animation eye blinking
     */
    private void blinkSquirrelNegative() {
        this.squirrel.setImageResource(R.drawable.ic_mascott_false_only_squirrel_eyeclosed);
        Handler handlerB = new Handler(Looper.getMainLooper());
        handlerB.postDelayed(() -> squirrel.setImageResource(R.drawable.ic_mascott_false_only_squirrel), 70);
    }

    /**
     * simple positive animation
     */
    private void squirrelAnimationPositiveLight() {
        Handler handlerB = new Handler(Looper.getMainLooper());
        handlerB.postDelayed(() -> {
            // bar animation
            squirrelBar.animate().rotationBy(-15).setDuration(260);
            new Handler(Looper.getMainLooper()).postDelayed(() -> squirrelBar.animate().rotationBy(15).setDuration(280), 750);
        }, 300);

        // 2. BLINK 2 times
        (new Handler(Looper.getMainLooper())).postDelayed(this::blinkSquirrelPositive, 1200);
        (new Handler(Looper.getMainLooper())).postDelayed(this::blinkSquirrelPositive, 1420);
    }

    /**
     * simple negative animation
     */
    private void squirrelAnimationNegativeLight() {
        //1. close eyes
        new Handler(Looper.getMainLooper()).postDelayed(() -> this.squirrel.setImageResource(R.drawable.ic_mascott_false_only_squirrel_eyeclosed), 80);
        Handler handlerB = new Handler(Looper.getMainLooper());
        handlerB.postDelayed(() -> {
            //2. rise bar
            squirrelBar.animate().rotationBy(-15).setDuration(340);
            //3. revert bar
            new Handler(Looper.getMainLooper()).postDelayed(() -> squirrelBar.animate().rotationBy(15).setDuration(370), 1080);
            //4. open eyes
            new Handler(Looper.getMainLooper()).postDelayed(() -> this.squirrel.setImageResource(R.drawable.ic_mascott_false_only_squirrel), 1890);
            //5. blink one time
            new Handler(Looper.getMainLooper()).postDelayed(this::blinkSquirrelNegative, 2180);
        }, 300);

    }


    /**
     * Getter & Setter below
     */
    public int getCurrentTree() {
        return currentTree;
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

}
