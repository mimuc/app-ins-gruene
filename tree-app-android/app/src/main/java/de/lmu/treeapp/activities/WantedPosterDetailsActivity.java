package de.lmu.treeapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.lmu.treeapp.R;
import de.lmu.treeapp.adapter.SliderAdapter;
import de.lmu.treeapp.adapter.WantedPosterCardAdapter;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeProfile;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.entities.content.TreeProfileCard;
import de.lmu.treeapp.fragments.WantedPosterCard;

/**
 * Activity showing the profiles of the trees.
 */
public class WantedPosterDetailsActivity extends AppCompatActivity {
    private Tree tree;  // Data of our tree (from CMS and Database)
    private TreeProfile treeProfile;    // Data of our profile (from CMS)
    // Slide-Show (view pager):
    private final List<Slide> slideList = new ArrayList<>();
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private int current_position = 0;

    // ViewPager2
    private ViewPager2 viewPager2;
    private final Handler sliderHandler = new Handler();

    /**
     * On creating the activity, we generate a RecyclerView with our custom WantedPosterCardAdapter for all the profile-cards
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanted_poster_details);

        //gets the tree and treeProfile data from DataManager, which gets the data from room database and app startup
        tree = DataManager.getInstance(getApplicationContext()).GetTree(Objects.requireNonNull(getIntent().getExtras()).getInt("TreeId"));
        treeProfile = DataManager.getInstance(getApplicationContext()).GetTreeProfile(tree.getId());

        RecyclerView recyclerView = findViewById(R.id.wanted_poster_recycler_view);
        WantedPosterCardAdapter adapter = new WantedPosterCardAdapter(getCards());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        StringBuilder content = new StringBuilder();
        if (treeProfile != null && treeProfile.cards != null) {
            for (int i = 0; i < treeProfile.cards.size(); i++) {
                TreeProfileCard card = treeProfile.cards.get(i);
                if (card.unlockedBy == Tree.GameCategories.none || tree.GetGameProgressionPercent(card.unlockedBy) > 90) {
                    content.append(card.name).append("\n");
                    content.append(card.content).append("\n");
                }
            }
        }

        if (getSupportActionBar() != null) {
            //set the title of the wanted poster( for example: 'Steckbrief Ahorn')
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            String titlePre = getResources().getString(R.string.wanted_poster_details_title_text);
            getSupportActionBar().setTitle(titlePre + " " + tree.getName());
        }

        // ViewPage2
        viewPager2 = findViewById(R.id.viewPagerImageSlider);
        List<SliderItem> sliderItems = new ArrayList<>();

        /*
         * Check here if user has taken pics of the tree components (game GameActivity_TakePicture)
         * If user has taken picture, the path is saved in the room database. Get the path and
         * convert them to a drawable and set it to the sliderItem. If user has not taken a pic of
         * the tree component, a graphical image is set.
         */
        if (!tree.appData.imageFruitTaken.equals("")) {
            Drawable d = setImage(tree.appData.imageFruitTaken);
            sliderItems.add(new SliderItem(null, d));
        } else {
            sliderItems.add(new SliderItem(getApplicationContext().getResources().
                    getIdentifier(tree.imageFruit, "drawable",
                            getApplicationContext().getPackageName()), null));
        }
        if (!tree.appData.imageLeafTaken.equals("")) {
            Drawable d = setImage(tree.appData.imageLeafTaken);
            sliderItems.add(new SliderItem(null, d));
        } else {
            sliderItems.add(new SliderItem(getApplicationContext().getResources()
                    .getIdentifier(tree.imageLeaf, "drawable",
                            getApplicationContext().getPackageName()), null));
        }
        if (!tree.appData.imageTrunkTaken.equals("")) {
            Drawable d = setImage(tree.appData.imageTrunkTaken);
            sliderItems.add(new SliderItem(null, d));
        } else {
            sliderItems.add(new SliderItem(getApplicationContext().getResources()
                    .getIdentifier(tree.imageTrunk, "drawable",
                            getApplicationContext().getPackageName()), null));
        }
        if (!tree.appData.imageTreeTaken.equals("")) {
            Drawable d = setImage(tree.appData.imageTreeTaken);
            sliderItems.add(new SliderItem(null, d));
        }
        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        // Animation during sliding
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        viewPager2.setPageTransformer(compositePageTransformer);

        // Automatic slideshow of the photos
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 5000); // Slide duration 5 sec
            }
        });
    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    // Create a slide show for the photos of the Foto-Challenge
    private void createSlideShow() {
        final Handler handler = new Handler();
        final Runnable runnable = () -> {
            if (current_position == slideList.size()) {
                current_position = 0;
            }
            pager.setCurrentItem(current_position++, true);
        };
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 250, 25000);
    }

    /**
     * Gets the Profile-Cards.
     *
     * @return A List of the Class WantedPosterCard, which includes whether its unlocked, the icon, name, content, etc.
     */
    private List<WantedPosterCard> getCards() {
        List<WantedPosterCard> wantedPosterCards = new ArrayList<>();

        if (treeProfile != null && treeProfile.cards != null) {
            for (int i = 0; i < treeProfile.cards.size(); i++) {
                TreeProfileCard card = treeProfile.cards.get(i);
                boolean unlocked = card.unlockedBy == Tree.GameCategories.none || tree.GetGameProgressionPercent(card.unlockedBy) > 90;
                int drawableId = getApplicationContext().getResources().getIdentifier(card.image, "drawable", getApplicationContext().getPackageName());
                Drawable image = getDrawable(drawableId);

                Uri imageUri = null;
                if (!card.picture.equalsIgnoreCase("")) {
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
        }

        return wantedPosterCards;
    }

    /**
     * Gets the imagePath from room database of this tree component and return a drawable image
     * of this tree component.
     *
     * @param imagePath String of the image-name (coming from room database, when user have taken
     *                  pic of the tree)
     * @return A Drawable of the image
     */
    private BitmapDrawable setImage(String imagePath) {
        File imgFile = new File(imagePath);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        return new BitmapDrawable(getResources(), myBitmap);
    }

    /**
     * Gets the URI of an Image based on a name.
     *
     * @param name String of the image-name (coming from the CMS).
     * @return The Uri of the respective image. Returns null if it does not exist.
     */
    private Uri getImageUri(String name) {
        String imageFileName = "AppInsGruene_" + name.trim().toLowerCase();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mediaFile = new File(storageDir + File.separator + imageFileName + ".jpg");

        if (!mediaFile.exists()) return null;

        return FileProvider.getUriForFile(this,
                "de.lmu.treeapp.fileprovider",
                mediaFile);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // Slideshow paused if App is minimized
    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }
}
