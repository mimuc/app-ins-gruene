package de.lmu.treeapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.lmu.treeapp.R;
import de.lmu.treeapp.adapter.OverviewRecyclerViewAdapter;
import de.lmu.treeapp.service.FragmentManagerService;

public class OverviewFragment extends Fragment {

    private FragmentManagerService fragmentManager;
    private Fragment selectedTreeFragment;
    private RecyclerView overviewRecyclerView;

    public OverviewFragment(FragmentManagerService fragmentManager, Fragment selectedTreeFragment) {
        this.fragmentManager = fragmentManager;
        this.selectedTreeFragment = selectedTreeFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        overviewRecyclerView = view.findViewById(R.id.overview_recycler_view);
        setupOverviewRecyclerView();

        Button overviewWantedPosterButton = view.findViewById(R.id.overview_wanted_poster_button);
        overviewWantedPosterButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.showFragment(selectedTreeFragment);
                switchNavigationButtonToTreeSelection();
            }
        });

        return view;
    }

    private void setupOverviewRecyclerView() {
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        overviewRecyclerView.setHasFixedSize(true);

        int gridColumns = 3;
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getContext(), gridColumns);
        overviewRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        List<String> testInput = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            testInput.add("Test" + i);
        }
        RecyclerView.Adapter recyclerViewAdapter = new OverviewRecyclerViewAdapter(testInput);
        overviewRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void switchNavigationButtonToTreeSelection() {
        ((BottomNavigationView) Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.action_tree_selection);
    }
}
