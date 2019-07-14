package de.lmu.treeapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.WantedPosterDetailsActivity;
import de.lmu.treeapp.adapter.DetailRecyclerViewAdapter;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

public class TreeSelectionFragment extends Fragment {
    private RecyclerView detailRecyclerView;

    public TreeSelectionFragment() {

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tree_selection, container, false);

        detailRecyclerView = view.findViewById(R.id.detail_recycler_view);

        setupOverviewRecyclerView();



        return view;
    }

    private void setupOverviewRecyclerView() {
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        detailRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        List<Tree> trees = DataManager.getInstance(getContext()).trees;
        RecyclerView.Adapter recyclerViewAdapter = new DetailRecyclerViewAdapter(trees);
        detailRecyclerView.setAdapter(recyclerViewAdapter);
    }
}
