package com.android.leafter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    MaterialToolbar topnavBar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavigationView);
//        --------------------------------------------------------------------------------------------------------

        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                topnavBar = findViewById(R.id.topToolbar);
                String topNavTitle = "";
                switch (item.getItemId()) {
                    case R.id.miHome:
                        selectedFragment = new BooksFragment();
                        topNavTitle = "Reading List";
                        break;

                    case R.id.miMusic:
                        selectedFragment = new MusicFragment();
                        topNavTitle = "Music List";
                        break;
                    case R.id.miCatalogue:
                        selectedFragment = new CatalogueFragment();
                        topNavTitle = "Catalogue List";
                        break;
                    case R.id.miWriter:
                        selectedFragment = new WriterFragment();
                        topNavTitle = "Writer List";
                        break;
                }
//                Begin Transaction
                if(selectedFragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    topnavBar.setTitle(topNavTitle);
                }

                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BooksFragment()).commit();

//        --------------------------------------------------------------------------------------------------------


//        bnv.getMenu().getItem(2).setEnabled(false); // to disable the placeholder item

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.miHome, R.id.miMusic, R.id.miCatalogue, R.id.miWriter)
                .build();
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        Toolbar toolbar = findViewById(R.id.topToolbar);
        //setSupportActionBar(toolbar);


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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}