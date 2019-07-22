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
    private int treeId;
    private Tree.GameCategories category;
    private List<Integer> gameIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);

        Bundle b = getIntent().getExtras();
        treeId = b.getInt("TreeId");
        category = (Tree.GameCategories) b.get("Category");
        this.gameIds = DataManager.getInstance(getApplicationContext()).GetTree(treeId).GetGameIds(category);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.game_selection_title);
        }

        gameSelectionRecyclerView = findViewById(R.id.gameselection_recyclerview);
        setupOverviewRecyclerView();
    }

    private void setupOverviewRecyclerView() {
        gameSelectionRecyclerView.setHasFixedSize(true);
        int gridColumns = 2;
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), gridColumns);
        gameSelectionRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        RecyclerView.Adapter recyclerViewAdapter = new GameselectionRecyclerViewAdapter(this.gameIds, this.treeId, this.category);
        gameSelectionRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
