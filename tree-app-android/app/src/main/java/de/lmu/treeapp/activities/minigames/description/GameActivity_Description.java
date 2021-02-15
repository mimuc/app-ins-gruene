package de.lmu.treeapp.activities.minigames.description;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.button.MaterialButton;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.daos.app.GameStateDescriptionDao;
import de.lmu.treeapp.contentData.database.entities.app.GameStateDescription;
import de.lmu.treeapp.contentData.database.entities.content.GameDescriptionItem;
import de.lmu.treeapp.contentData.database.entities.content.GameDescriptionRelations;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;
import io.reactivex.rxjava3.core.Completable;

import static com.google.android.flexbox.FlexWrap.WRAP;
import static de.lmu.treeapp.utils.language.LanguageUtils.getTreeGenitiveGerman;


public class GameActivity_Description extends GameActivity_Base implements RecyclerAdapter.
        OptionClickInterface, PopupInterface {
    ArrayList<DescriptionElement> sList;
    RecyclerAdapter rcAdapter;
    Dialog popupWindow;
    Popup popup;
    String correctString;
    GameStateDescription gameStateDescription;
    private GameDescriptionRelations descriptionGame;
    MaterialButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (gameStateDescription == null) getGameState().subscribe();

        String treeName = getTreeGenitiveGerman(parentTree.getName());

        descriptionGame = (GameDescriptionRelations) gameContent;
        popupWindow = new Dialog(this);
        popup = new Popup(this, treeId);
        popup.setWinTitle(getString(R.string.popup_quiz_positive_title));
        popup.setLooseTitle(getString(R.string.popup_negative_title_close));
        popup.setNeutralTitle(getString(R.string.game_description_start_screen_short));
        popup.showWithButtonText(PopupType.NEUTRAL, getString(R.string.popup_btn_continue),
                MessageFormat.format(getString(R.string.game_description_start_screen_long),
                        treeName));

        addButton = findViewById(R.id.game_add_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.text_input,
                (ViewGroup) getCurrentFocus(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            String m_Text = input.getText().toString();
            rcAdapter.add(rcAdapter.getItemCount(), new DescriptionElement(m_Text, true,
                    GameActivity_Description.this, true));
            if (rcAdapter.getItemCount() > 20) {
                // allow max 20 items
                addButton.setVisibility(View.INVISIBLE);
            }

        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        final AlertDialog alert = builder.create();

        addButton.setOnClickListener(v -> {
            input.setText("");
            alert.show();
        });


        ImageView contentBox = findViewById(R.id.leafImage);
        int backgroundImage = getResources().getIdentifier(descriptionGame.getImageResource(),
                "drawable", getPackageName());
        Glide.with(this).load(backgroundImage).into(contentBox);

        // Recycler View Content
        RecyclerView recyclerViewContent = findViewById(R.id.game_orderWords_content);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this) {
        };
        layoutManager.setFlexWrap(WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setAlignItems(AlignItems.CENTER);
        recyclerViewContent.setLayoutManager(layoutManager);
        recyclerViewContent.setPadding(8, 30, 8, 30);

        sList = getListItemData();
        Collections.shuffle(sList);
        rcAdapter = new RecyclerAdapter(sList, this);
        recyclerViewContent.setAdapter(rcAdapter);


        Button sendButton = findViewById(R.id.game_orderWords_sendButton);
        sendButton.setOnClickListener(view -> {
            boolean isCorrect = checkCorrectness();
            if (isCorrect) {
                popup.setButtonSecondary(true);
                popup.showWithButtonText(PopupType.POSITIVE, getString(R.string.popup_btn_finished), getString(R.string.popup_btn_wiki), correctString);
            } else {
                popup.showWithButtonText(PopupType.NEGATIVE_ANIMATION, getString(R.string.popup_btn_continue), getString(R.string.popup_loose_title));
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__description;
    }

    private boolean checkCorrectness() {

        boolean isCorrect = true;
        boolean selectedSomething = false;
        ArrayList<String> correctStrings = new ArrayList<>();

        ArrayList<DescriptionElement> dataToCheck = rcAdapter.getItems();
        for (DescriptionElement element : dataToCheck) {
            if (element.isSelected && !element.isRight) {
                element.setCorrect(false);
                isCorrect = false;
            } else if (element.isSelected && element.isRight) {
                correctStrings.add(element.getText());
                element.setCorrect(true);
                if (!element.isUserCreated) {
                    selectedSomething = true;
                }
            }

        }
        correctString = getString(R.string.game_description_you_selected) + TextUtils.join(", ", correctStrings);
        gameStateDescription.description = "" + TextUtils.join(", ", correctStrings);

        return isCorrect && selectedSomething;
    }

    private ArrayList<DescriptionElement> getListItemData() {
        ArrayList<DescriptionElement> listViewItems = new ArrayList<>();
        for (GameDescriptionItem descriptionItem : descriptionGame.getItems()) {
            listViewItems.add(new DescriptionElement(descriptionItem.content, descriptionItem.isRight,
                    GameActivity_Description.this, false));
        }
        return listViewItems;
    }

    @Override
    public void optionClicked(int position, RecyclerAdapter.MyViewHolder holder) {
        RecyclerAdapter holderAdapter = holder.getOuterClass();
        holderAdapter.getItemById(position).changeSelected();
    }

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (type == PopupType.POSITIVE || type == PopupType.POSITIVE_ANIMATION) {
            if (action == PopupAction.ACCEPT) {
                saveGameState().subscribe();
                onSuccess();
            } else {
                DataManager.getInstance(getApplicationContext()).setGameCompleted(parentCategory,
                        gameContent.getId(), parentTree);
                saveGameState().subscribe();
                showTreeProfile();
            }
        }
    }

    @Override
    protected Completable getGameState() {
        return DataManager.getInstance(getApplicationContext()).getOrCreateGameStateSingle(treeId, gameId, parentCategory, GameStateDescriptionDao.class).flatMapCompletable(s -> {
            gameStateDescription = s;
            parentTree.appData.treeDescriptions.add(gameStateDescription);
            return Completable.complete();
        });
    }

    @Override
    protected Completable saveGameState() {
        return DataManager.getInstance(getApplicationContext()).updateGameState(gameStateDescription, GameStateDescriptionDao.class);
    }
}
