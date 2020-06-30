package de.lmu.treeapp.activities.minigames.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.GameSelectionActivity;
import de.lmu.treeapp.activities.minigames.chooseAnswer.ChooseAnswer_Options_RecyclerViewAdapter;
import de.lmu.treeapp.activities.minigames.chooseAnswer.GameActivity_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.cms.ContentManager;

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

    // Within a quiz go back to the game selection activity and finish all previous activities, the game process is not saved
    @Override
    public boolean onSupportNavigateUp(){
        if(gameContent.type.name().equalsIgnoreCase("ChooseAnswer")) {
            ChooseAnswer_Options_RecyclerViewAdapter.current=1; //reset: quiz game can be started again from the beginning
        }
        finish();
        return true;
    }

    // In case of the android hardware back button is pressed,
    // the handling within a quiz game should be the same as using the navigate up button
    @Override
    public void onBackPressed(){
        if(gameContent.type.name().equalsIgnoreCase("ChooseAnswer")) {
            ChooseAnswer_Options_RecyclerViewAdapter.current=1; //reset: quiz game can be started again from the beginning
        }
        super.onBackPressed();
    }

    // Save the game process and go back to the game selection activity
    protected void onSuccess(){
        DataManager.getInstance(getApplicationContext()).GameCompleted(parentCategory, gameContent.uid, parentTree);
        Intent intent = new Intent(getApplicationContext(), GameSelectionActivity.class);
        intent.putExtra("TreeId", treeId);
        intent.putExtra("Category", parentCategory);
        startActivity(intent);
    }

    // Save the game process and display the next quiz game in this category
    protected void onQuizSuccess(){
        DataManager.getInstance(getApplicationContext()).GameCompleted(parentCategory, gameContent.uid, parentTree);
        DataManager.getInstance(getApplicationContext()).GameCompleted(parentCategory, gameContent.uid-10, parentTree);
        DataManager.getInstance(getApplicationContext()).GameCompleted(parentCategory, gameContent.uid-20, parentTree);
        DataManager.getInstance(getApplicationContext()).GameCompleted(parentCategory, gameContent.uid-30, parentTree);
        Intent intent = new Intent(getApplicationContext(), GameSelectionActivity.class);
        intent.putExtra("TreeId", treeId);
        intent.putExtra("Category", parentCategory);
        finish();
        startActivity(intent);
        ChooseAnswer_Options_RecyclerViewAdapter.current=1; //reset: quiz game can be started again from the beginning
    }

    protected void onFail(){
        Toast.makeText(getApplicationContext(), "Falsch", Toast.LENGTH_LONG).show();
    }

   public int getNextQuizID(){
        return DataManager.getInstance(getApplicationContext()).GetNextQuiz(gameContent.uid).uid;
    }
}
