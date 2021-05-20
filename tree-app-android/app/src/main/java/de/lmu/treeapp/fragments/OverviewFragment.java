package de.lmu.treeapp.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.MainActivity;
import de.lmu.treeapp.adapter.OverviewRecyclerViewAdapter;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.service.FragmentManagerService;
import de.lmu.treeapp.service.MainActivityViewModel;

import java.util.List;

public class OverviewFragment extends Fragment {

    private FragmentManagerService fragmentManager;
    private RecyclerView overviewRecyclerView;
    private MainActivityViewModel viewModel;

    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = FragmentManagerService.getInstance(getParentFragmentManager());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        overviewRecyclerView = view.findViewById(R.id.overview_recycler_view);

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        setupOverviewRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateOverviewRecyclerView();
    }

    private void setupOverviewRecyclerView() {
        overviewRecyclerView.setHasFixedSize(true);
        // Also possible to use Overview_AutofitRecyclerView.java:
        int gridColumns = 3;
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getContext(), gridColumns);
        overviewRecyclerView.setLayoutManager(recyclerViewLayoutManager);

    }

    private void updateOverviewRecyclerView() {
        List<Tree> trees = DataManager.getInstance(getContext()).trees;
        OverviewRecyclerViewAdapter recyclerViewAdapter = new OverviewRecyclerViewAdapter(trees, fragmentManager, viewModel);
        recyclerViewAdapter.setActivity(this.getActivity());
        overviewRecyclerView.setAdapter(recyclerViewAdapter);
        overviewRecyclerView
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(() -> {
                    // At this point the layout is complete and the
                    // dimensions of recyclerView and any child views
                    // are known.
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
                    boolean name = preferences.getBoolean("show", false);
                    if (!name) {
                        ImageButton b = OverviewRecyclerViewAdapter.firstTree;
                        ImageView lock = OverviewRecyclerViewAdapter.treeLocked;
                        ((MainActivity) requireActivity()).presentMaterialTapTargetSequence(b, lock);
                    }
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("show", true);
                    editor.apply();
                });
    }
}
