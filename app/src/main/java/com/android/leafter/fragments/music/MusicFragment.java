package com.android.leafter.fragments.music;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;


import com.android.leafter.Service.MusicService;
import com.android.leafter.R;
import com.android.leafter.contoller.MusicController;
import com.android.leafter.models.Song;
import com.android.leafter.adapters.SongAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MusicFragment extends Fragment implements MediaController.MediaPlayerControl {

    View musicFragmentView;
    //song list variables
    private ArrayList<Song> songList;
    private ListView songView;

    //service
    private MusicService musicSrv;
    private Intent playIntent;

    //binding
    private boolean musicBound = false;

    //media plauer intent
    Intent onPreparedIntent = new Intent("MEDIA_PLAYER_PREPARED");
    IntentFilter onPreparedIntentFilter = new IntentFilter("MEDIA_PLAYER_PREPARED");

    //controller
    private MusicController controller;

    //external permission code
//    private static final int STORAGE_PERMISSION_CODE = 101;

    //activity and playback pause flags
    private boolean paused = false, playbackPaused = false;

    public void checkPermission(String permission) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                getContext(), permission) ==
                PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getContext(), "Already granted", Toast.LENGTH_SHORT).show();
            getSongList();
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            Toast.makeText(getContext(), "request sent", Toast.LENGTH_SHORT).show();
        }
    }

//    ------------------------------- Permission ----------------------------------------------------

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Toast.makeText(getContext(), "permission is granted", Toast.LENGTH_SHORT).show();
                    getSongList();

                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(getContext(), "permission not granted", Toast.LENGTH_SHORT).show();
                }
            });


    //    ------------------------------- END Permission ----------------------------------------------------
    // Broadcast receiver to determine when music player has been prepared
    private final BroadcastReceiver onPrepareReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            // When music player has been prepared, show controller
            Log.v("debug : ", "onPrepareReceiver called");
            controller.show(0);
        }
    };

    public MusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("debug : ", "Oncreate called");
        // Inflate the layout for this fragment
        musicFragmentView = inflater.inflate(R.layout.fragment_music, container, false);

        //retrieve list view
        songView = (ListView) musicFragmentView.findViewById(R.id.song_list);
        //instantiate list
        songList = new ArrayList<Song>();
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

        //sort alphabetically by title
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        //create and set adapter
        SongAdapter songAdt = new SongAdapter(getContext(), songList);
        songView.setAdapter(songAdt);
        //setup controller
        setController();

        songView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.v("Item CLicked", "position of song : "+position);
                songPicked(position);

            }
        });

        return musicFragmentView;
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            if(musicSrv!=null){
                Log.v("debug : ", "onServiceConnected --> musicSrv not null");
            }else{
                Log.v("debug : ", "onServiceConnected --> musicSrv is null");
            }
            //pass list
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v("debug : ", "onServiceDisconnected -->called");
            musicBound = false;

        }
    };
    //start and bind the service when the activity starts
    @Override
    public void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(getContext(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
            Log.v("debug : ", "playIntent == null onStart called");
        }else{
            Log.v("debug : ", "playIntent != null onStart called");
        }
    }


    //method to retrieve song info from device
    public void getSongList() {

        //query external audio
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }

    //user song select
    public void songPicked(int position) {
        musicSrv.setSong(position);
        musicSrv.playSong();
        Log.v("debug : ", "songPicked  called");

        if (playbackPaused) {
//            setController();
            playbackPaused = false;
        }
    }
    @Override
    public void start() {

        musicSrv.go();
        controller.show(0);
        Log.v("debug : ", "start  called and controller show");
    }

    @Override
    public void pause() {
        playbackPaused = true;
        musicSrv.pausePlayer();
        controller.show(0);
        Log.v("debug : ", "pause  called and controller show");
    }

    @Override
    public int getDuration() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if (musicSrv != null && musicBound){
//            Log.v("debug : ", "isPlaying  called inside if");
            return musicSrv.isPng();
        }
        Log.v("debug : ", "isPlaying  called inside else");
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    //set the controller up
    private void setController() {
        if (controller == null) controller = new MusicController(getContext());
        //set previous and next button listeners
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        //set and show
        controller.setMediaPlayer(this);
        controller.setAnchorView(musicFragmentView.findViewById(R.id.song_list));
        controller.setEnabled(true);
        Log.v("debug : ", "setController  called");
    }

    private void playNext() {
        musicSrv.playNext();
        if (playbackPaused) {
//            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    private void playPrev() {
        musicSrv.playPrev();
        if (playbackPaused) {
//            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    @Override
    public void onPause() {
        Log.v("debug : ", "onPause  called");
        super.onPause();
    }
    @Override
    public void onResume() {
        Log.v("debug : ", "onResume  called");
        super.onResume();

//        setController();

        // Set up receiver for media player onPrepared broadcast
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onPrepareReceiver, onPreparedIntentFilter);

//        controller.show(0);

//        if (paused) {
////            setController();
//            paused = false;
//        }

    }

    @Override
    public void onStop() {

//        controller.hide();
//        controller = null;
        Log.v("debug : ", "onStop  called and controller is hided and set to null");
//        System.gc();
        super.onStop();
    }
}