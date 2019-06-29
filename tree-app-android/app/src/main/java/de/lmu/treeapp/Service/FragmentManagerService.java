package de.lmu.treeapp.Service;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.Serializable;

import de.lmu.treeapp.R;

public class FragmentManagerService implements Serializable {

    private static volatile FragmentManagerService sSoleInstance;
    private FragmentManager fragmentManager;
    private Fragment currentActiveFragment;

    private FragmentManagerService(FragmentManager supportFragmentManager){
        if (sSoleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.fragmentManager = supportFragmentManager;
    }

    public static FragmentManagerService getInstance(FragmentManager supportFragmentManager) {
        if (sSoleInstance == null) {
            synchronized (FragmentManagerService.class) {
                if (sSoleInstance == null) sSoleInstance = new FragmentManagerService(supportFragmentManager);
            }
        }

        return sSoleInstance;
    }

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

    public void showFragment(Fragment fragment) {
        if(fragment != this.currentActiveFragment) {
            fragmentManager.beginTransaction().hide(this.currentActiveFragment).show(fragment).commit();
            this.currentActiveFragment = fragment;
        }
    }

    //Make singleton from serialize and deserialize operation.
    protected FragmentManagerService readResolve(FragmentManager supportFragmentManager) {
        return getInstance(supportFragmentManager);
    }
}