package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

public class GameActivity_ChooseAnswer extends AppCompatActivity {

    private Minigame_ChooseAnswer gameContent;
    private Tree parentTree;
    private Tree.GameCategories parentCategory;
    private RecyclerView optionsRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game__choose_answer);

        Bundle b = getIntent().getExtras();
        parentCategory = (Tree.GameCategories)b.get("Category");
        parentTree = DataManager.getInstance(getApplicationContext()).GetTree(b.getInt("TreeId"));
        gameContent = (Minigame_ChooseAnswer) DataManager.getInstance(getApplicationContext()).GetMinigame(b.getInt("GameId"));

        TextView title = findViewById(R.id.game_chooseAnswer_title);
        title.setText(gameContent.name);
        TextView description = findViewById(R.id.game_chooseAnswer_description);
        description.setText(gameContent.description);

        setupOptionRecyclerView();
    }

    private void setupOptionRecyclerView(){
        optionsRecyclerView = findViewById(R.id.game_chooseAnswer_recyclerView);
        optionsRecyclerView.setHasFixedSize(true);
        int columns = 2;
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), columns);
        optionsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        RecyclerView.Adapter recyclerViewAdapter = new ChooseAnswer_Options_RecyclerViewAdapter(gameContent, getApplicationContext());
        optionsRecyclerView.setAdapter(recyclerViewAdapter);
    }
}
