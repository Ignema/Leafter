package com.android.leafter.fragments.books;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.leafter.activities.Book_Info_Activity;
import com.android.leafter.R;
import com.android.leafter.adapters.BookAdapter;
import com.android.leafter.persistence.database.DatabaseAPI;
import com.android.leafter.persistence.database.Instance;
import com.android.leafter.util.SortBy;

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

        DatabaseAPI db = Instance.getDB(requireContext());

        RecyclerView recyclerView = requireView().findViewById(R.id.book_fragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        BookAdapter bookAdapter = new BookAdapter(requireContext(), db, db.getBookIds(SortBy.Default, DatabaseAPI.STATUS_ANY));
        bookAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                readBook((int)view.getTag());
                Log.d("BOOKID", view.getTag().toString());
                myClick(getView(), view.getTag().toString());
            }
        });

        recyclerView.setAdapter(bookAdapter);
    }

    private void myClick(View view, String bookid) {
        Intent intent = new Intent(getContext(), Book_Info_Activity.class);
        intent.putExtra("BookID", bookid);
        this.startActivity(intent);
    }
}