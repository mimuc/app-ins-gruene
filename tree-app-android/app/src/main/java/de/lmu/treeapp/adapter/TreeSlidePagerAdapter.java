package de.lmu.treeapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import de.lmu.treeapp.fragments.DetailSingleTreeFragment;

public class TreeSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<DetailSingleTreeFragment> detailSingleTreeFragments;

    public TreeSlidePagerAdapter(FragmentManager fm, List<DetailSingleTreeFragment> detailSingleTreeFragments) {
        super(fm);
        this.detailSingleTreeFragments = detailSingleTreeFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return detailSingleTreeFragments.get(position);
    }

    @Override
    public int getCount() {
        return detailSingleTreeFragments.size();
    }
}
