package com.example.keystone_try.ui.OpenData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OpendataViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public OpendataViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Open data fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
