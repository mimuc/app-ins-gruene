package de.lmu.treeapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.GameSelectionActivity;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeComponent;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.tutorial.CustomTapTargetPromptBuilder;
import de.lmu.treeapp.wantedPoster.activity.WantedPosterTreeActivity;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;

public class DetailSingleTreeFragment extends Fragment {

    public static String TREE_KEY = "tree";

    private TextView treeName;
    private ImageView treeImage;
    private Button treeProfileButton;
    // TODO replace with lists
    private ImageButton leafButton;
    private ImageButton fruitButton;
    private ImageButton trunkButton;
    private ImageButton otherButton;
    private ProgressBar leafProgressBar;
    private ProgressBar fruitProgressBar;
    private ProgressBar trunkProgressBar;
    private ProgressBar otherProgressBar;
    private Tree tree;

    public static DetailSingleTreeFragment newInstance(Tree tree) {
        DetailSingleTreeFragment detailSingleTreeFragment = new DetailSingleTreeFragment();

        Bundle args = new Bundle();
        args.putInt(TREE_KEY, tree.getId());
        detailSingleTreeFragment.setArguments(args);

        return detailSingleTreeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();
        int treeId = args.getInt(TREE_KEY);
        this.tree = DataManager.getInstance(getContext()).getTree(treeId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_detail_single_tree, container, false);

        this.findViewsById(rootView);
        this.setupSingleTreeContent();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presentMaterialTapTargetSequence();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.tree = DataManager.getInstance(getContext()).getTree(tree.getId());
        this.updateTreeView();
    }

    private void findViewsById(ViewGroup rootView) {
        treeName = rootView.findViewById(R.id.detail_single_tree_text);
        treeImage = rootView.findViewById(R.id.detail_single_tree_image);
        treeProfileButton = rootView.findViewById(R.id.detail_single_tree_profileButton);
        leafButton = rootView.findViewById(R.id.detail_single_tree_leafButton);
        fruitButton = rootView.findViewById(R.id.detail_single_tree_fruitButton);
        trunkButton = rootView.findViewById(R.id.detail_single_tree_trunkButton);
        otherButton = rootView.findViewById(R.id.detail_single_tree_otherButton);
        leafProgressBar = rootView.findViewById(R.id.detail_single_tree_circularProgressbar_leaf);
        fruitProgressBar = rootView.findViewById(R.id.detail_single_tree_circularProgressbar_fruit);
        trunkProgressBar = rootView.findViewById(R.id.detail_single_tree_circularProgressbar_trunk);
        otherProgressBar = rootView.findViewById(R.id.detail_single_tree_circularProgressbar_other);
    }

    private void setupSingleTreeContent() {
        treeName.setText(tree.getName());
        if (getContext() != null) {
            this.setupImageResources();
            this.setupOnClickListener();
        }
    }

    private void updateTreeView() {
        float leafProgress = this.tree.GetGameProgressionPercent(Tree.GameCategories.leaf);
        float fruitProgress = this.tree.GetGameProgressionPercent(Tree.GameCategories.fruit);
        float trunkProgress = this.tree.GetGameProgressionPercent(Tree.GameCategories.trunk);
        float otherProgress = this.tree.GetGameProgressionPercent(Tree.GameCategories.other);

        setProgressColor(leafProgress, leafProgressBar);
        setProgressColor(fruitProgress, fruitProgressBar);
        setProgressColor(trunkProgress, trunkProgressBar);
        setProgressColor(otherProgress, otherProgressBar);

        leafProgressBar.setProgress((int) leafProgress);
        fruitProgressBar.setProgress((int) fruitProgress);
        trunkProgressBar.setProgress((int) trunkProgress);
        otherProgressBar.setProgress((int) otherProgress);
    }

    private void setProgressColor(float progress, ProgressBar progressBar) {
        int[] progressColors = requireContext().getResources().getIntArray(R.array.progressColors);
        progressBar.setProgressTintList(ColorStateList.valueOf(progressColors[(int) (progress / (101f / progressColors.length))]));
    }

    private void setupImageResources() {
        int imageTreeId = requireContext().getResources().getIdentifier(tree.getTreeImage(TreeComponent.TREE).imageResource, "drawable", getContext().getPackageName());
        int imageLeafId = getContext().getResources().getIdentifier(tree.getTreeImage(TreeComponent.LEAF).imageResource, "drawable", getContext().getPackageName());
        int imageFruitId = getContext().getResources().getIdentifier(tree.getTreeImage(TreeComponent.FRUIT).imageResource, "drawable", getContext().getPackageName());
        int imageTrunkId = getContext().getResources().getIdentifier(tree.getTreeImage(TreeComponent.TRUNK).imageResource, "drawable", getContext().getPackageName());
        //int imageOtherId = getContext().getResources().getIdentifier(tree.imageOther, "drawable", getContext().getPackageName());
        Glide.with(this).load(imageTreeId).into(treeImage);
        Glide.with(this).load(imageLeafId).into(leafButton);
        Glide.with(this).load(imageFruitId).into(fruitButton);
        Glide.with(this).load(imageTrunkId).into(trunkButton);
        //Glide.with(this).load(imageOtherId).into(otherButton);
        Glide.with(this).load(R.drawable.ic_tree_other).into(otherButton);
    }

    private void setupOnClickListener() {
        treeProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), WantedPosterTreeActivity.class);
            intent.putExtra("TreeId", tree.getId());
            intent.putExtra("TabId", 1);
            startActivity(intent);
        });
        leafButton.setOnClickListener(this.getCategoryButtonOnClickListener(Tree.GameCategories.leaf));
        fruitButton.setOnClickListener(this.getCategoryButtonOnClickListener(Tree.GameCategories.fruit));
        trunkButton.setOnClickListener(this.getCategoryButtonOnClickListener(Tree.GameCategories.trunk));
        otherButton.setOnClickListener(this.getCategoryButtonOnClickListener(Tree.GameCategories.other));
    }

    private Button.OnClickListener getCategoryButtonOnClickListener(final Tree.GameCategories category) {
        return v -> {
            Intent intent = new Intent(getContext(), GameSelectionActivity.class);
            intent.putExtra("TreeId", tree.getId());
            intent.putExtra("Category", category);
            startActivity(intent);
        };
    }

    void presentMaterialTapTargetSequence() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean name = preferences.getBoolean("single", false);
        if (name == false) {
            new MaterialTapTargetSequence()
                    .addPrompt(new CustomTapTargetPromptBuilder(getActivity())
                            .setTarget(R.id.detail_single_tree_circularProgressbar_other)
                            .setFocalRadius(R.dimen.target_radius_small)
                            .setPrimaryText(R.string.other_heading)
                            .setSecondaryText(R.string.other_text))
                    .addPrompt(new CustomTapTargetPromptBuilder(getActivity())
                            .setTarget(R.id.detail_single_tree_circularProgressbar_leaf)
                            .setFocalRadius(R.dimen.target_radius_small)
                            .setPrimaryText(R.string.leaf_heading)
                            .setSecondaryText(R.string.leaf_text))
                    .addPrompt(new CustomTapTargetPromptBuilder(getActivity())
                            .setTarget(R.id.detail_single_tree_circularProgressbar_fruit)
                            .setFocalRadius(R.dimen.target_radius_small)
                            .setPrimaryText(R.string.fruit_heading)
                            .setSecondaryText(R.string.fruit_text))
                    .addPrompt(new CustomTapTargetPromptBuilder(getActivity())
                            .setTarget(R.id.detail_single_tree_circularProgressbar_trunk)
                            .setFocalRadius(R.dimen.target_radius_small)
                            .setPrimaryText(R.string.trunk_heading)
                            .setSecondaryText(R.string.trunk_text))
                    .addPrompt(new CustomTapTargetPromptBuilder(getActivity())
                            .setTarget(R.id.detail_single_tree_profileButton)
                            .setFocalRadius(R.dimen.target_radius_big)
                            .setPrimaryText(R.string.profile_heading)
                            .setSecondaryText(R.string.profile_text))
                    .show();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("single", true);
        editor.apply();
    }
}
