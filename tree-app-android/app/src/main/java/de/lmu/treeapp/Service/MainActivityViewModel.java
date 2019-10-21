package de.lmu.treeapp.Service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<Integer> currentPagerIndex = new MutableLiveData<>();

    public LiveData<Integer> getCurrentPagerIndex() {
        return currentPagerIndex;
    }
    public void setCurrentPagerIndex(int index){
        currentPagerIndex.setValue(index);
    }



}
