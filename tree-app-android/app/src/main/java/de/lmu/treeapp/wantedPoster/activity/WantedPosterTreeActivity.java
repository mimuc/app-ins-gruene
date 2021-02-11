package de.lmu.treeapp.wantedPoster.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.WantedPosterImageList;
import de.lmu.treeapp.contentClasses.trees.WantedPosterTextList;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.wantedPoster.adapter.WantedPosterTreeAdapter;
import de.lmu.treeapp.wantedPoster.view.AgeInfoView;
import de.lmu.treeapp.wantedPoster.view.BlossomInfoView;
import de.lmu.treeapp.wantedPoster.view.FunFactView;
import de.lmu.treeapp.wantedPoster.view.MyStuffView;
import de.lmu.treeapp.wantedPoster.view.HeightInfoView;
import de.lmu.treeapp.wantedPoster.view.LeafFruitBarkInfoView;
import de.lmu.treeapp.wantedPoster.view.LifecycleInfoView;
import de.lmu.treeapp.wantedPoster.view.TreeDetailInfoView;
import de.lmu.treeapp.wantedPoster.view.TreeVideoView;

public class WantedPosterTreeActivity extends AppCompatActivity implements
        DiscreteScrollView.ScrollStateChangeListener<WantedPosterTreeAdapter.ViewHolder>,
        DiscreteScrollView.OnItemChangedListener<WantedPosterTreeAdapter.ViewHolder> {

    private WantedPosterTextList wantedPosterTextList;

    private TreeDetailInfoView treeDetailInfoView;
    private HeightInfoView heightInfoView;
    private AgeInfoView ageInfoView;
    private LeafFruitBarkInfoView leafFruitBarkInfoView;
    private BlossomInfoView blossomInfoView;
    private FunFactView funFactView;
    private LifecycleInfoView lifecycleInfoView;
    private MyStuffView myStuffView;
    private TreeVideoView treeVideoView;
    private final List<String> imageStrings = new ArrayList<String>();
    private Integer buttonInactiveId, questionMarkInactiveId, cameraInactiveId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanted_poster_tree);

        //gets the tree and treeProfile data from DataManager, which gets the data from room database and app startup
        // Data of our tree (from CMS and Database)
        Tree tree = DataManager.getInstance(getApplicationContext()).getTree(Objects.requireNonNull(getIntent().getExtras()).getInt("TreeId"));
        wantedPosterTextList = DataManager.getInstance(getApplicationContext()).getAllWantedPosters(tree.getId());
        WantedPosterImageList wantedPosterImageList = DataManager.getInstance(getApplicationContext()).getAllWantedPosterImages(tree.getId());

        String title = getResources().getString(R.string.wanted_poster_details_title_text) + " " + tree.getName();

        if (getSupportActionBar() != null) {
            //set the title of the wanted poster( for example: 'Steckbrief Ahorn')
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }

        treeDetailInfoView = findViewById(R.id.tree_detail_info_view);
        heightInfoView = findViewById(R.id.height_info_view);
        ageInfoView = findViewById(R.id.age_info_view);
        leafFruitBarkInfoView = findViewById(R.id.leaf_fruit_bark_info_view);
        blossomInfoView = findViewById(R.id.blossom_info_view);
        funFactView = findViewById(R.id.fun_fact_view);
        lifecycleInfoView = findViewById(R.id.lifecycle_info_view);
        myStuffView = findViewById(R.id.my_stuff_view);
        treeVideoView = findViewById(R.id.video_view);

        for (int i = 0; i < tree.contentData.images.size(); i++) {
            imageStrings.add(tree.contentData.images.get(i).imageResource);
        }
        List<Integer> treeWantedPosterIcons = Arrays.asList(
                getApplicationContext().getResources().getIdentifier(imageStrings.get(0),
                        "drawable", getApplicationContext().getPackageName()),
                R.drawable.sb_icon_hoehe, R.drawable.sb_icon_alter,
                getApplicationContext().getResources().getIdentifier(imageStrings.get(1),
                        "drawable", getApplicationContext().getPackageName()),
                getApplicationContext().getResources().getIdentifier(imageStrings.get(2),
                        "drawable", getApplicationContext().getPackageName()),
                getApplicationContext().getResources().getIdentifier(imageStrings.get(3),
                        "drawable", getApplicationContext().getPackageName()),
                R.drawable.sb_icon_bluete, R.drawable.sb_icon_funfact, R.drawable.sb_icon_zyklus,
                R.drawable.sb_icon_galerie, R.drawable.sb_icon_video);

        DiscreteScrollView treeCategoryPicker = findViewById(R.id.tree_category_picker);
        treeCategoryPicker.setSlideOnFling(true);
        treeCategoryPicker.setAdapter(new WantedPosterTreeAdapter(treeWantedPosterIcons));
        treeCategoryPicker.addOnItemChangedListener(this);
        treeCategoryPicker.addScrollStateChangeListener(this);
        treeCategoryPicker.scrollToPosition(0);
        treeCategoryPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.6f).setMaxScale(1.1f)
                .build());

        buttonInactiveId = getApplicationContext().getResources().getIdentifier(
                "sb_icon_magnifier", "drawable", getApplicationContext().getPackageName());
        Integer buttonActiveId = getApplicationContext().getResources().getIdentifier(
                "sb_icon_magnifierclicked", "drawable", getApplicationContext().getPackageName());
        questionMarkInactiveId = getApplicationContext().getResources().getIdentifier(
                "sb_icon_questionmark", "drawable", getApplicationContext().getPackageName());
        Integer questionMarkActiveId = getApplicationContext().getResources().getIdentifier(
                "sb_icon_questionmarkclicked", "drawable", getApplicationContext().getPackageName());
        cameraInactiveId = getApplicationContext().getResources().getIdentifier(
                "sb_icon_camera", "drawable", getApplicationContext().getPackageName());
        Integer cameraActiveId = getApplicationContext().getResources().getIdentifier(
                "sb_icon_cameraclicked", "drawable", getApplicationContext().getPackageName());

        treeDetailInfoView.setTreeDetailInfo(this, wantedPosterTextList.wantedPosters
                , wantedPosterImageList.wantedPosterImages, buttonActiveId, buttonInactiveId);
        heightInfoView.setHeightInfo(this, wantedPosterTextList.wantedPosters,
                wantedPosterImageList.wantedPosterImages, buttonActiveId, buttonInactiveId);
        ageInfoView.setAgeInfo(this, wantedPosterTextList.wantedPosters,
                wantedPosterImageList.wantedPosterImages, buttonActiveId, buttonInactiveId, questionMarkActiveId, questionMarkInactiveId);
        leafFruitBarkInfoView.setLeafInfo(wantedPosterTextList.wantedPosters,
                getApplicationContext().getResources().getIdentifier(imageStrings.get(1), "drawable", getApplicationContext().getPackageName()));
        leafFruitBarkInfoView.setFruitInfo(wantedPosterTextList.wantedPosters,
                getApplicationContext().getResources().getIdentifier(imageStrings.get(2), "drawable", getApplicationContext().getPackageName()));
        leafFruitBarkInfoView.setBarkInfo(wantedPosterTextList.wantedPosters,
                getApplicationContext().getResources().getIdentifier(imageStrings.get(3), "drawable", getApplicationContext().getPackageName()));
        blossomInfoView.setBlossomInfo(this, wantedPosterTextList.wantedPosters,
                wantedPosterImageList.wantedPosterImages, buttonActiveId, buttonInactiveId, cameraActiveId, cameraInactiveId);
        funFactView.setFunFact(this, wantedPosterTextList.wantedPosters,
                wantedPosterImageList.wantedPosterImages);
        lifecycleInfoView.setLifecycleInfo(this, wantedPosterTextList.wantedPosters,
                wantedPosterImageList.wantedPosterImages);
        myStuffView.setMyStuff(this, tree.getId(), tree.appData.takePictureImages,
                tree.appData.treeInputStrings, tree.appData.treeDescriptions);
        treeVideoView.setTreeVideo(this.getLifecycle(), wantedPosterTextList.wantedPosters);

        setVisibility(View.GONE, heightInfoView, ageInfoView,
                leafFruitBarkInfoView, blossomInfoView, funFactView, lifecycleInfoView,
                myStuffView, treeVideoView);
    }

    @Override
    public void onCurrentItemChanged(@Nullable WantedPosterTreeAdapter.ViewHolder holder, int position) {
        //viewHolder will never be null, because we never remove items from adapter's list
        if (holder != null) {
            switch (position) {
                case 0:
                    setVisibility(View.GONE, heightInfoView, ageInfoView,
                            leafFruitBarkInfoView, blossomInfoView, funFactView, lifecycleInfoView,
                            myStuffView, treeVideoView);
                    treeDetailInfoView.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    setVisibility(View.GONE, treeDetailInfoView, ageInfoView,
                            leafFruitBarkInfoView, blossomInfoView, funFactView, lifecycleInfoView,
                            myStuffView, treeVideoView);
                    heightInfoView.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    setVisibility(View.GONE, treeDetailInfoView, heightInfoView,
                            leafFruitBarkInfoView, blossomInfoView, funFactView, lifecycleInfoView,
                            myStuffView, treeVideoView);
                    ageInfoView.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    setVisibility(View.GONE, treeDetailInfoView, heightInfoView, ageInfoView,
                            blossomInfoView, funFactView, lifecycleInfoView, myStuffView,
                            treeVideoView);
                    leafFruitBarkInfoView.setLeafInfo(wantedPosterTextList.wantedPosters,
                            getApplicationContext().getResources().getIdentifier(imageStrings.get(1),
                                    "drawable", getApplicationContext().getPackageName()));
                    leafFruitBarkInfoView.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    setVisibility(View.GONE, treeDetailInfoView, heightInfoView, ageInfoView,
                            blossomInfoView, funFactView, lifecycleInfoView, myStuffView,
                            treeVideoView);
                    leafFruitBarkInfoView.setFruitInfo(wantedPosterTextList.wantedPosters,
                            getApplicationContext().getResources().getIdentifier(imageStrings.get(2),
                                    "drawable", getApplicationContext().getPackageName()));
                    leafFruitBarkInfoView.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    setVisibility(View.GONE, treeDetailInfoView, heightInfoView, ageInfoView,
                            blossomInfoView, funFactView, lifecycleInfoView, myStuffView,
                            treeVideoView);
                    leafFruitBarkInfoView.setBarkInfo(wantedPosterTextList.wantedPosters,
                            getApplicationContext().getResources().getIdentifier(imageStrings.get(3),
                                    "drawable", getApplicationContext().getPackageName()));
                    leafFruitBarkInfoView.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    setVisibility(View.GONE, treeDetailInfoView, heightInfoView, ageInfoView,
                            leafFruitBarkInfoView, funFactView, lifecycleInfoView, myStuffView,
                            treeVideoView);
                    blossomInfoView.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    setVisibility(View.GONE, treeDetailInfoView, heightInfoView, ageInfoView,
                            leafFruitBarkInfoView, blossomInfoView, lifecycleInfoView, myStuffView,
                            treeVideoView);
                    funFactView.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    setVisibility(View.GONE, treeDetailInfoView, heightInfoView, ageInfoView,
                            leafFruitBarkInfoView, blossomInfoView, funFactView, myStuffView,
                            treeVideoView);
                    lifecycleInfoView.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    setVisibility(View.GONE, treeDetailInfoView, heightInfoView, ageInfoView,
                            leafFruitBarkInfoView, blossomInfoView, funFactView, lifecycleInfoView,
                            treeVideoView);
                    myStuffView.setVisibility(View.VISIBLE);
                    break;
                case 10:
                    setVisibility(View.GONE, treeDetailInfoView, heightInfoView, ageInfoView,
                            leafFruitBarkInfoView, blossomInfoView, funFactView, lifecycleInfoView,
                            myStuffView);
                    treeVideoView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    /**
     * @param visibility possible values: View.VISIBLE or View.GONE
     * @param views      list of views which we don't wanna show the user so we make them invisible
     */
    public static void setVisibility(int visibility, View... views) {
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }

    @Override
    public void onScrollStart(@NonNull WantedPosterTreeAdapter.ViewHolder holder, int position) {
    }

    @Override
    public void onScrollEnd(@NonNull WantedPosterTreeAdapter.ViewHolder currentItemHolder,
                            int adapterPosition) {
    }

    @Override
    public void onScroll(
            float position,
            int currentIndex, int newIndex,
            @Nullable WantedPosterTreeAdapter.ViewHolder currentHolder,
            @Nullable WantedPosterTreeAdapter.ViewHolder newHolder) {
        switch (currentIndex) {
            case 0:
                treeDetailInfoView.resetTreeView(buttonInactiveId);
                break;
            case 1:
                heightInfoView.resetHeightView(buttonInactiveId);
                break;
            case 2:
                ageInfoView.resetAgeView(buttonInactiveId, questionMarkInactiveId);
                break;
            case 6:
                blossomInfoView.resetBlossomView(buttonInactiveId, cameraInactiveId);
                break;
            case 8:
                lifecycleInfoView.resetLifecycleInfoView();
                break;
            case 9:
                myStuffView.resetMyStuffView();
                break;
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
