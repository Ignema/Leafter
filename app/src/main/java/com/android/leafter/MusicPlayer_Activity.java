package com.android.leafter;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MusicPlayer_Activity extends AppCompatActivity implements View.OnClickListener {

//    Views delcaration

    TextView tvTime, tvDuration;
    Button btnPlay;
    SeekBar seekBarTime, seekBarVolume;

    MediaPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_);

        tvTime = findViewById(R.id.tvTime);
        tvDuration = findViewById(R.id.tvDuration);
        btnPlay = findViewById(R.id.btnPlay);
        seekBarTime = findViewById(R.id.seekBar_time);
        seekBarVolume = findViewById(R.id.seekBarVolume);

        musicPlayer = MediaPlayer.create(this, R.raw.mami_sayada);
        musicPlayer.setLooping(true);
        musicPlayer.seekTo(0);
        musicPlayer.setVolume(0.5f, 0.5f);

        btnPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnPlay){
            if(musicPlayer.isPlaying()){
                musicPlayer.pause();
                btnPlay.setBackgroundResource(R.drawable.ic_play);
            }else{
                musicPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.ic_pause);
            }
        }
    }
}