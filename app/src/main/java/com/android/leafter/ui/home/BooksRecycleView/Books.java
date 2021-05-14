package com.android.leafter.ui.home.BooksRecycleView;

import android.media.Image;

public class Books {

    private String name;
    private  String WriterName;
    private Image image;

    public Books(String name, String writerName, Image image) {
        this.name = name;
        WriterName = writerName;
        this.image = image;
    }
    public Books(String name, String writerName) {
        this.name = name;
        this.WriterName = writerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWriterName() {
        return WriterName;
    }

    public void setWriterName(String writerName) {
        WriterName = writerName;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }


}
