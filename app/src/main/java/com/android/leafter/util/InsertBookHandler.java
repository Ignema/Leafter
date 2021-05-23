package com.android.leafter.util;

import android.os.Handler;
import android.os.Message;

import com.android.leafter.activities.Book_Info_Activity;
import com.android.leafter.activities.Book_List_Activity;

import java.lang.ref.WeakReference;

public class InsertBookHandler extends Handler {

    private static final int SHOW_PROGRESS = 1002;
    private static final int HIDE_PROGRESS = 1003;
    private final WeakReference<Book_Info_Activity> weakReference;

    public InsertBookHandler(Book_Info_Activity blInstance) {
        weakReference = new WeakReference<>(blInstance);
    }

    void showProgress(int progress) {
        Message msg=new Message();
        msg.arg1 = InsertBookHandler.SHOW_PROGRESS;
        msg.arg2 = progress;
        sendMessage(msg);
    }
    void hideProgress() {
        Message msg=new Message();
        msg.arg1 = InsertBookHandler.HIDE_PROGRESS;
        sendMessage(msg);
    }

    @Override
    public void handleMessage(Message msg) {
        Book_Info_Activity blInstance = weakReference.get();
        if (blInstance != null) {
            switch (msg.arg1) {

                case SHOW_PROGRESS:
                    blInstance.showProgress(msg.arg2);
                    break;
                case HIDE_PROGRESS:
                    blInstance.hideProgress();
                    break;
            }
        }
    }
}

