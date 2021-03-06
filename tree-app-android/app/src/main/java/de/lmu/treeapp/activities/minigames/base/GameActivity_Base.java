package de.lmu.treeapp.activities.minigames.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.GameSelectionActivity;
import de.lmu.treeapp.contentClasses.minigames.IGameBase;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.daos.app.GameStateScoresDao;
import de.lmu.treeapp.contentData.database.entities.app.GameStateScore;
import de.lmu.treeapp.wantedPoster.activity.WantedPosterTreeActivity;
import io.reactivex.rxjava3.core.Completable;

public abstract class GameActivity_Base extends AppCompatActivity {

    protected IGameBase gameContent;
    protected int treeId;
    protected int gameId;
    protected Tree parentTree;
    protected Tree.GameCategories parentCategory;
    protected GameStateScore gameStateScore;
    protected String specialGameName;
    protected boolean isDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        Bundle b = getIntent().getExtras();
        parentCategory = (Tree.GameCategories) b.get("Category");
        treeId = b.getInt("TreeId");
        gameId = b.getInt("GameId");
        specialGameName = b.getString("GameName") != null ? b.getString("GameName") : "";

        parentTree = DataManager.getInstance(getApplicationContext()).getTree(treeId);
        gameContent = DataManager.getInstance(getApplicationContext()).getMinigame(gameId);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(gameContent.getName());
        }

        TextView description = findViewById(R.id.game_description);
        if (description != null) {
            description.setText(gameContent.getDescription());
        }
    }

    protected abstract int getLayoutId();

    // Remove the current activity from the stack to switch to the previous one
    @Override
    public boolean onSupportNavigateUp() {
        if (isDone()) onSuccess();
        else {
            this.showGameSelection();
        }
        return true;
    }

    // Android hardware back button is pressed
    @Override
    public void onBackPressed() {
        if (isDone()) onSuccess();
        else {
            this.showGameSelection();
            super.onBackPressed();
        }
    }

    protected boolean isDone() {
        return isDone;
    }

    protected void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    // Save the game process and go back to the game selection activity
    protected void onSuccess() {
        DataManager.getInstance(getApplicationContext()).setGameCompleted(parentCategory, gameContent.getId(), parentTree).subscribe();
        showGameSelection();
    }

    public boolean isInputEmpty(String m_text) {
        return !m_text.matches("[\\s\\S]*(?i)[a-z][\\s\\S]*");
    }


    // Save the game process and display the next quiz game in this category
    protected void onQuizSuccess(ArrayList<Integer> quizIDs) {
        if (quizIDs == null || quizIDs.isEmpty()) return;
        System.out.println(quizIDs);

        for (int i = 0; i < quizIDs.size(); i++) {
            DataManager.getInstance(getApplicationContext()).setGameCompleted(parentCategory, quizIDs.get(i), parentTree).subscribe();
        }
        quizIDs.clear();

        showGameSelection();
    }

    public void showGameSelection() {
        Intent intent = new Intent(getApplicationContext(), GameSelectionActivity.class);
        intent.putExtra("TreeId", treeId);
        intent.putExtra("Category", parentCategory);
        startActivity(intent);
        finish();
    }

    public void showTreeProfile(Boolean toSelection) {
        Intent intent = new Intent(getApplicationContext(), WantedPosterTreeActivity.class);
        intent.putExtra("TreeId", treeId);
        intent.putExtra("TabId", 0);
        intent.putExtra("Category", parentCategory);
        intent.putExtra("ReturnToGames", toSelection);
        startActivity(intent);
        finish();
    }

    public void showTreeProfileCrafting(Boolean toSelection) {
        Intent intent = new Intent(getApplicationContext(), WantedPosterTreeActivity.class);
        intent.putExtra("TreeId", treeId);
        intent.putExtra("TabId", 0);
        intent.putExtra("Crafting", true);
        intent.putExtra("Category", parentCategory);
        intent.putExtra("ReturnToGames", toSelection);
        startActivity(intent);
        finish();
    }

    protected void onFail() {
        Toast.makeText(getApplicationContext(), getString(R.string.popup_quiz_negative_title), Toast.LENGTH_LONG).show();
    }

    public int getNextQuizID() {
        return DataManager.getInstance(getApplicationContext()).getNextQuiz(gameContent.getId()).getId();
    }

    /**
     * Save game state in background.
     */
    protected Completable getGameState() {
        return DataManager.getInstance(getApplicationContext()).getOrCreateGameStateSingle(treeId, gameId, parentCategory, GameStateScoresDao.class).flatMapCompletable(s -> {
            gameStateScore = s;
            return Completable.complete();
        });
    }

    /**
     * Write game state in background.
     */
    protected Completable saveGameState() {
        return DataManager.getInstance(getApplicationContext()).updateGameState(gameStateScore, GameStateScoresDao.class);
    }
}
