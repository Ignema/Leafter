package com.android.leafter;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BooksFragment extends Fragment {


    public BooksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lv = (ListView) requireView().findViewById(R.id.book_fragment_listview);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> your_array_list = new ArrayList<String>();
        your_array_list.add("Lord of the Rings");
        your_array_list.add("Hobbit");
        your_array_list.add("1984");
        your_array_list.add("Harry Potter and the Philosopher's Stone");
        your_array_list.add("The Great Gatsby");
        your_array_list.add("The Book Thief");

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                requireContext(),
                R.layout.book_tile,
                R.id.textView15,
                your_array_list
        );

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                myClick(getView(), arrayAdapter.getItem(arg2));
            }
        });
        lv.setAdapter(arrayAdapter);
    }

    private void myClick(View view, String title) {
        Intent intent = new Intent(getContext(), Book_Info.class);
        intent.putExtra("BookTitle", title);
        this.startActivity(intent);
    }
}