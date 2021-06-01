///*
//package com.android.leafter.activities;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Context;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//import com.android.leafter.R;
//import com.android.leafter.models.Song;
//
//import java.io.IOException;
//
//public class Music_Player_Activity extends AppCompatActivity implements View.OnClickListener {
//
////    Views delcaration
//
//    TextView tvTime, tvDuration, songTitle, songArtist;
//    Button btnPlay;
//    SeekBar seekBarTime, seekBarVolume;
//
//    MediaPlayer musicPlayer;
//    AudioManager audiomanager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_music_player_);
//
//        tvTime = findViewById(R.id.tvTime);
//        tvDuration = findViewById(R.id.tvDuration);
//        songTitle = findViewById(R.id.songTitle);
//        songArtist = findViewById(R.id.songArtist);
//
//        btnPlay = findViewById(R.id.btnPlay);
//        seekBarTime = findViewById(R.id.seekBar_time);
//        seekBarVolume = findViewById(R.id.seekBarVolume);
//
//
////        Song currentSong = Parcels.unwrap(getIntent().getParcelableExtra("currentSong"));
//        Song currentSong = (Song) getIntent().getSerializableExtra("currentSong");
//
//        songTitle.setText(currentSong.getTitle());
//        songArtist.setText(currentSong.getArtist());
//
//        musicPlayer = new MediaPlayer();
//        Toast.makeText(this,"before try file://"+currentSong.getPath(),Toast.LENGTH_SHORT).show();
//        try {
//            Toast.makeText(this,"after try file://"+currentSong.getPath(),Toast.LENGTH_SHORT).show();
//            musicPlayer.setDataSource("file://"+currentSong.getPath());
//            Toast.makeText(this,"after setDataSource file://"+currentSong.getPath(),Toast.LENGTH_SHORT).show();
//            musicPlayer.prepare();
//        } catch (IOException e) {
//            System.out.println("--------------> file://"+currentSong.getPath());
//            e.printStackTrace();
//        }
////        musicPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mami_sayada);
//        musicPlayer.setLooping(true);
//        musicPlayer.seekTo(0);
//        musicPlayer.setVolume(0.5f, 0.5f);
//
//        btnPlay.setOnClickListener(this);
//        initVolumeControls();
//        initMusicControls();
//    }
//
//    private void initMusicControls() {
//        seekBarTime.setMax(musicPlayer.getDuration());
//        tvDuration.setText(millisecondsToString(musicPlayer.getDuration()));
//        seekBarTime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
//                if(isFromUser){
//                    musicPlayer.seekTo(progress);
//                    seekBarTime.setProgress(progress);
//                }
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (musicPlayer != null){
//                    if(musicPlayer.isPlaying()){
//                        try {
//                            final double current = musicPlayer.getCurrentPosition();
//                            String elapsedTime = millisecondsToString((int) current);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    tvTime.setText(elapsedTime);
//                                    seekBarTime.setProgress((int) current);
//                                }
//                            });
//                            Thread.sleep(1000);
//
//                        }catch (InterruptedException e){
//
//                        }
//                    }
//                }
//            }
//        }).start();
//    }
//
//    private void initVolumeControls() {
//        audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        seekBarVolume.setMax(audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
//        seekBarVolume.setProgress(audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC));
//        seekBarVolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFronUser) {
//                audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//
//    }
//
//    private String millisecondsToString(int duration) {
//        String elapsedTime = "";
//        int minutes = duration / 1000 / 60;
//        int seconds = duration / 1000 % 60;
//        elapsedTime += minutes + ":";
//        if (seconds < 10) elapsedTime += "0";
//        elapsedTime += seconds;
//        return elapsedTime;
//    }
//
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//            int index = seekBarVolume.getProgress();
//            seekBarVolume.setProgress(index + 1);
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            int index = seekBarVolume.getProgress();
//            seekBarVolume.setProgress(index - 1);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (view.getId() == R.id.btnPlay) {
//            if (musicPlayer.isPlaying()) {
//                musicPlayer.pause();
//                btnPlay.setBackgroundResource(R.drawable.ic_play);
//            } else {
//                musicPlayer.start();
//                btnPlay.setBackgroundResource(R.drawable.ic_pause);
//            }
//        }
//    }
//}*/
