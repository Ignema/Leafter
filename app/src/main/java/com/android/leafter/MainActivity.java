package com.android.leafter;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.leafter.ui.music.MusicFragment;
import com.android.leafter.ui.search.SearchViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.miHome, R.id.miMusic, R.id.miCatalogue,R.id.miWriterChoose)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.top_nav_menu,menu);
        MenuItem searchItem=menu.findItem(R.id.search);
        SearchView searchView= (SearchView) searchItem.getActionView();
        SearchViewModel searchViewModel=new ViewModelProvider(this).get(SearchViewModel.class);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchViewModel.setQuery(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }



}