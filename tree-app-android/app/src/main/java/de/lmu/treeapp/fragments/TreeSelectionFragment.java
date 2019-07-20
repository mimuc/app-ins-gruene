package de.lmu.treeapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.viewpagerdots.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.adapter.TreeSlidePagerAdapter;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

public class TreeSelectionFragment extends Fragment {

    private ViewPager pager;
    private PagerAdapter adapter;
    private DotsIndicator dotsIndicator;

    public TreeSelectionFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tree_selection, container, false);

        pager = view.findViewById(R.id.tree_selection_view_pager);
        dotsIndicator = view.findViewById(R.id.tree_selection_dots_indicator);

        adapter = new TreeSlidePagerAdapter(getFragmentManager(), getDetailSingleTreeFragments(DataManager.getInstance(getContext()).trees));

        pager.setClipToPadding(false);
        pager.setPadding(100, 0, 100, 0);
        pager.setPageMargin(24);
        pager.setAdapter(adapter);

        dotsIndicator.attachViewPager(pager);

        return view;
    }

    private List<DetailSingleTreeFragment> getDetailSingleTreeFragments(List<Tree> trees) {
        List<DetailSingleTreeFragment> detailSingleTreeFragments = new ArrayList<>();
        for (Tree tree : trees) {
            detailSingleTreeFragments.add(new DetailSingleTreeFragment(tree));

        }

        return  detailSingleTreeFragments;
    }
}
