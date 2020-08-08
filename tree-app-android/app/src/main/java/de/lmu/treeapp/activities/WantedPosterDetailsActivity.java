package de.lmu.treeapp.activities;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.transition.Slide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.lmu.treeapp.R;
import de.lmu.treeapp.adapter.WantedPosterCardAdapter;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeProfile;
import de.lmu.treeapp.contentClasses.trees.TreeProfileCard;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.fragments.WantedPosterCard;

/**
 * Activity showing the profiles of the trees.
 */
public class WantedPosterDetailsActivity extends AppCompatActivity {
    private Tree tree;  // Data of our tree (from CMS and Database)
    private TreeProfile treeProfile;    // Data of our profile (from CMS)
    // Slide-Show (view pager):
    private List<Slide> slideList = new ArrayList<>();
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private Timer timer;
    private int current_position = 0;

    /**
     * On creating the activity, we generate a RecyclerView with our custom WantedPosterCardAdapter for all the profile-cards
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanted_poster_details);

        tree = DataManager.getInstance(getApplicationContext()).GetTree(getIntent().getExtras().getInt("TreeId"));
        treeProfile = DataManager.getInstance(getApplicationContext()).GetTreeProfile(tree.profileId);

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

        // Slide-Show:
        pager = findViewById(R.id.view_pager);
        prepareSlide();
        pagerAdapter = new PagerAdapter(slideList, this);
        pager.setAdapter(pagerAdapter);
        createSlideShow();
    }

    private void prepareSlide(){
        int[] imgId = (R., R.drawable.dark_blue_gradient);
        // List<String> titles = Arrays.asList(getResources().getStringArray(R.array.main_title));
        // List<String> subTitles = Arrays.asList(getResources().getStringArray(R.array.sub_title));
        for (int count = 0; count < imgId.length; count++){
            // slideList.add(new Slide(imgId[count], title.get(count), subTitles.get(count)));
            slideList.add(new Slide(imgId[count]));
        }
    }

    // Create a slide show for the photos of the Foto-Challenge
    private void createSlideShow(){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(current_position == slideList.size()){
                    current_position = 0;
                }
                pager.setCurrentItem(current_position++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },250, 25000);
    }

    /**
     * Gets the Profile-Cards.
     * @return A List of the Class WantedPosterCard, which includes whether its unlocked, the icon, name, content, etc.
     */
    private List<WantedPosterCard> getCards() {
        List<WantedPosterCard> wantedPosterCards = new ArrayList<>();

        for (int i = 0; i < treeProfile.cards.size(); i++){
            TreeProfileCard card = treeProfile.cards.get(i);
            boolean unlocked = card.unlockedBy == Tree.GameCategories.none || tree.GetGameProgressionPercent(card.unlockedBy) > 90;
            int drawableId = getApplicationContext().getResources().getIdentifier(card.image, "drawable", getApplicationContext().getPackageName());
            Drawable image = getDrawable(drawableId);

            Uri imageUri = null;
            if (!card.picture.equalsIgnoreCase("")){
                imageUri = getImageUri(card.picture);
            }

            WantedPosterCard posterCard = new WantedPosterCard(unlocked,
                    card.name,
                    image,
                    card.content,
                    imageUri,
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

    /**
     * Gets the URI of an Image based on a name.
     * @param name String of the image-name (coming from the CMS).
     * @return The Uri of the respective image. Returns null if it does not exist.
     */
    private Uri getImageUri(String name) {
        String imageFileName = "AppInsGruene_" + name.trim().toLowerCase();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mediaFile = new File(storageDir + File.separator + imageFileName +".jpg");

        if (!mediaFile.exists()) return null;

        Uri photoURI = FileProvider.getUriForFile(this,
                "de.lmu.treeapp.fileprovider",
                mediaFile);
        return photoURI;
    }


}
