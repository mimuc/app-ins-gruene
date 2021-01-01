package de.lmu.treeapp.activities.minigames.rhyme;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
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
import de.lmu.treeapp.contentData.database.entities.content.GameRhymeItem;
import de.lmu.treeapp.contentData.database.entities.content.GameRhymeRelations;

import static com.google.android.flexbox.FlexWrap.WRAP;


public class GameActivity_Rhyme extends GameActivity_Base implements RecyclerAdapter.OptionClickInterface {
    ArrayList<RhymeElement> sList;
    ArrayList<RhymeElement> solutionList;
    RecyclerAdapter rcAdapterSolution;
    RecyclerAdapter rcAdapter;
    Dialog popupWindow;
    Button btnAccept;
    TextView popupTitle;
    RecyclerView recyclerViewSolution;
    private GameRhymeRelations rhymeGame;
    private ArrayList<RhymeElement> correctList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rhymeGame = (GameRhymeRelations) gameContent;
        popupWindow = new Dialog(this);

        ImageView contentBox = findViewById(R.id.leafImage);
        int backgroundImage = getResources().getIdentifier(rhymeGame.getImageResource(), "drawable", getPackageName());
        Glide.with(this).load(backgroundImage).into(contentBox);

        // Recycler View Content
        RecyclerView recyclerViewContent = findViewById(R.id.game_rhyme_content);
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
        recyclerViewSolution = findViewById(R.id.game_rhyme_solution);
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

        Button sendButton = findViewById(R.id.game_rhyme_sendButton);
        sendButton.setOnClickListener(view -> {
            boolean isCorrect = checkCorrectness();
            if (isCorrect) {
                showPositivePopup();
            } else {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Das stimmt leider noch nicht ganz.", Toast.LENGTH_LONG).show());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__rhyme;
    }

    private boolean checkCorrectness() {

        ArrayList<RhymeElement> dataToCheck = rcAdapterSolution.getItems();

        if (dataToCheck.size() != correctList.size()) {
            return false;
        }

        for (int i = 0; i < correctList.size(); i++) {
            if (correctList.get(i) != dataToCheck.get(i)) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<RhymeElement> getListItemData() {
        ArrayList<RhymeElement> listViewItems = new ArrayList<>();
        for (GameRhymeItem rhymeItem : rhymeGame.getItems()) {
            for (String split : rhymeItem.content.split("\\|")) {
                listViewItems.add(new RhymeElement(split));
            }
        }
        return listViewItems;
    }

    @Override
    public void optionClicked(int position, RecyclerAdapter.MyViewHolder holder) {

        RecyclerAdapter holderAdapter = holder.getOuterClass();

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

    private void showPositivePopup() {
        popupWindow.setContentView(R.layout.popup_quiz_positive);
        btnAccept = popupWindow.findViewById(R.id.forward_next_game_positive);
        popupTitle = popupWindow.findViewById(R.id.popup_positive_title);
        btnAccept.setText(R.string.game_btn_finished);

        ViewCompat.animate(btnAccept).setStartDelay(200).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();

        btnAccept.setOnClickListener(v -> {
            popupWindow.dismiss();
            onSuccess();
        });

        popupWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.show();

        Window window = popupWindow.getWindow();
        window.setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
    }
}
