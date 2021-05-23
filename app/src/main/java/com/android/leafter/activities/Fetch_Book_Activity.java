package com.android.leafter.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leafter.R;
import com.android.leafter.persistence.database.Instance;
import com.android.leafter.persistence.database.DatabaseAPI;

import java.util.Map;

public class Fetch_Book_Activity extends Activity implements View.OnClickListener, View.OnLongClickListener{

    private EditText nameBox;
    private EditText urlBox;

    private LinearLayout list;

    private DatabaseAPI db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_book);

        db = Instance.getDB(this);

        list = findViewById(R.id.webs_list);

        nameBox = findViewById(R.id.web_name);
        urlBox = findViewById(R.id.web_url);
        Button wadd = findViewById(R.id.web_add);

        final Button wnew = findViewById(R.id.web_new);
        final LinearLayout add_layout= findViewById(R.id.web_add_layout);


        wadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (urlBox.getText().length()>0) {
                    String url = urlBox.getText().toString();
                    String name = url.replaceAll("^https?://([\\w\\-.])(/.*)","$1");
                    if (nameBox.getText().length()>0) {
                        name = nameBox.getText().toString();
                    }
                    db.addWebsite(name, url);
                    displayWeb(name, url, true);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm!=null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    add_layout.setVisibility(View.GONE);
                    wnew.setVisibility(View.VISIBLE);
                }
            }
        });

        wnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_layout.setVisibility(View.VISIBLE);
                wnew.setVisibility(View.GONE);
            }
        });



        Map<String,String> webs = db.getWebSites();

        for (Map.Entry<String,String> web: webs.entrySet()) {
            displayWeb(web.getValue(), web.getKey());
        }

    }

    private void displayWeb(String name, String url) {
        displayWeb(name, url, false);
    }

    private void displayWeb(String name, String url, boolean first) {
        TextView v = new TextView(this);
        v.setTextSize(24);
        v.setTextColor(Color.BLUE);
        v.setPadding(16,16,8,8);
        v.setText(name);
        v.setTag(url);
        v.setOnClickListener(this);
        v.setOnLongClickListener(this);
        if (first) {
            list.addView(v, 0);
        } else {
            list.addView(v);
        }

    }

    @Override
    public void onClick(View v) {
        try {
            String url = (String) v.getTag();
            if (url != null) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            Log.e("Webs", e.getMessage(), e);
        }
    }

    @Override
    public boolean onLongClick(final View v) {

        PopupMenu p = new PopupMenu(this, v);
        MenuItem m = p.getMenu().add("Delete");

        m.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                list.removeView(v);
                db.deleteWebSite((String)v.getTag());
                return true;
            }
        });
        p.show();
        return true;
    }
}
