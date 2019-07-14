package de.lmu.treeapp.activities.minigames;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.Minigame_InputStringAnswer;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

public class GameActivity_InputString extends AppCompatActivity {

    private Minigame_InputStringAnswer gameContent;
    private Tree parentTree;
    private Tree.GameCategories parentCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game__input_string);

        Bundle b = getIntent().getExtras();
        parentCategory = (Tree.GameCategories)b.get("Category");
        parentTree = DataManager.getInstance(getApplicationContext()).GetTree(b.getInt("TreeId"));
        gameContent = (Minigame_InputStringAnswer) DataManager.getInstance(getApplicationContext()).GetMinigame(b.getInt("GameId"));

        TextView title = findViewById(R.id.game_inputString_title);
        title.setText(gameContent.name);
        TextView description = findViewById(R.id.game_inputString_description);
        description.setText(gameContent.description);
    }
}
