package com.eltech.snc.ui.image_copying;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImageCopyingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ImageCopyingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}