package de.lmu.treeapp.activities.minigames.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.GameSelectionActivity;
import de.lmu.treeapp.activities.WantedPosterDetailsActivity;
import de.lmu.treeapp.activities.minigames.chooseAnswer.GameActivity_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.IGameBase;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

public abstract class GameActivity_Base extends AppCompatActivity {

    protected IGameBase gameContent;
    protected int treeId;
    protected Tree parentTree;
    protected Tree.GameCategories parentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        Bundle b = getIntent().getExtras();
        parentCategory = (Tree.GameCategories) b.get("Category");
        treeId = b.getInt("TreeId");
        parentTree = DataManager.getInstance(getApplicationContext()).getTree(treeId);
        gameContent = DataManager.getInstance(getApplicationContext()).getMinigame(b.getInt("GameId"));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(gameContent.getName());
        }

        TextView description = findViewById(R.id.game_description);
        description.setText(gameContent.getDescription());
    }

    protected abstract int getLayoutId();

    // Remove the current activity from the stack to switch to the previous one
    @Override
    public boolean onSupportNavigateUp() {
        if (gameContent.getType().name().equalsIgnoreCase("ChooseAnswer")) {
            GameActivity_ChooseAnswer.quizIDs.clear();
        }
        finish();
        return true;
    }


    // Android hardware back button is pressed
    @Override
    public void onBackPressed() {
        if (gameContent.getType().name().equalsIgnoreCase("ChooseAnswer")) {
            GameActivity_ChooseAnswer.quizIDs.clear();
        }
        finish();
        super.onBackPressed();
    }


    // Save the game process and go back to the game selection activity
    protected void onSuccess() {
        DataManager.getInstance(getApplicationContext()).setGameCompleted(parentCategory, gameContent.getId(), parentTree).subscribe();
        Intent intent = new Intent(getApplicationContext(), GameSelectionActivity.class);
        intent.putExtra("TreeId", treeId);
        intent.putExtra("Category", parentCategory);
        startActivity(intent);
    }

    // Save the game process and display the next quiz game in this category
    protected void onQuizSuccess(ArrayList<Integer> quizIDs) {
        if (quizIDs == null || quizIDs.isEmpty()) return;
        System.out.println(quizIDs);

        for (int i = 0; i < quizIDs.size(); i++) {
            DataManager.getInstance(getApplicationContext()).setGameCompleted(parentCategory, quizIDs.get(i), parentTree).subscribe();
        }
        quizIDs.clear();
        System.out.println(quizIDs);


        Intent intent = new Intent(getApplicationContext(), GameSelectionActivity.class);
        intent.putExtra("TreeId", treeId);
        intent.putExtra("Category", parentCategory);
        finish(); // Removes the last quiz activity from the stack
        startActivity(intent);
    }

    public void showTreeProfile(String picPath, boolean toWantedPoster) {
        DataManager.getInstance(getApplicationContext()).setGameCompleted(parentCategory, gameContent.getId(), parentTree).subscribe();
        DataManager.getInstance(getApplicationContext()).setTakeTreePicture(picPath, parentCategory, parentTree).subscribe();
        if (toWantedPoster) {
            Intent intent = new Intent(getApplicationContext(), WantedPosterDetailsActivity.class);
            intent.putExtra("TreeId", treeId);
            startActivity(intent);
        }
    }

    protected void onFail() {
        Toast.makeText(getApplicationContext(), "Falsch", Toast.LENGTH_LONG).show();
    }

    public int getNextQuizID() {
        return DataManager.getInstance(getApplicationContext()).getNextQuiz(gameContent.getId()).getId();
    }
}
