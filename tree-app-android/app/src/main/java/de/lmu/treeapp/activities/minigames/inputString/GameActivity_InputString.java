package de.lmu.treeapp.activities.minigames.inputString;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.Random;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.daos.app.GameStateInputStringDao;
import de.lmu.treeapp.contentData.database.entities.app.GameStateInputString;
import de.lmu.treeapp.contentData.database.entities.content.GameInputStringRelations;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;

public class GameActivity_InputString extends GameActivity_Base implements PopupInterface {

    private TextInputEditText inputField;
    protected Popup popup;
    protected GameStateInputString gameStateInputString;
    private boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConstraintLayout constraintLayout = findViewById(R.id.constrainLayout_inputString);
        ConstraintSet set = new ConstraintSet();

        inputField = findViewById(R.id.game_inputString_inputField);
        TextInputLayout textInputLayout = findViewById(R.id.textInputLayout);

        popup = new Popup(this, treeId);
        popup.setButtonSecondary(true);

        getGameState().observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            GameInputStringRelations inputStringGame = (GameInputStringRelations) gameContent;
            inputField = findViewById(R.id.game_inputString_inputField);
            Button sendButton = findViewById(R.id.game_inputString_sendButton);

            if (gameStateInputString != null) {
                inputField.setText(gameStateInputString.inputString);
            }
            int image = getResources().getIdentifier(inputStringGame.getImageResource(),
                    "drawable", getPackageName());

            createBackground(constraintLayout, set, image, textInputLayout);

            sendButton.setOnClickListener(view -> {
                if (checkAnswer(Objects.requireNonNull(inputField.getText()).toString())) {
                    done = true;
                    popup.showWithButtonText(PopupType.POSITIVE_ANIMATION, getString(R.string.popup_btn_finished), getString(R.string.popup_btn_wiki), inputField.getText().toString());
                } else {
                    onFail();
                }
            });
        });
    }

    private boolean checkAnswer(String toString) {
        return true;
    }

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (action == PopupAction.ACCEPT) {
            saveGameState().subscribe();
            onSuccess();
        } else {
            // Set success without going back to overview
            DataManager.getInstance(getApplicationContext()).setGameCompleted(parentCategory,
                    gameContent.getId(), parentTree);
            saveGameState().subscribe();
            showTreeProfile(true);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__input_string;
    }

    @Override
    protected Completable getGameState() {
        return DataManager.getInstance(getApplicationContext()).getOrCreateGameStateSingle(treeId, gameId, parentCategory, GameStateInputStringDao.class).flatMapCompletable(s -> {
            gameStateInputString = s;
            parentTree.appData.treeInputStrings.add(gameStateInputString);
            return Completable.complete();
        });
    }

    @Override
    protected Completable saveGameState() {
        gameStateInputString.inputString = Objects.requireNonNull(inputField.getText()).toString();
        return DataManager.getInstance(getApplicationContext()).updateGameState(gameStateInputString, GameStateInputStringDao.class);
    }

    public void createBackground(ConstraintLayout constraintLayout, ConstraintSet set, int image, TextInputLayout textInputLayout) {
        // for getting width and height of imageview in dp
        float scale = getResources().getDisplayMetrics().density;
        int size = (int) (50 * scale + 0.5f);

        Random random = new Random();

        // starting position of first image view (margin in relation to the textfield):
        // this px values will be translated to dp values later on by using the scale factor
        int x = 0;
        int y = 75; // (margin to the top of the input field)

        // distance between imageviews: 20dp to gain a little overlapping
        // start in lower left corner (x: 0, y: (int) (75 * scale + 0.5f))
        //           ---------------------
        //          |                     |
        // start ->  ---------------------

        // the constraints are set as follows:
        // app:layout_constraintTop_toTopOf="@id/textInputLayout" (set.connect(childView.getId(), ConstraintSet.TOP, textInputLayout.getId(), ConstraintSet.TOP, (int) (y * scale + 0.5f)))
        // app:layout_constraintStart_toStartOf="@id/textInputLayout" (set.connect(childView.getId(), ConstraintSet.START, textInputLayout.getId(), ConstraintSet.START, (int) (x * scale + 0.5f)))

        while (x <= 500) {
            // create new image view
            ImageView childView = new ImageView(this);
            // set values that are the same for each image view
            setImageViewValues(childView, image, random, constraintLayout, set, size);

            if (x <= 260) { // create lower border (goes from x = 0 to x = 270), y is set to 75:
                set.connect(childView.getId(), ConstraintSet.TOP, textInputLayout.getId(), ConstraintSet.TOP, (int) (y * scale + 0.5f));
                set.connect(childView.getId(), ConstraintSet.START, textInputLayout.getId(), ConstraintSet.START, (int) (x * scale + 0.5f));
            } else { // a few images should fall down randomly
                int randomX = random.nextInt(271);
                int randomY = random.nextInt(500);
                set.connect(childView.getId(), ConstraintSet.TOP, textInputLayout.getId(), ConstraintSet.TOP, (int) (randomY * scale + 0.5f));
                set.connect(childView.getId(), ConstraintSet.START, textInputLayout.getId(), ConstraintSet.START, (int) (randomX * scale + 0.5f));
            }
            set.applyTo(constraintLayout);
            x += 20;
        }
    }

    public void setImageViewValues(ImageView childView, int image, Random random, ConstraintLayout constraintLayout, ConstraintSet set, int size) {
        childView.setId(View.generateViewId());
        childView.setBackgroundResource(image);

        // generate random numbers for the alpha value (between 0.0 and 1.0) and rotation (between 90 and 270)
        // nextInt is exclusive of top value -> add 1 for inclusive
        int minRotation = 90;
        int maxRotation = 270;
        int randomRotation = random.nextInt((maxRotation - minRotation) + 1) + minRotation;
        childView.setRotation(randomRotation);
        float randomAlpha = (float) random.nextDouble();
        childView.setAlpha(randomAlpha);

        // add the image view to the constraint layout (parent)
        constraintLayout.addView(childView);

        // set constraints of imageview we've just added
        set.clone(constraintLayout);
        set.constrainHeight(childView.getId(), size);
        set.constrainWidth(childView.getId(), size);
        // place image view behind text field:
        set.setTranslationZ(childView.getId(), -1);
    }

    @Override
    public void onBackPressed() {
        if (done) {
            saveGameState().subscribe();
            onSuccess();
        } else super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (done) {
            saveGameState().subscribe();
            onSuccess();
        } else super.onSupportNavigateUp();
        return true;
    }
}
