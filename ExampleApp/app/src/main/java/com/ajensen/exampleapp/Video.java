package com.ajensen.exampleapp;

import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class Video extends AppCompatActivity {

    VideoView demoVV;
    ImageButton playButt;
    SeekBar audioSB;
    SeekBar volumeSB;
    MediaPlayer mediaPlayer;
    MediaController mediaController;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Video & Audio");
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        demoVV = (VideoView) findViewById(R.id.demoVV);
        demoVV.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.demovideo);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(demoVV);
        demoVV.setMediaController(mediaController);

        playButt = findViewById(R.id.playButt);
        playButt.setScaleX(2);
        playButt.setScaleY(2);
        mediaPlayer = MediaPlayer.create(this, R.raw.airplane);
        playButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playButt.setImageResource(R.drawable.baseline_play_arrow_black_18);
                } else {
                    mediaPlayer.start();
                    playButt.setImageResource(R.drawable.baseline_pause_black_18);
                }
            }
        });

        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSB = findViewById(R.id.volumeSB);
        volumeSB.setProgress(currentVolume);
        volumeSB.setMax(maxVolume);
        volumeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        audioSB = findViewById(R.id.audioSB);
        audioSB.setMax(mediaPlayer.getDuration());
        audioSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mediaPlayer.seekTo(i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { mediaPlayer.pause(); }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { mediaPlayer.start(); }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                audioSB.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 1000);
    }
}
