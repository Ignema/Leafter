package com.android.leafter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;


public class MusicFragment extends Fragment {

    ArrayList<Song> listSongs;
    ListView lvSongs;
    SongAdapter songAdapter;
//    ------------------------------- Permission ----------------------------------------------------

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Toast.makeText(getContext(),"permission is granted",Toast.LENGTH_SHORT).show();
                    getSongs();

                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(getContext(),"permission not granted",Toast.LENGTH_SHORT).show();
                }
            });


//    ------------------------------- END Permission ----------------------------------------------------

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
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
////            send permission request first time
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
//
////            getSongs();
//
//        } else {
////            user already granted permissions first time
//            getSongs();
//        }
//
//        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Song currentSong = listSongs.get(position);
//
//                Intent musicPlayerIntent = new Intent(getContext(),MusicPlayer_Activity.class);
////                Bundle bundle = new Bundle();
////                bundle.putParcelable("currentSong", Parcels.wrap(currentSong));
//                musicPlayerIntent.putExtra("currentSong", currentSong);
//                startActivity(musicPlayerIntent);
//            }
//        });

        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(),"Already granted",Toast.LENGTH_SHORT).show();
            getSongs();
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            Toast.makeText(getContext(),"request sent",Toast.LENGTH_SHORT).show();
        }

        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Song currentSong = listSongs.get(position);

                Intent musicPlayerIntent = new Intent(getContext(), MusicPlayer_Activity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("currentSong", Parcels.wrap(currentSong));
                musicPlayerIntent.putExtra("currentSong", currentSong);
                startActivity(musicPlayerIntent);
            }
        });

        return musicFragmentView;
    }





//----------------------------------------------------- PERMISSION SECTION ----------------------------------------------------------------------------------


    private void getSongs() {
        // get songs from external storage
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(uri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int titleIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int dataIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String songTitle = songCursor.getString(titleIndex);
                String songArtist = songCursor.getString(artistIndex);
                String path = songCursor.getString(dataIndex);
                listSongs.add(new Song(songTitle, songArtist, path));
            } while (songCursor.moveToNext());
//            Toast.makeText(getContext(),"inside if getSongs",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "else getSongs", Toast.LENGTH_SHORT).show();
        }
        songAdapter.notifyDataSetChanged();
    }
}