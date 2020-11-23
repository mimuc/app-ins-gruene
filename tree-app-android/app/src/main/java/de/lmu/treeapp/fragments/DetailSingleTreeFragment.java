package de.lmu.treeapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.GameSelectionActivity;
import de.lmu.treeapp.activities.WantedPosterDetailsActivity;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

public class DetailSingleTreeFragment extends Fragment {

    private TextView treeName;
    private ImageView treeImage;
    private Button treeProfileButton;
    private ImageButton leafButton;
    private ImageButton fruitButton;
    private ImageButton trunkButton;
    private ImageButton otherButton;
    private ProgressBar leafProgressBar;
    private ProgressBar fruitProgressBar;
    private ProgressBar trunkProgressBar;
    private ProgressBar otherProgressBar;
    private Tree tree;

    DetailSingleTreeFragment(Tree tree) {
        this.tree = tree;
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
    public void onResume() {
        super.onResume();
        this.tree = DataManager.getInstance(getContext()).GetTree(tree.uid);
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
        treeName.setText(tree.name);

        if (getContext() != null) {
            this.setupImageResources();
            this.setupOnClickListener();
        }
    }

    private void updateTreeView() {
        leafProgressBar.setProgress((int) this.tree.GetGameProgressionPercent(Tree.GameCategories.leaf));
        fruitProgressBar.setProgress((int) this.tree.GetGameProgressionPercent(Tree.GameCategories.fruit));
        trunkProgressBar.setProgress((int) this.tree.GetGameProgressionPercent(Tree.GameCategories.trunk));
        otherProgressBar.setProgress((int) this.tree.GetGameProgressionPercent(Tree.GameCategories.other));
    }

    private void setupImageResources() {
        int imageTreeId = Objects.requireNonNull(getContext()).getResources().getIdentifier(tree.imageTree, "drawable", getContext().getPackageName());
        int imageLeafId = getContext().getResources().getIdentifier(tree.imageLeaf, "drawable", getContext().getPackageName());
        int imageFruitId = getContext().getResources().getIdentifier(tree.imageFruit, "drawable", getContext().getPackageName());
        int imageTrunkId = getContext().getResources().getIdentifier(tree.imageTrunk, "drawable", getContext().getPackageName());
        //int imageOtherId = getContext().getResources().getIdentifier(tree.imageOther, "drawable", getContext().getPackageName());
        treeImage.setImageResource(imageTreeId);
        leafButton.setImageResource(imageLeafId);
        fruitButton.setImageResource(imageFruitId);
        trunkButton.setImageResource(imageTrunkId);
        //otherButton.setImageResource(imageOtherId);
        otherButton.setImageResource(R.drawable.ic_tree_other);
    }

    private void setupOnClickListener() {
        treeProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), WantedPosterDetailsActivity.class);
            intent.putExtra("TreeId", tree.uid);
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
            intent.putExtra("TreeId", tree.uid);
            intent.putExtra("Category", category);
            startActivity(intent);
        };
    }
}
