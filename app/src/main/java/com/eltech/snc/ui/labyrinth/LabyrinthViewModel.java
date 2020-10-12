package com.eltech.snc.ui.labyrinth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LabyrinthViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LabyrinthViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}