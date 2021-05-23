package com.android.leafter.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.leafter.R;
import com.android.leafter.persistence.database.DatabaseAPI;
import com.android.leafter.persistence.database.Instance;
import com.android.leafter.util.InsertBookHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;

public class Book_Info_Activity extends AppCompatActivity {

    private static final String ENABLE_SCREEN_PAGE_KEY = "screenpaging";
    private static final String ENABLE_DRAG_SCROLL_KEY = "dragscroll";

    private InsertBookHandler viewAdder;

    private DatabaseAPI db;
    private int recentread;

    private SharedPreferences data;
    public final static String prefname = "booklist";

    private DatabaseAPI.BookRecord book;

    private TextView tv;
    private FloatingActionButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__info);

        viewAdder = new InsertBookHandler(this);
        data = getSharedPreferences(prefname, Context.MODE_PRIVATE);
        db = Instance.getDB(this);
        recentread = db.getMostRecentlyRead();
        tv = findViewById(R.id.progress_text);

        if(this.getIntent().getExtras() != null){
            String bookID = this.getIntent().getExtras().getString("BookID");
            Log.d("BOOKID in BookInfo", bookID);
            book = db.getBookRecord(Integer.parseInt(bookID));

            if (book!=null && book.filename!=null) {
                TextView title = this.findViewById(R.id.textView);
                TextView author = this.findViewById(R.id.textView2);

                title.setText(book.title);
                author.setText(book.author);
            }

            btn = findViewById(R.id.readbtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    readBook(Integer.parseInt(bookID));
                }
            });
        }
    }

    private void readBook(final int bookid) {

         book = db.getBookRecord(bookid);

        if (book!=null && book.filename!=null) {
            //data.edit().putString(LASTREAD_KEY, BOOK_PREFIX + book.id).apply();

            final long now = System.currentTimeMillis();
            db.updateLastRead(bookid, now);
            recentread = bookid;

            viewAdder.postDelayed(new Runnable() {
                @Override
                public void run() {

                    getReader(book,true);

                }
            }, 300);

//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
//
//                try {
//
//                    ShortcutManager shortcutManager = (ShortcutManager) getSystemService(Context.SHORTCUT_SERVICE);
//                    if (shortcutManager!=null) {
//                        Intent readBook = getReader(book,false);
//
//
//                        ShortcutInfo readShortcut = new ShortcutInfo.Builder(this, "id1")
//                                .setShortLabel(getString(R.string.shortcut_latest))
//                                .setLongLabel(getString(R.string.shortcut_latest_title, maxlen(book.title, 24)))
//                                .setIcon(Icon.createWithResource(Book_Info_Activity.this, R.mipmap.ic_launcher_round))
//                                .setIntent(readBook)
//                                .build();
//
//
//
//                        shortcutManager.setDynamicShortcuts(Collections.singletonList(readShortcut));
//                    }
//                } catch(Exception e) {
//                    Log.e("Booky", e.getMessage(), e);
//                }
//            }


        }
    }

    private Intent getReader(DatabaseAPI.BookRecord book, boolean start) {
        Intent readBook = new Intent(Book_Info_Activity.this, Reader_Activity.class);
        readBook.putExtra(Reader_Activity.FILENAME, book.filename);
        readBook.putExtra(Reader_Activity.SCREEN_PAGING, data.getBoolean(ENABLE_SCREEN_PAGE_KEY, true));
        readBook.putExtra(Reader_Activity.DRAG_SCROLL, data.getBoolean(ENABLE_DRAG_SCROLL_KEY, true));
        readBook.setAction(Intent.ACTION_VIEW);
        if (start) {
//            bookAdapter.notifyItemIdChanged(book.id);
            startActivity(readBook);
        }
        return readBook;
    }

    public void showProgress(int added) {

        if (tv.getVisibility() != View.VISIBLE) {
            tv.setVisibility(View.VISIBLE);
            tv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        if (added>0) {
            tv.setText(getString(R.string.added_numbooks, added));
        } else {
            tv.setText(R.string.loading);
        }
    }

    public void hideProgress() {
        tv.setVisibility(View.GONE);
    }
}