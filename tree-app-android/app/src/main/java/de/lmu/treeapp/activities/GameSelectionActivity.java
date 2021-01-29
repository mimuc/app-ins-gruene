package de.lmu.treeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.adapter.GameselectionRecyclerViewAdapter;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

/**
 * This Activity is displayed, when the user clicks on a category inside a tree-card. It contains the different games in this category.
 * The layout and functionality of the game-previews is handled in the RecyclerViewAdapter (see "adapter/GameselectionRecyclerViewAdapter").
 */
public class GameSelectionActivity extends AppCompatActivity {

    private RecyclerView gameSelectionRecyclerView;
    private int treeId;
    private Tree.GameCategories category;
    private List<Integer> gameIds;

    /**
     * On creation of this Activity, get the current tree and category we need to display and set-up the RecyclerView and Actionbar.
     *
     * @param savedInstanceState Bundle containing the tree and category ids.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);
        // Get Tree and Category
        Bundle b = getIntent().getExtras();
        treeId = b.getInt("TreeId");
        category = (Tree.GameCategories) b.get("Category");
        this.gameIds = DataManager.getInstance(getApplicationContext()).getTree(treeId).GetGameIds(category);
        // Set ActionBar-Content
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.game_selection_title);
        }
        // Setup the RecyclerView
        gameSelectionRecyclerView = findViewById(R.id.gameselection_recyclerview);
        setupOverviewRecyclerView();
    }

    /**
     * Setup te RecyclerView with GridLayout
     */
    private void setupOverviewRecyclerView() {
        gameSelectionRecyclerView.setHasFixedSize(true);
        int gridColumns = 3;
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), gridColumns);
        gameSelectionRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        RecyclerView.Adapter recyclerViewAdapter = new GameselectionRecyclerViewAdapter(this.gameIds, this.treeId, this.category);
        gameSelectionRecyclerView.setAdapter(recyclerViewAdapter);
    }

    /**
     * Handle the Back/Home-Button -> Go back to MainActivity.
     *
     * @param item The item of the menu, which was clicked. We are only interested in the home-item.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = new Intent(this, MainActivity.class);
                NavUtils.navigateUpTo(this, upIntent);
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent upIntent = new Intent(this, MainActivity.class);
        NavUtils.navigateUpTo(this, upIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
