package com.android.leafter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MusicFragment extends Fragment {

    ArrayList<Song> listSongs;
    ListView lvSongs;
    SongAdapter songAdapter;

    private static final int REQUEST_PERMISSION_CODE = 99;


    public MusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View musicFragmentView = inflater.inflate(R.layout.fragment_music, container, false);

        listSongs = new ArrayList<>();
        songAdapter = new SongAdapter(getContext(), listSongs);

        lvSongs = (ListView) musicFragmentView.findViewById(R.id.lvSongs);
        lvSongs.setAdapter(songAdapter);
//----------------------------------------------------- PERMISSION SECTION ----------------------------------------------------------------------------------
//        check if the App have permission to read external storage
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            send permission request first time
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);

        } else {
//            user already granted permissions first time
            getSongs();


        }
        return musicFragmentView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                getSongs();
            }
        }
    }

//----------------------------------------------------- PERMISSION SECTION ----------------------------------------------------------------------------------


    private void getSongs() {
//        get songs from external storage
    }

}