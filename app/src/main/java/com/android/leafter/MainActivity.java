package com.android.leafter;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.leafter.adapters.BookAdapter;
import com.android.leafter.fragments.books.BooksFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookAdapter.ItemClickListener {

    BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
//        bnv.getMenu().getItem(2).setEnabled(false);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.miHome, R.id.miMusic, R.id.miCatalogue, R.id.miWriter)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

//        BooksFragment frag = (BooksFragment) getSupportFragmentManager().findFragmentById(R.id.miHome);
//        System.out.println(frag.toString());
//        if (frag!=null && frag.isResumed()) {
            // data to populate the RecyclerView with
            ArrayList<String> animalNames = new ArrayList<>();
            for(int i=0; i<20; i++){
                animalNames.add("The Lord of the Rings");
                animalNames.add("Abandoned");
                animalNames.add("The Great Gatsby");
                animalNames.add("A song of fire and ice");
                animalNames.add("the Hobbit");
            }

            // set up the RecyclerView
            RecyclerView recyclerView = findViewById(R.id.booksRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            bookAdapter = new BookAdapter(this, animalNames);
            bookAdapter.setClickListener(this);
            recyclerView.setAdapter(bookAdapter);
//        }
//        else {
//            //Whatever
//        }

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + bookAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
//        setContentView(R.layout.activity_main);
//
//        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
////        bnv.getMenu().getItem(2).setEnabled(false);
//
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.miHome, R.id.miMusic, R.id.miCatalogue)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
//
//    }


}