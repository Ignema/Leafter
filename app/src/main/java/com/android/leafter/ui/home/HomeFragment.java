package com.android.leafter.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.leafter.R;
import com.android.leafter.ui.home.BooksRecycleView.Books;
import com.android.leafter.ui.home.BooksRecycleView.RecycleViewAdapter;
import com.android.leafter.ui.search.SearchViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    ArrayList<Books> arrayList=new ArrayList<>();
    private RecycleViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        arrayList.add(new Books("The End of the end","Hamza Boucherit"));
        arrayList.add(new Books("sories","Hakonamatata"));
        arrayList.add(new Books("la Candela","XoverX"));
        arrayList.add(new Books("la dela","XorX"));
        arrayList.add(new Books("la scana","XoveX"));
        arrayList.add(new Books("sories","Hakonamatata"));
        arrayList.add(new Books("la Candela","XoverX"));
        arrayList.add(new Books("la dela","XorX"));
        arrayList.add(new Books("la scana","XoveX"));



        return  inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context=view.getContext();
        recyclerView=view.findViewById(R.id.RecView);
        adapter=new RecycleViewAdapter(context);
        adapter.setBooks(arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));

        SearchViewModel searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);
        searchViewModel.getQuery().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    adapter.getFilter().filter(s);
                }
            }
        });

    }


}