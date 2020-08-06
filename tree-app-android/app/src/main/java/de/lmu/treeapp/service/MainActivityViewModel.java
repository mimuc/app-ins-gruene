package de.lmu.treeapp.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * The class MainActivityViewModel is used inside the MainActivity to store and update the currently active tree-card.
 * If someone clicks on a tree in the overview-screen, this should change to the respective tree, so the pager scrolls accordingly.
 * Note that horizontal-scrolling through the trees does not change the Index, since the scroll-state is saved automatically by Android.
 */
public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<Integer> currentPagerIndex = new MutableLiveData<>();

    /**
     * Gets the Index of the current tree-card, that we should scroll to.
     * @return An LiveData-Object of type Integer as the index -> this data may change asynchronously and has to be observed.
     */
    public LiveData<Integer> getCurrentPagerIndex() {
        return currentPagerIndex;
    }

    /**
     * Sets the Index of the current tree-card.
     * @param index The index of the the tree-card which should now be active.
     */
    public void setCurrentPagerIndex(int index){
        currentPagerIndex.setValue(index);
    }



}
