package com.android.leafter.ui.writerChoose;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WriterChooseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WriterChooseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is WriterChoose fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}