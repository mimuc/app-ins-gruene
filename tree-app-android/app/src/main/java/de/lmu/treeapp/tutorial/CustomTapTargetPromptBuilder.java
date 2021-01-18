package de.lmu.treeapp.tutorial;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;

import androidx.annotation.NonNull;

import de.lmu.treeapp.R;
import uk.co.samuelwall.materialtaptargetprompt.ActivityResourceFinder;
import uk.co.samuelwall.materialtaptargetprompt.extras.PromptOptions;

public class CustomTapTargetPromptBuilder extends PromptOptions<CustomTapTargetPromptBuilder> {

    public CustomTapTargetPromptBuilder(final @NonNull Activity activity) {
        this(new ActivityResourceFinder(activity));
    }

    public CustomTapTargetPromptBuilder(ActivityResourceFinder resourceFinder) {
        super(resourceFinder);
        Typeface font = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            font = resourceFinder.getResources().getFont(R.font.freude);
        }
        this.setMaxTextWidth(R.dimen.max_text_width)
                .setCaptureTouchEventOnFocal(true)
                .setCaptureTouchEventOutsidePrompt(true)
                .setIdleAnimationEnabled(false)
                .setBackgroundColour(resourceFinder.getResources().getColor(R.color.forest_transparent))
                .setFocalColour(resourceFinder.getResources().getColor(R.color.transparent))
                .setSecondaryTextTypeface(font)
                .setPrimaryTextTypeface(font)
                .setPrimaryTextSize(R.dimen.heading1)
                .setSecondaryTextSize(R.dimen.heading2);
    }
}