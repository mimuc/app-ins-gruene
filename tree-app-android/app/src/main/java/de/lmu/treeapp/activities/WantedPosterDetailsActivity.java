package de.lmu.treeapp.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.adapter.WantedPosterCardAdapter;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeProfile;
import de.lmu.treeapp.contentClasses.trees.TreeProfileCard;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.fragments.WantedPosterCard;

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
        RecyclerView recyclerView = findViewById(R.id.wanted_poster_recycler_view);
        WantedPosterCardAdapter adapter = new WantedPosterCardAdapter(getCards());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        String content = "";
        for (int i = 0; i < treeProfile.cards.size(); i++){
            TreeProfileCard card = treeProfile.cards.get(i);
            if (card.unlockedBy == Tree.GameCategories.none || tree.GetGameProgressionPercent(card.unlockedBy) > 90){
                content += card.name + "\n";
                content += card.content + "\n";
            }
        }

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            String titlePre = getResources().getString(R.string.wanted_poster_details_title_text);
            getSupportActionBar().setTitle( titlePre + " "+ tree.name);
        }
    }

    private List<WantedPosterCard> getCards() {
        List<WantedPosterCard> wantedPosterCards = new ArrayList<>();

        for (int i = 0; i < treeProfile.cards.size(); i++){
            TreeProfileCard card = treeProfile.cards.get(i);
            boolean unlocked = card.unlockedBy == Tree.GameCategories.none || tree.GetGameProgressionPercent(card.unlockedBy) > 90;
            int drawableId = getApplicationContext().getResources().getIdentifier(card.image, "drawable", getApplicationContext().getPackageName());
            Drawable image = getDrawable(drawableId);
            WantedPosterCard posterCard = new WantedPosterCard(unlocked,
                    card.name,
                    image,
                    card.content,
                    getBaseContext());

            wantedPosterCards.add(posterCard);
        }

        return wantedPosterCards;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
