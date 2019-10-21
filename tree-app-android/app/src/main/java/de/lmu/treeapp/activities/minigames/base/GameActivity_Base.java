package de.lmu.treeapp.activities.minigames.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.GameSelectionActivity;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

public class GameActivity_Base extends AppCompatActivity {


    protected Minigame_Base gameContent;
    protected int treeId;
    protected Tree parentTree;
    protected Tree.GameCategories parentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle b = getIntent().getExtras();
        parentCategory = (Tree.GameCategories)b.get("Category");
        treeId = b.getInt("TreeId");
        parentTree = DataManager.getInstance(getApplicationContext()).GetTree(treeId);
        gameContent = DataManager.getInstance(getApplicationContext()).GetMinigame(b.getInt("GameId"));

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(gameContent.name);
        }

        TextView description = findViewById(R.id.game_description);
        description.setText(gameContent.description);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    // Change this to display a confirm button which then starts the Activity
    protected void onSuccess(){
        //Toast.makeText(getApplicationContext(), "Richtig!", Toast.LENGTH_LONG).show();
        DataManager.getInstance(getApplicationContext()).GameCompleted(parentCategory, gameContent.uid, parentTree);
        Intent intent = new Intent(getApplicationContext(), GameSelectionActivity.class);
        intent.putExtra("TreeId", treeId);
        intent.putExtra("Category", parentCategory);
        startActivity(intent);
    }

    protected void onFail(){
        Toast.makeText(getApplicationContext(), "Falsch", Toast.LENGTH_LONG).show();
    }
}
