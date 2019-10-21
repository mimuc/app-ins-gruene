package de.lmu.treeapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.Service.MainActivityViewModel;
import de.lmu.treeapp.adapter.OverviewRecyclerViewAdapter;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.service.FragmentManagerService;

public class OverviewFragment extends Fragment {

    private FragmentManagerService fragmentManager;
    private Fragment selectedTreeFragment;
    private RecyclerView overviewRecyclerView;
    private MainActivityViewModel viewModel;

    public OverviewFragment(FragmentManagerService fragmentManager, Fragment selectedTreeFragment) {
        this.fragmentManager = fragmentManager;
        this.selectedTreeFragment = selectedTreeFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        overviewRecyclerView = view.findViewById(R.id.overview_recycler_view);

        viewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
        setupOverviewRecyclerView();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateOverviewRecyclerView();
    }
    private void setupOverviewRecyclerView() {
        overviewRecyclerView.setHasFixedSize(true);
        int gridColumns = 3;
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getContext(), gridColumns);
        overviewRecyclerView.setLayoutManager(recyclerViewLayoutManager);
    }
    private void updateOverviewRecyclerView(){
        List<Tree> trees = DataManager.getInstance(getContext()).trees;
        RecyclerView.Adapter recyclerViewAdapter = new OverviewRecyclerViewAdapter(trees, fragmentManager, selectedTreeFragment, viewModel);
        ((OverviewRecyclerViewAdapter) recyclerViewAdapter).setActivity(this.getActivity());
        overviewRecyclerView.setAdapter(recyclerViewAdapter);
    }
}
