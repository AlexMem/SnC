package com.eltech.snc.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Это начальная страница. Здесь будет что-нибудь информативное, например описание краткое описание модуля и его компонентов (мини игр), статистика.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}