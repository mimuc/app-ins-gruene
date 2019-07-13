package de.lmu.treeapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import de.lmu.treeapp.R;
import de.lmu.treeapp.service.FragmentManagerService;

public class OverviewFragment extends Fragment {

    private FragmentManagerService fragmentManager;
    private Fragment selectedTreeFragment;

    public OverviewFragment(FragmentManagerService fragmentManager, Fragment selectedTreeFragment) {
        this.fragmentManager = fragmentManager;
        this.selectedTreeFragment = selectedTreeFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

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

    private void switchNavigationButtonToTreeSelection() {
        ((BottomNavigationView) Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.action_tree_selection);
    }
}
