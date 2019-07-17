package de.lmu.treeapp.activities;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeProfile;
import de.lmu.treeapp.contentClasses.trees.TreeProfileCard;
import de.lmu.treeapp.contentData.DataManager;

public class WantedPosterDetailsActivity extends AppCompatActivity {

    private Tree tree;
    private TreeProfile treeProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanted_poster_details);


        tree = DataManager.getInstance(getApplicationContext()).GetTree(getIntent().getExtras().getInt("TreeId"));
        treeProfile = DataManager.getInstance(getApplicationContext()).GetTreeProfile(tree.profileId);

        // The following should be replaced by a RecyclerView or something similar for the cards
        TextView contentPlaceholder = findViewById(R.id.wanted_poster_details_content_placeholder);
        String content = "";
        for (int i = 0; i < treeProfile.cards.size(); i++){
            TreeProfileCard card = treeProfile.cards.get(i);
            content += card.name + "\n";
            content += card.content + "\n";
        }
        contentPlaceholder.setText(content);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            String titlePre = getResources().getString(R.string.wanted_poster_details_title_text);
            getSupportActionBar().setTitle( titlePre + " "+ tree.name);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
