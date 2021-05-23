package com.android.leafter.fragments.catalogue;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.android.leafter.R;
import com.android.leafter.activities.Book_Info_Activity;
import com.android.leafter.activities.Fetch_Book_Activity;
import com.android.leafter.adapters.BookAdapter;
import com.android.leafter.adapters.CatalogueAdapter;
import com.android.leafter.persistence.database.DatabaseAPI;
import com.android.leafter.persistence.database.Instance;
import com.android.leafter.util.SortBy;


public class CatalogueFragment extends Fragment {

    DatabaseAPI db;

    public CatalogueFragment() {
        // Required empty public constructor
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalogue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = Instance.getDB(requireContext());

        RecyclerView recyclerView = requireView().findViewById(R.id.catalogue_fragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        CatalogueAdapter catalogueAdapter = new CatalogueAdapter(requireContext(), db, db.getWebsiteIds());
        catalogueAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClick(getView(), view.getTag().toString());
            }
        });

        recyclerView.setAdapter(catalogueAdapter);
    }


    public void myClick(View v, String ctlgid) {
        try {
            String url = db.getCatalogueRecord(Integer.parseInt(ctlgid)).url;
            if (url != null) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            Log.e("Catalogue", e.getMessage(), e);
        }
    }

//    private void myClick(View view, String ctlgid) {
//        Intent intent = new Intent(getContext(), Fetch_Book_Activity.class);
//        intent.putExtra("BookID", ctlgid);
//        this.startActivity(intent);
//    }
}