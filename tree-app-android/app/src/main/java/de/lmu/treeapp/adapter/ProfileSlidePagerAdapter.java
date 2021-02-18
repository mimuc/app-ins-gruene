package de.lmu.treeapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import de.lmu.treeapp.activities.profile.ProfileFragment;

public class ProfileSlidePagerAdapter extends FragmentStatePagerAdapter {

    private final List<ProfileFragment> profileFragments;

    public ProfileSlidePagerAdapter(FragmentManager fm, List<ProfileFragment> profileFragments) {
        super(fm);
        this.profileFragments = profileFragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return profileFragments.get(position);
    }

    @Override
    public int getCount() {
        return profileFragments.size();
    }
}
