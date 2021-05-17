package de.lmu.treeapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.viewpagerdots.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.adapter.TreeSlidePagerAdapter;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.service.MainActivityViewModel;

public class TreeSelectionFragment extends Fragment {

    private ViewPager pager;
    private DotsIndicator dotsIndicator;
    private MainActivityViewModel viewModel;

    public TreeSelectionFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tree_selection, container, false);
        this.findViewsById(view);
        this.setupViewPager();
        this.setupViewModelObserving();
        return view;
    }

    private void findViewsById(View view) {
        pager = view.findViewById(R.id.tree_selection_view_pager);
        dotsIndicator = view.findViewById(R.id.tree_selection_dots_indicator);
    }

    private void setupViewPager() {
        PagerAdapter adapter = new TreeSlidePagerAdapter(getFragmentManager(), getDetailSingleTreeFragments(DataManager.getInstance(getContext()).trees));
        pager.setClipToPadding(false);
        pager.setPadding(100, 0, 100, 0);
        pager.setPageMargin(24);
        pager.setAdapter(adapter);
        dotsIndicator.attachViewPager(pager);
    }

    private void setupViewModelObserving() {

        final Observer<Integer> indexObserver = newIndex -> pager.setCurrentItem(newIndex, false);
        viewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
        viewModel.getCurrentPagerIndex().observe(getViewLifecycleOwner(), indexObserver);
    }


    private List<DetailSingleTreeFragment> getDetailSingleTreeFragments(List<Tree> trees) {
        List<DetailSingleTreeFragment> detailSingleTreeFragments = new ArrayList<>();
        for (Tree tree : trees) {
            detailSingleTreeFragments.add(DetailSingleTreeFragment.newInstance(tree));
        }

        return detailSingleTreeFragments;
    }
}
