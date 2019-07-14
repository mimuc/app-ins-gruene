package de.lmu.treeapp.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

public class WantedPosterDetailsActivity extends AppCompatActivity {

    private Tree tree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanted_poster_details);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.wanted_poster_details_title_text);
        }
        tree = DataManager.getInstance(getApplicationContext()).GetTree(getIntent().getExtras().getInt("TreeId"));
        TextView tv = findViewById(R.id.wanted_poster_details_text_view);
        tv.setText(tree.name);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
