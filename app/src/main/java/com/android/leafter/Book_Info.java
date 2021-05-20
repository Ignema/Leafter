package com.android.leafter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Book_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__info);
        if(this.getIntent().getExtras() != null){
            String s = this.getIntent().getExtras().getString("BookTitle");
            TextView title = this.findViewById(R.id.textView);
            title.setText(s);
        }

    }
}