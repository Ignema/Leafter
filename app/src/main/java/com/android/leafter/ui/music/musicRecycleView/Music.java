package com.android.leafter.ui.music.musicRecycleView;

import android.media.Image;

public class Music {
    private String name;
    private String Singer;
    private Image image;

    public Music(String name, String singer, Image image) {
        this.name = name;
        Singer = singer;
        this.image = image;
    }

    public Music(String name, String singer) {
        this.name = name;
        Singer = singer;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return Singer;
    }

    public void setSinger(String singer) {
        Singer = singer;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
