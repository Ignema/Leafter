package com.android.leafter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.leafter.R;
import com.android.leafter.models.Song;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {

    TextView tvSongTitle;
    TextView tvSongArtist;

    public SongAdapter(@NonNull Context context, @NonNull List<Song> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, null);

        tvSongArtist = (TextView) convertView.findViewById(R.id.tvSongArtist);
        tvSongTitle = (TextView) convertView.findViewById(R.id.tvSongTitle);
        Song song = getItem(position);
        tvSongTitle.setText(song.getTitle());
        tvSongArtist.setText(song.getArtist());


        return convertView;
    }
}
