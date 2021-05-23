package com.android.leafter.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leafter.R;
import com.android.leafter.fragments.books.BooksFragment;
import com.android.leafter.fragments.catalogue.CatalogueFragment;
import com.android.leafter.fragments.music.MusicFragment;
import com.android.leafter.fragments.writer.WriterFragment;
import com.android.leafter.models.Book;
import com.android.leafter.models.Metadata;
import com.android.leafter.persistence.database.DatabaseAPI;
import com.android.leafter.persistence.database.Instance;
import com.android.leafter.persistence.filesystem.FileAPI;
import com.android.leafter.util.SortBy;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import java.io.File;
import java.util.List;

public class Book_List_Activity extends AppCompatActivity {

    private static final String SORTORDER_KEY = "sortorder";
    private static final String LASTSHOW_STATUS_KEY = "LastshowStatus";
    private static final String STARTWITH_KEY = "startwith";
    private static final String ENABLE_SCREEN_PAGE_KEY = "screenpaging";
    private static final String ENABLE_DRAG_SCROLL_KEY = "dragscroll";

    private static final int STARTLASTREAD = 1;
    private static final int STARTOPEN = 2;
    private static final int STARTALL = 3;

    private static final String ACTION_SHOW_OPEN = "com.quaap.bookymcbookface.SHOW_OPEN_BOOKS";
    private static final String ACTION_SHOW_UNREAD = "com.quaap.bookymcbookface.SHOW_UNREAD_BOOKS";
    public static final String ACTION_SHOW_LAST_STATUS = "com.quaap.bookymcbookface.SHOW_LAST_STATUS";

    private DrawerLayout drawer;
    MaterialToolbar topnavBar;

    private SharedPreferences data;
    private DatabaseAPI db;
    private int recentread;
    private boolean showingSearch;
    private int showStatus = DatabaseAPI.STATUS_ANY;
    public final String SHOW_STATUS = "showStatus";
    public final static String prefname = "booklist";
    private boolean openLastread = false;
    private static boolean alreadyStarted=false;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        data = getSharedPreferences(prefname, Context.MODE_PRIVATE);
        db = Instance.getDB(this);
        recentread = db.getMostRecentlyRead();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               fetchBook();
                importBook();
            }
        });


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
    protected void onNewIntent(Intent intent) {
        //Log.d("BOOKLIST", "onNewIntent");
        super.onNewIntent(intent);
        processIntent(intent);
    }

    private void processIntent(Intent intent) {

        recentread = db.getMostRecentlyRead();

        showStatus = DatabaseAPI.STATUS_ANY;

        openLastread = false;

        boolean hadSpecialOpen = false;
        //Intent intent = getIntent();
        if (intent != null) {
            if (intent.getAction() != null) {
                switch (intent.getAction()) {
                    case ACTION_SHOW_OPEN:
                        showStatus = DatabaseAPI.STATUS_STARTED;
                        hadSpecialOpen = true;
                        break;
                    case ACTION_SHOW_UNREAD:
                        showStatus = DatabaseAPI.STATUS_NONE;
                        hadSpecialOpen = true;
                        break;
                    case ACTION_SHOW_LAST_STATUS:
                        showStatus = data.getInt(LASTSHOW_STATUS_KEY, DatabaseAPI.STATUS_ANY);
                        hadSpecialOpen = true;
                        break;
                }

            }
        }

        if (!hadSpecialOpen){

            switch (data.getInt(STARTWITH_KEY, STARTLASTREAD)) {
                case STARTLASTREAD:
                    if (recentread!=-1 && data.getBoolean(Reader_Activity.READEREXITEDNORMALLY, true)) openLastread = true;
                    break;
                case STARTOPEN:
                    showStatus = DatabaseAPI.STATUS_STARTED; break;
                case STARTALL:
                    showStatus = DatabaseAPI.STATUS_ANY;
            }
        }


    }

    private static final int REQUEST_READ_EXTERNAL_STORAGE_NOYAY = 4333;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 4334;

    private boolean checkStorageAccess(boolean yay) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    yay? REQUEST_READ_EXTERNAL_STORAGE : REQUEST_READ_EXTERNAL_STORAGE_NOYAY);
            return false;
        }
        return true;
    }

    private boolean addBook(String filename) {
        return addBook(filename, true, System.currentTimeMillis());
    }

    private boolean addBook(String filename, boolean showToastWarnings, long dateadded) {

        try {
            if (db.containsBook(filename)) {

                if (showToastWarnings) {
                    Toast.makeText(this, getString(R.string.already_added, new File(filename).getName()), Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            Metadata metadata = Book.getMetaData(this, filename);

            if (metadata!=null) {

                return db.addBook(filename, metadata.getTitle(), metadata.getAuthor(), dateadded) > -1;

            } else if (showToastWarnings) {
                Toast.makeText(this, getString(R.string.coulndt_add_book, new File(filename).getName()),Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("BookList", "File: " + filename  + ", " + e.getMessage(), e);
        }
        return false;
    }

    private void searchBooks(String searchfor, boolean stitle, boolean sauthor) {
        showStatus = DatabaseAPI.STATUS_SEARCH;
        data.edit().putInt(LASTSHOW_STATUS_KEY, showStatus).apply();
        List<Integer> books = db.searchBooks(searchfor, stitle, sauthor);
        populateBooks(books, false);
        Book_List_Activity.this.setTitle(getString(R.string.search_res_title, searchfor, books.size()));
        showingSearch = true;
        invalidateOptionsMenu();
    }

    private void populateBooks() {
        populateBooks(DatabaseAPI.STATUS_ANY);
    }

    private void populateBooks(int status) {
        //Log.d("BOOKLIST", "populateBooks " + status);
        showStatus = status;
        data.edit().putInt(LASTSHOW_STATUS_KEY, showStatus).apply();

        boolean showRecent = false;
        int title = R.string.app_name;
        switch (status) {
            case DatabaseAPI.STATUS_SEARCH:
                String lastSearch = data.getString("__LAST_SEARCH_STR__","");
                if (!lastSearch.trim().isEmpty()) {
                    boolean stitle = data.getBoolean("__LAST_TITLE__", true);
                    boolean sauthor = data.getBoolean("__LAST_AUTHOR__", true);
                    searchBooks(lastSearch, stitle, sauthor);
                    return;
                }
            case DatabaseAPI.STATUS_ANY:
                title = R.string.book_status_any;
                showRecent = true;
                showingSearch = false;
                break;
            case DatabaseAPI.STATUS_NONE:
                title = R.string.book_status_none;
                showingSearch = false;
                break;
            case DatabaseAPI.STATUS_STARTED:
                title = R.string.book_status_started;
                showRecent = true;
                showingSearch = false;
                break;
            case DatabaseAPI.STATUS_DONE:
                title = R.string.book_status_completed2;
                showingSearch = false;
                break;
            case DatabaseAPI.STATUS_LATER:
                title = R.string.book_status_later2;
                showingSearch = false;
                break;
        }

        Book_List_Activity.this.setTitle(title);

        SortBy sortorder = getSortOrder();
        final List<Integer> books = db.getBookIds(sortorder, status);
        populateBooks(books,  showRecent);

        invalidateOptionsMenu();
    }

    private void populateBooks(final List<Integer> books, boolean showRecent) {

        if (showRecent) {
            recentread = db.getMostRecentlyRead();
            if (recentread >= 0) {
                //viewAdder.displayBook(recentread);
                books.remove((Integer) recentread);
                books.add(0, (Integer)recentread);
            }
        }

//        bookAdapter.setBooks(books);
    }

    @NonNull
    private SortBy getSortOrder() {

        try {
            return SortBy.valueOf(data.getString(SORTORDER_KEY, SortBy.Default.name()));
        } catch (IllegalArgumentException e) {
            Log.e("Booklist", e.getMessage(), e);
            return SortBy.Default;
        }
    }

    private void importBook() {
        FileAPI fileAPI = new FileAPI(this);

        if (checkStorageAccess(false)) {
            fileAPI.selectExternalLocation(new FileAPI.SelectionMadeListener() {
                @Override
                public void selected(File selection) {
                    addBook(selection.getPath());
                    populateBooks();
                }
            }, getString(R.string.find_book), false, Book.getFileExtensionRX());
        }
    }

    private void fetchBook() {
        Intent intent = new Intent(getApplicationContext(), Fetch_Book_Activity.class);
        // intent.putExtra("BookTitle", title);
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public static String maxlen(String text, int maxlen) {
        if (text!=null && text.length() > maxlen) {
            int minus = text.length()>3?3:0;

            return text.substring(0, maxlen-minus) + "...";
        }
        return text;
    }
}