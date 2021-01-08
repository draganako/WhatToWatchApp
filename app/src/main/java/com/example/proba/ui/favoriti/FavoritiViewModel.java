package com.example.proba.ui.favoriti;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavoritiViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FavoritiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Favoriti");
    }

    public LiveData<String> getText() {
        return mText;
    }
}