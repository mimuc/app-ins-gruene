package de.lmu.treeapp.service;

import android.view.MenuItem;
import androidx.annotation.NonNull;
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
    private FragmentManager fragmentManager;
    private Fragment currentActiveFragment;

    /**
     * Set the FragmentManager.
     */
    private FragmentManagerService(FragmentManager supportFragmentManager){
        if (sSoleInstance != null){
            throw new RuntimeException("FragmentManagerService - Use getInstance() method to get the single instance of this class.");
        }
        this.fragmentManager = supportFragmentManager;
    }

    /**
     * Set up our FragmentManagerService-Singleton
     * @param supportFragmentManager The reference to the supportFragmentManager (gotten in any Activity by getSupportFragmentManager())
     * @return  The instance of our class.
     */
    public static FragmentManagerService getInstance(FragmentManager supportFragmentManager) {
        if (sSoleInstance == null) {
            synchronized (FragmentManagerService.class) {
                if (sSoleInstance == null) sSoleInstance = new FragmentManagerService(supportFragmentManager);
            }
        }
        return sSoleInstance;
    }

    /**
     * Set-Up the fragments and their transactions.
     * @param fragmentsToShow   The array containing all the fragments to show (as of now -> only 2).
     */
    public void registerTransactions(Fragment[] fragmentsToShow) {
        for (Fragment fragment: fragmentsToShow) {
            if(fragment == fragmentsToShow[0]) {
                this.fragmentManager.beginTransaction().add(R.id.main_container, fragment, fragment.getTag()).commit();
                this.currentActiveFragment = fragment;
                continue;
            }
            this.fragmentManager.beginTransaction().add(R.id.main_container, fragment, fragment.getTag()).hide(fragment).commit();
        }
    }

    /**
     * Activate and show a fragment.
     * @param fragment The fragment to activate.
     */
    public void showFragment(Fragment fragment) {
        if(fragment != this.currentActiveFragment) {
            fragmentManager.beginTransaction().hide(this.currentActiveFragment).show(fragment).commit();
            this.currentActiveFragment = fragment;
        }
    }

    /**
     * Functionality for the BottomNavigation which contains the overview and detail buttons.
     * @param overviewFragment The fragment of the trees-overview.
     * @param treeSelectionFragment Te fragment of the trees-detail.
     * @return
     */
    public BottomNavigationView.OnNavigationItemSelectedListener getOnNavigationItemSelectedListener(final Fragment overviewFragment, final Fragment treeSelectionFragment) {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    // If the Overview-Button is clicked -> Show Overview-Fragment
                    case R.id.action_overview:
                        showFragment(overviewFragment);
                        break;
                    // If the Detail-Button is clicked -> Show Detail-Fragment
                    case R.id.action_tree_selection:
                        showFragment(treeSelectionFragment);
                        break;
                }
                return true;
            }
        };
    }
}