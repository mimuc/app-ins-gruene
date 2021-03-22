package de.lmu.treeapp.activities.minigames.orderWords;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Collections;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentData.database.entities.content.GameOrderWordsItem;
import de.lmu.treeapp.contentData.database.entities.content.GameOrderWordsRelations;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;

import static com.google.android.flexbox.FlexWrap.WRAP;


public class GameActivity_OrderWords extends GameActivity_Base implements RecyclerAdapter.OptionClickInterface, PopupInterface {
    ArrayList<OrderWordsElement> sList;
    ArrayList<OrderWordsElement> solutionList;
    RecyclerAdapter rcAdapterSolution;
    RecyclerAdapter rcAdapter;
    Dialog popupWindow;
    RecyclerView recyclerViewSolution;
    Popup popup;
    String correctString;
    private GameOrderWordsRelations orderWordsGame;
    private ArrayList<OrderWordsElement> correctList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderWordsGame = (GameOrderWordsRelations) gameContent;
        popupWindow = new Dialog(this);
        popup = new Popup(this, treeId);
        popup.setWinTitle(getString(R.string.popup_quiz_positive_title));
        popup.setLooseTitle(getString(R.string.popup_negative_title_close));


        ImageView contentBox = findViewById(R.id.leafImage);
        int backgroundImage = getResources().getIdentifier(orderWordsGame.getImageResource(), "drawable", getPackageName());
        Glide.with(this).load(backgroundImage).into(contentBox);

        // Recycler View Content
        RecyclerView recyclerViewContent = findViewById(R.id.game_orderWords_content);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        layoutManager.setFlexWrap(WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setAlignItems(AlignItems.CENTER);
        recyclerViewContent.setLayoutManager(layoutManager);
        recyclerViewContent.setPadding(8, 30, 8, 30);

        sList = getListItemData();
        correctList = new ArrayList<>(sList);
        Collections.shuffle(sList);
        rcAdapter = new RecyclerAdapter(sList, this);
        recyclerViewContent.setAdapter(rcAdapter);

        // Recycler View Solution
        recyclerViewSolution = findViewById(R.id.game_orderWords_solution);
        FlexboxLayoutManager layoutManagerSolution = new FlexboxLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        layoutManagerSolution.setFlexWrap(WRAP);
        layoutManagerSolution.setFlexDirection(FlexDirection.ROW);
        layoutManagerSolution.setJustifyContent(JustifyContent.CENTER);
        layoutManagerSolution.setAlignItems(AlignItems.CENTER);
        recyclerViewSolution.setLayoutManager(layoutManagerSolution);
        recyclerViewSolution.setPadding(8, 30, 8, 30);

        solutionList = new ArrayList<>();
        rcAdapterSolution = new RecyclerAdapter(solutionList, this);
        recyclerViewSolution.setAdapter(rcAdapterSolution);

        ItemTouchHelper.Callback callback = new ItemMoveCallback(rcAdapterSolution);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewSolution);

        Button sendButton = findViewById(R.id.game_orderWords_sendButton);
        sendButton.setOnClickListener(view -> {
            if (isDone()) {
                popup.showWithButtonText(PopupType.POSITIVE_ANIMATION, getString(R.string.popup_btn_finished), correctString);
            } else {
                popup.showWithButtonText(PopupType.NEGATIVE_ANIMATION, getString(R.string.popup_neutral_ok), getString(R.string.popup_try_again_short));
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__order_words;
    }

    @Override
    protected boolean isDone() {

        ArrayList<OrderWordsElement> dataToCheck = rcAdapterSolution.getItems();

        if (dataToCheck.size() != correctList.size()) {
            return false;
        }

        boolean isCorrect = true;

        for (int i = 0; i < correctList.size(); i++) {
            if (correctList.get(i) != dataToCheck.get(i)) {
                dataToCheck.get(i).setCorrect(false);
                isCorrect = false;
            }
        }
        return isCorrect;
    }

    private ArrayList<OrderWordsElement> getListItemData() {
        ArrayList<OrderWordsElement> listViewItems = new ArrayList<>();
        for (GameOrderWordsItem orderWordsItem : orderWordsGame.getItems()) {
            correctString = orderWordsItem.content.replaceAll("\\|", "");
            for (String split : orderWordsItem.content.split("\\|")) {
                listViewItems.add(new OrderWordsElement(split, GameActivity_OrderWords.this));
            }
        }
        return listViewItems;
    }

    @Override
    public void optionClicked(int position, RecyclerAdapter.MyViewHolder holder) {

        RecyclerAdapter holderAdapter = holder.getOuterClass();

        for (OrderWordsElement element : rcAdapterSolution.getItems()) {
            element.setCorrect(true);
        }

        if (holderAdapter == rcAdapter) {

            rcAdapterSolution.add(rcAdapterSolution.getItemCount(), rcAdapter.getItemById(position));
        } else {
            rcAdapter.add(rcAdapter.getItemCount(), rcAdapterSolution.getItemById(position));
        }

        holderAdapter.remove(position);
        View solutionDescriptionView = findViewById(R.id.solution_description);

        if (rcAdapterSolution.getItemCount() > 0) {
            recyclerViewSolution.setVisibility(View.VISIBLE);
            solutionDescriptionView.setVisibility(View.VISIBLE);
        } else {
            recyclerViewSolution.setVisibility(View.INVISIBLE);
            solutionDescriptionView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (type != PopupType.NEGATIVE && type != PopupType.NEGATIVE_ANIMATION) {
            onSuccess();
        }
    }
}
