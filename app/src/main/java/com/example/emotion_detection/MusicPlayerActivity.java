package com.example.emotion_detection;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;


public class MusicPlayerActivity extends AppCompatActivity {

    static MediaPlayer mediaPlayer;

    int position;

    String songname;

    ArrayList<MusicItem> arrMusicList;

    Toolbar toolbar;

    TextView tv_name, tv_starttime, tv_endtime;

    ImageView ppimg, prevsongimg, nextsongimg, songanimimg;

    SeekBar seekBar;

    String musicurl;

    int x = 0;

    Uri musicuri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        toolbar = findViewById(R.id.player_toolbar);
        seekBar = findViewById(R.id.player_seekbar);
        tv_name = findViewById(R.id.player_tv_name);
        tv_starttime = findViewById(R.id.player_tv_start);
        tv_endtime = findViewById(R.id.player_tv_end);

        ppimg = findViewById(R.id.player_pauseplay);
        nextsongimg = findViewById(R.id.player_nextsong);
        prevsongimg = findViewById(R.id.player_prevsong);
        songanimimg = findViewById(R.id.player_songanimimg);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());

        Intent intent = getIntent();

        arrMusicList = (ArrayList<MusicItem>) intent.getSerializableExtra("musiclist");

        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        songname = intent.getStringExtra("music_name");
        position = intent.getIntExtra("position", 0);

        musicurl = arrMusicList.get(position).music_url;
        musicuri = Uri.parse(arrMusicList.get(position).music_url);
        songname = arrMusicList.get(position).music_name;
        tv_name.setText(songname);

        mediaPlayer = MediaPlayer.create(this, musicuri);
        mediaPlayer.start();
        seekBar.setProgress(0);
        seekBar.setMax(mediaPlayer.getDuration());


        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    tv_starttime.setText(createTime((mediaPlayer.getCurrentPosition())));

                    if (mediaPlayer.isPlaying()){
                        ppimg.setImageResource(R.drawable.playicon);
                        songanimimg.setRotation(x++);
                    }else {
                        ppimg.setImageResource(R.drawable.pauseicon);
                        songanimimg.setRotation(x);
                    }
                }

                new Handler().postDelayed(this, 100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer!=null && b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tv_endtime.setText(createTime(mediaPlayer.getDuration()));

        ppimg.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()){
                ppimg.setImageResource(R.drawable.pauseicon);
                mediaPlayer.pause();
            }else {
                ppimg.setImageResource(R.drawable.playicon);
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(mediaPlayer -> nextSongPlay());

        nextsongimg.setOnClickListener(view -> nextSongPlay());

        prevsongimg.setOnClickListener(view -> prevSongPlay());

    }

    private void prevSongPlay() {
        x = 0;
        mediaPlayer.stop();
        mediaPlayer.release();
        position = Math.max((position - 1), 0);
        Uri uri1 = Uri.parse(arrMusicList.get(position).music_url);
        mediaPlayer = MediaPlayer.create(this, uri1);
        songname = arrMusicList.get(position).music_name;
        tv_name.setText(songname);
        tv_starttime.setText(createTime(mediaPlayer.getCurrentPosition()));
        tv_endtime.setText(createTime(mediaPlayer.getDuration()));
        mediaPlayer.start();
        ppimg.setImageResource(R.drawable.pauseicon);
        startAnimation(songanimimg);
    }

    private void nextSongPlay() {
        x = 0;
        mediaPlayer.stop();
        mediaPlayer.release();
        position = (position+1)<(arrMusicList.size())?(position+1):0;
        Uri uri1 = Uri.parse(arrMusicList.get(position).music_url);
        mediaPlayer = MediaPlayer.create(this, uri1);
        songname = arrMusicList.get(position).music_name;
        tv_name.setText(songname);
        tv_starttime.setText(createTime(mediaPlayer.getCurrentPosition()));
        tv_endtime.setText(createTime(mediaPlayer.getDuration()));
        mediaPlayer.start();
        ppimg.setImageResource(R.drawable.pauseicon);
        startAnimation(songanimimg);
    }

    public String createTime(int duration){
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time+=min+":";

        if (sec<10){
            time+="0";
        }
        time+=sec;

        return time;
    }

    public void startAnimation(View view){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(songanimimg, "rotation", 0f, 360f);
        objectAnimator.setDuration(1000);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(objectAnimator);
        set.start();
    }

}