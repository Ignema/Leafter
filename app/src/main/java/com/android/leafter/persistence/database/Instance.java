package com.android.leafter.persistence.database;

import android.app.Application;
import android.content.Context;

public class Instance extends Application {

    private DatabaseAPI db;

    @Override
    public void onCreate() {
        super.onCreate();

        db = new DatabaseAPI(this);
    }

    public static DatabaseAPI getDB(Context context) {
        return ((Instance)context.getApplicationContext()).db;
    }


    @Override
    public void onTerminate() {
        if (db!=null) db.close();

        super.onTerminate();
    }
}
