package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.MediaType;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.entities.content.GameChooseAnswerOption;
import de.lmu.treeapp.contentData.database.entities.content.GameChooseAnswerRelations;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;

public class GameActivity_ChooseAnswer extends GameActivity_Base implements
        ChooseAnswer_Options_RecyclerViewAdapter.OptionClickInterface, PopupInterface {

    /**
     * Amount of remaining questions
     */
    public int current;
    public boolean superPowerGivenAnswer = false;
    public ArrayList<Integer> quizIDs = new ArrayList<>();
    protected String resultText;
    protected int resultImage;
    AutofitRecyclerView optionsRecyclerView;
    Adapter<ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder> recyclerViewAdapter;
    TextView description;
    Popup popup;
    private int showAnswer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        description = findViewById(R.id.game_description);
        optionsRecyclerView = findViewById(R.id.game_chooseAnswer_recyclerView);
        optionsRecyclerView.setHasFixedSize(true);

        // TODO use own GameType for each and compare
        if (gameId >= 100) {
            current = 1;
        } else if (gameId < 40) {
            current = 4;
        } else {
            current = 3;
        }
        setupOptionRecyclerView();
        popup = new Popup(this, treeId);
        popup.setLooseTitle(getString(R.string.popup_quiz_negative_title));
        popup.setWinTitle(getString(R.string.popup_quiz_positive_title));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__choose_answer;
    }

    private void setupOptionRecyclerView() {
        recyclerViewAdapter = new ChooseAnswer_Options_RecyclerViewAdapter(this,
                (GameChooseAnswerRelations) gameContent, getApplicationContext()
        );
        optionsRecyclerView.setAdapter(recyclerViewAdapter);

        List<GameChooseAnswerOption> options = ((GameChooseAnswerRelations) gameContent).getOptions();
        for (GameChooseAnswerOption option : options) {
            if (option.isRight) {
                if (option.optionType == MediaType.TEXT) {
                    resultText = option.content;
                } else if (option.optionType == MediaType.IMAGE) {
                    resultImage = getResources().getIdentifier(option.content, "drawable", getPackageName());
                }

                optionsRecyclerView.setIsImage(option.optionType == MediaType.IMAGE);
                break;
            }
        }
    }

    private void showNextQuestion() {
        gameContent = DataManager.getInstance(getApplicationContext()).getMinigame(getNextQuizID());
        description.setText(gameContent.getDescription());
        setupOptionRecyclerView();
        showAnswer = 0;
        popup.dismiss();
    }

    @Override
    public void optionClicked(GameChooseAnswerOption option) {
        if (option.isRight) {
            //calls positive popup
            if (current > 1) {
                popup.showWithButtonText(PopupType.POSITIVE, getString(R.string.popup_btn_continue));
            } else {
                superPowerGivenAnswer = true;
                popup.showWithButtonText(PopupType.POSITIVE_ANIMATION, getString(R.string.popup_btn_finished));
            }
        } else {
            showAnswer++;

            //calls negative popup
            if (showAnswer < 2) {
                popup.showWithButtonText(PopupType.NEGATIVE, getString(R.string.popup_neutral_ok),
                        getString(R.string.popup_try_again));
            } else {
                int popupBtnTextRes = current > 1 ? R.string.popup_btn_continue : R.string.popup_btn_finished;
                List<View> views = new ArrayList<>();
                if (option.optionType == MediaType.TEXT) {
                    TextView textView = new TextView(this);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setText(resultText);
                    views.add(textView);

                    popup.showWithButtonText(PopupType.NEGATIVE_ANIMATION,
                            getString(popupBtnTextRes), getString(R.string.popup_quiz_negative_text), views);
                } else if (option.optionType == MediaType.IMAGE) {
                    float factor = getResources().getDisplayMetrics().density; // Convert to dp
                    ImageView childView = new ImageView(this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT, (int) (125 * factor));
                    childView.setLayoutParams(lp);
                    Glide.with(this).load(resultImage).into(childView);
                    childView.setAdjustViewBounds(true);
                    views.add(childView);
                    popup.showWithButtonText(PopupType.NEGATIVE_ANIMATION,
                            getString(popupBtnTextRes), getString(R.string.popup_quiz_negative_text), views);
                }
            }
        }
    }

    private boolean checkGameState() {
        quizIDs.add(gameContent.getId());
        if (current > 1) {
            showNextQuestion();
            current--;
            return false;
        } else {
            // should return true when user has finished quiz or user finished "Superkraft" game
            // else returns false
            return !gameContent.getName().equals("Superkraft") || superPowerGivenAnswer;
        }
    }

    /**
     * listener methods (3) for continue/accept/wiki button -> click event
     *
     * @param type
     * @param action
     */
    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if ((type != PopupType.NEGATIVE && type != PopupType.NEGATIVE_ANIMATION ||
                showAnswer >= 2) && checkGameState()) {
            onQuizSuccess(quizIDs);
        }
    }

    @Override
    public void onBackPressed() {
        if (checkGameState()) onQuizSuccess(quizIDs);
        else super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (checkGameState()) onQuizSuccess(quizIDs);
        else finish();
        return true;
    }
}