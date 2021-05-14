package com.android.leafter.ui.music;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.leafter.R;
import com.android.leafter.ui.music.musicRecycleView.Music;
import com.android.leafter.ui.music.musicRecycleView.RecycleViewAdapter;
import com.android.leafter.ui.search.SearchViewModel;

import java.util.ArrayList;


public class MusicFragment extends Fragment {

    private MusicViewModel musicViewModel;
    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;

    ArrayList<Music> arrayList=new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        musicViewModel=
                new ViewModelProvider(this).get(MusicViewModel.class);
        arrayList.add(new Music("The End of the end","Hamza Boucherit"));
        arrayList.add(new Music("sories","Hakonamatata"));
        arrayList.add(new Music("la Candela","XoverX"));
        arrayList.add(new Music("la dela","XorX"));
        arrayList.add(new Music("la scana","XoveX"));
        arrayList.add(new Music("sories","Hakonamatata"));
        arrayList.add(new Music("la Candela","XoverX"));
        arrayList.add(new Music("la dela","XorX"));
        arrayList.add(new Music("la hamza","pikeup"));

        return inflater.inflate(R.layout.music_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context=view.getContext();
        recyclerView=view.findViewById(R.id.RecycleView);
        adapter=new RecycleViewAdapter(context);
        adapter.setMusic(arrayList);
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