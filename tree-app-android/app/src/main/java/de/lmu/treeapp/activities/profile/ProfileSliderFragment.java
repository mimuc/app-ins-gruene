package de.lmu.treeapp.activities.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.viewpagerdots.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.Imprint;
import de.lmu.treeapp.adapter.ProfileSlidePagerAdapter;
import de.lmu.treeapp.contentData.database.AppDatabase;
import de.lmu.treeapp.contentData.database.entities.app.UserProfileState;
import de.lmu.treeapp.service.MainActivityViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfileSliderFragment extends Fragment {

    public static int PROFILE_SLIDER_FRAGMENT_CODE = 1337;
    public static String PAGER_USER_ID = "pagerUserId";

    private Context context;
    private ViewPager pager;
    private Toolbar toolbar;
    private DotsIndicator dotsIndicator;
    List<UserProfileState> profileList;

    public ProfileSliderFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_slider, container, false);
        this.findViewsById(view);
        this.setupViewPager(0);
        this.setupViewModelObserving();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.profile_app_bar);
        toolbar.inflateMenu(R.menu.profile_menu);

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.open_imprint:
                    this.context = getContext();
                    Intent intent_imprint = new Intent(context, Imprint.class);
                    context.startActivity(intent_imprint);
                    return true;
                case R.id.profile_add:
                    UserProfileState user = new UserProfileState();
                    AppDatabase.getInstance(getContext()).userProfileDao().insertOne(user).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                        user.id = s.intValue(); // Update id of newly inserted user
                        this.profileList.add(user);

                        FragmentManager fragManager = getParentFragmentManager();
                        ProfileEditFragment profileEditFragment = ProfileEditFragment.newInstance(user, false);
                        profileEditFragment.setTargetFragment(this, PROFILE_SLIDER_FRAGMENT_CODE);
                        fragManager.beginTransaction()
                                .replace(R.id.profile_slider_fragment, profileEditFragment)
                                .addToBackStack(null)
                                .commit();
                    });
                    return true;
                case R.id.profile_edit:
                    FragmentManager fragManager = getParentFragmentManager();

                    ProfileEditFragment profileEditFragment = ProfileEditFragment.newInstance(profileList.get(pager.getCurrentItem()), true);
                    profileEditFragment.setTargetFragment(this, PROFILE_SLIDER_FRAGMENT_CODE);
                    fragManager.beginTransaction()
                            .replace(R.id.profile_slider_fragment, profileEditFragment)
                            .addToBackStack(null)
                            .commit();
                default:
                    return super.onOptionsItemSelected(item);
            }
        });

    }

    private void findViewsById(View view) {
        pager = view.findViewById(R.id.profile_selection_view_pager);
        dotsIndicator = view.findViewById(R.id.profile_selection_dots_indicator);
    }

    private void setupViewPager(int userId) {

        AppDatabase.getInstance(getContext()).userProfileDao().getAll().flatMap(tmpProfiles -> {
            if (tmpProfiles.size() < 1) {
                UserProfileState user = new UserProfileState();
                user.name = null;
                user.age = null;
                user.location = null;
                user.tree = null;
                user.leaf = null;
                user.season = null;
                tmpProfiles.add(user);
                return AppDatabase.getInstance(getContext()).userProfileDao().insertOne(user).flatMap(id -> {
                    user.id = id.intValue();
                    return Single.just(tmpProfiles);
                });
            } else {
                return Single.just(tmpProfiles);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
            this.profileList = s;
            int pagerItemPosition = 0;
            for (int i = 0; i < profileList.size(); i++) {
                if (profileList.get(i).id == userId) {
                    pagerItemPosition = i;
                }
            }
            PagerAdapter adapter = new ProfileSlidePagerAdapter(getParentFragmentManager(), getProfileFragments(profileList));
            pager.setClipToPadding(false);
            pager.setPageMargin(100);
            pager.setAdapter(adapter);
            pager.setCurrentItem(pagerItemPosition);
            dotsIndicator.attachViewPager(pager);
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < 1) {
                    toolbar.setTitle(R.string.profile_thats_me);
                } else {
                    toolbar.setTitle(R.string.profile_friend_book);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewModelObserving() {

        final Observer<Integer> indexObserver = newIndex -> pager.setCurrentItem(newIndex, false);
        MainActivityViewModel viewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
        viewModel.getCurrentPagerIndex().observe(getViewLifecycleOwner(), indexObserver);
    }


    private List<ProfileFragment> getProfileFragments(List<UserProfileState> profiles) {

        List<ProfileFragment> profileFragments = new ArrayList<>();

        if (profiles != null && profiles.size() > 0) {
            for (UserProfileState profile : profiles) {
                profileFragments.add(ProfileFragment.newInstance(profile));
            }
        }

        return profileFragments;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PROFILE_SLIDER_FRAGMENT_CODE && resultCode == Activity.RESULT_OK) {
            int userId = data.getIntExtra(PAGER_USER_ID, 0);
            setupViewPager(userId);
        }
    }
}
