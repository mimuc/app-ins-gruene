package de.lmu.treeapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.adapter.GameselectionRecyclerViewAdapter;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

public class GameSelectionActivity extends AppCompatActivity {

    private RecyclerView gameSelectionRecyclerView;
    private List<Integer> gameIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);

        Bundle b = getIntent().getExtras();
        int treeId = b.getInt("TreeId");
        Tree.GameCategories category = (Tree.GameCategories) b.get("Category");
        this.gameIds = DataManager.getInstance(getApplicationContext()).GetTree(treeId).GetGameIds(category);

        gameSelectionRecyclerView = findViewById(R.id.gameselection_recyclerview);
        setupOverviewRecyclerView();
    }

    private void setupOverviewRecyclerView() {
        gameSelectionRecyclerView.setHasFixedSize(true);
        int gridColumns = 3;
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), gridColumns);
        gameSelectionRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        RecyclerView.Adapter recyclerViewAdapter = new GameselectionRecyclerViewAdapter(this.gameIds);
        gameSelectionRecyclerView.setAdapter(recyclerViewAdapter);
    }
}
