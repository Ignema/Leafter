package com.android.leafter.fragments.writer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WriterViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WriterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Writer fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}