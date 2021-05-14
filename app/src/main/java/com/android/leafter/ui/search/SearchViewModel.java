package com.android.leafter.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    public MutableLiveData<String> query = new MutableLiveData<String>();
    public void setQuery(String queryData) {

        query.setValue(queryData);
    }
    public LiveData<String> getQuery() {
        return query;
    }
}
