package de.lmu.treeapp.service;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;

import de.lmu.treeapp.R;

/**
 * The FragmentManagerService is a Singleton used to prepare and launch the fragments (The overview and detail views of the trees in the MainActivity).
 * Guide to Fragments: https://developer.android.com/guide/components/fragments
 */
public class FragmentManagerService implements Serializable {
    private static volatile FragmentManagerService sSoleInstance;
    private final FragmentManager fragmentManager;
    private Fragment currentActiveFragment;

    /**
     * Set the FragmentManager.
     */
    private FragmentManagerService(FragmentManager supportFragmentManager) {
        if (sSoleInstance != null) {
            throw new RuntimeException("FragmentManagerService - Use getInstance() method to get the single instance of this class.");
        }
        this.fragmentManager = supportFragmentManager;
    }

    /**
     * Set up our FragmentManagerService-Singleton
     *
     * @param supportFragmentManager The reference to the supportFragmentManager (gotten in any Activity by getSupportFragmentManager())
     * @return The instance of our class.
     */
    public static FragmentManagerService getInstance(FragmentManager supportFragmentManager) {
        if (sSoleInstance == null) {
            synchronized (FragmentManagerService.class) {
                if (sSoleInstance == null) // double checked locking
                    sSoleInstance = new FragmentManagerService(supportFragmentManager);
            }
        }
        return sSoleInstance;
    }

    /**
     * Set-Up the fragments and their transactions.
     *
     * @param fragmentsToShow The array containing all the fragments to show (as of now -> only 2).
     */
    public void registerTransactions(Fragment[] fragmentsToShow) {
        for (Fragment fragment : fragmentsToShow) {
            if (fragment == fragmentsToShow[0]) {
                this.fragmentManager.beginTransaction().add(R.id.main_container, fragment, fragment.getTag()).commit();
                //this.fragmentManager.beginTransaction().replace(R.id.main_container, fragment).addToBackStack("my_fragment").commit();
                this.currentActiveFragment = fragment;
            } else {
                this.fragmentManager.beginTransaction().add(R.id.main_container, fragment, fragment.getTag()).hide(fragment).commit();
                //this.fragmentManager.beginTransaction().replace(R.id.main_container, fragment).addToBackStack("my_fragment").commit();
            }
        }
    }

    // Show overview and reset the isImprint variable to show columns of overview correctly
    public void showOverview(Fragment[] fragmentsToShow) {
        showFragment(fragmentsToShow[0]);
    }

    /**
     * Activate and show a fragment.
     *
     * @param fragment The fragment to activate.
     */
    public void showFragment(Fragment fragment) {
        if (fragment != this.currentActiveFragment) {
            fragmentManager.beginTransaction().hide(this.currentActiveFragment).show(fragment).commit();
            this.currentActiveFragment = fragment;
        }
    }

    /**
     * Get current active fragment.
     */
    public Fragment getCurrentActiveFragment() {
        return this.currentActiveFragment;
    }

    /**
     * Functionality for the BottomNavigation which contains the overview and detail buttons.
     *
     * @param overviewFragment      The fragment of the trees-overview.
     * @param treeSelectionFragment The fragment of the trees-detail.
     * @param profileFragment       The fragment of profile.
     * @return
     */
    public BottomNavigationView.OnNavigationItemSelectedListener getOnNavigationItemSelectedListener(
            final Fragment overviewFragment,
            final Fragment treeSelectionFragment,
            final Fragment profileFragment

    ) {
        return item -> {
            switch (item.getItemId()) {
                // If the Overview-Button is clicked -> Show Overview-Fragment
                case R.id.action_overview:
                    showFragment(overviewFragment);
                    break;
                // If the Detail-Button is clicked -> Show Detail-Fragment
                case R.id.action_tree_selection:
                    showFragment(treeSelectionFragment);
                    break;
                case R.id.action_profile:
                    showFragment(profileFragment);
                    break;
            }
            return true;
        };
    }

    public void onDestroy() {
        sSoleInstance = null;
    }
}