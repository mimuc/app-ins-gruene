package de.lmu.treeapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.GameSelectionActivity;
import de.lmu.treeapp.activities.WantedPosterDetailsActivity;
import de.lmu.treeapp.contentClasses.trees.Tree;

public class DetailSingleTreeFragment extends Fragment {

    private TextView treeName;
    private ImageView treeImage;
    private Button treeProfileButton;
    private ImageButton leafButton;
    private ImageButton fruitButton;
    private ImageButton trunkButton;

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

    private void findViewsById(ViewGroup rootView) {
        treeName = rootView.findViewById(R.id.detail_single_tree_text);
        treeImage = rootView.findViewById(R.id.detail_single_tree_image);
        treeProfileButton = rootView.findViewById(R.id.detail_single_tree_profileButton);
        leafButton = rootView.findViewById(R.id.detail_single_tree_leafButton);
        fruitButton = rootView.findViewById(R.id.detail_single_tree_fruitButton);
        trunkButton = rootView.findViewById(R.id.detail_single_tree_trunkButton);
    }

    private void setupSingleTreeContent() {
        treeName.setText(tree.name);

        if(getContext() != null) {
            this.setupImageResources();
            this.setupOnClickListener();
        }
    }

    private void setupImageResources() {
        int imageTreeId = Objects.requireNonNull(getContext()).getResources().getIdentifier(tree.imageTree, "drawable", getContext().getPackageName());
        int imageLeafId = getContext().getResources().getIdentifier(tree.imageLeaf, "drawable", getContext().getPackageName());
        int imageFruitId = getContext().getResources().getIdentifier(tree.imageFruit, "drawable", getContext().getPackageName());
        int imageTrunkId = getContext().getResources().getIdentifier(tree.imageTrunk, "drawable", getContext().getPackageName());
        treeImage.setImageResource(imageTreeId);
        leafButton.setImageResource(imageLeafId);
        fruitButton.setImageResource(imageFruitId);
        trunkButton.setImageResource(imageTrunkId);
    }

    private void setupOnClickListener() {
        treeProfileButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WantedPosterDetailsActivity.class);
                intent.putExtra("TreeId", tree.uid);
                startActivity(intent);
            }
        });
        leafButton.setOnClickListener(this.getCategoryButtonOnClickListener(Tree.GameCategories.leaf));
        fruitButton.setOnClickListener(this.getCategoryButtonOnClickListener(Tree.GameCategories.fruit));
        trunkButton.setOnClickListener(this.getCategoryButtonOnClickListener(Tree.GameCategories.trunk));
    }

    private Button.OnClickListener getCategoryButtonOnClickListener(final Tree.GameCategories category ) {
        return new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GameSelectionActivity.class);
                intent.putExtra("TreeId", tree.uid);
                intent.putExtra("Category", category);

                startActivity(intent);
            }
        };
    }

}