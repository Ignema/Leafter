package com.android.leafter.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.leafter.R;
import com.android.leafter.models.Song;

import java.util.ArrayList;
import java.util.Collection;

/*
 * This is demo code to accompany the Mobiletuts+ series:
 * Android SDK: Creating a Music Player
 *
 * Sue Smith - February 2014
 */

public class SongAdapter extends BaseAdapter implements Filterable {

    //song list and layout
    private ArrayList<Song> songs;
    private ArrayList<Song> songsfull;
    private LayoutInflater songInf;




    //constructor
    public SongAdapter(Context c, ArrayList<Song> theSongs){
        songs=theSongs;
        songInf=LayoutInflater.from(c);
        songsfull=new ArrayList<>(songs);
    }

    @Override
    public int getCount() {
        return songs.size();
    }
    private final Filter myfilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchtext=constraint.toString().toLowerCase();
            ArrayList<Song> temp=new ArrayList<>();
            if(searchtext.length() == 0){
                temp.addAll(songsfull);
            }
            else {
                for(Song item: songsfull){
                    if(item.getTitle().toLowerCase().contains(searchtext)
                            || item.getArtist().toLowerCase().contains(searchtext))temp.add(item);
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=temp;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            songs.clear();
            songs.addAll((Collection<? extends Song>) results.values);
            notifyDataSetChanged();

        }
    };

    @Override
    public Filter getFilter() {
        return myfilter;
    }




    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout songLay = (LinearLayout)songInf.inflate
                (R.layout.item_song, parent, false);
        //get title and artist views
        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
        //get song using position
        Song currSong = songs.get(position);
        //get title and artist strings
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        //set position as tag
        songLay.setTag(position);
        return songLay;
    }

}
