package com.example.emotion_detection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MusicListActivity extends AppCompatActivity {

    RecyclerView list_recyclerview;

    ArrayList<MusicItem> arrMusicList = new ArrayList<>();

    Intent intent;

    ProgressBar progressBar;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        progressBar = findViewById(R.id.list_pbar);
        toolbar = findViewById(R.id.list_toolbar);
        list_recyclerview = findViewById(R.id.list_recyclerview);

        setSupportActionBar(toolbar);

        FirebaseApp.initializeApp(this);

//        String [] classes = {"angry", "disgust", "fear", "happy", "neutral", "sad", "surprise"};

        intent = getIntent();
        Log.d("meramood", intent.getStringExtra("emotion"));
        if (intent!=null){
            if (intent.getStringExtra("emotion")!=null){
                if (intent.getStringExtra("emotion").equals("angry") || intent.getStringExtra("emotion").equals("disgust")){
                    fetchSongsList("angrysongs/");
                }else if (intent.getStringExtra("emotion").equals("sad") || intent.getStringExtra("emotion").equals("fear")){
                    fetchSongsList("sadsongs/");
                }else if (intent.getStringExtra("emotion").equals("happy") || intent.getStringExtra("emotion").equals("surprise")){
                    fetchSongsList("happysongs/");
                }else if (intent.getStringExtra("emotion").equals("neutral")){
                    fetchSongsList("neutralsongs/");
                }else{
                    fetchSongsList("neutralsongs/");
                }

                toolbar.setTitle(intent.getStringExtra("emotion")+" songs");
                toolbar.setNavigationOnClickListener(view -> finish());
            }
        }
    }

    private void fetchSongsList(String emotion) {


        list_recyclerview.setLayoutManager(new LinearLayoutManager(MusicListActivity.this));
        MusicListAdapter adapter = new MusicListAdapter(MusicListActivity.this, arrMusicList);


        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("songs/"+emotion);
        storageReference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference reference : listResult.getItems()){

                reference.getDownloadUrl().addOnSuccessListener(uri -> {
                    MusicItem musicItem = new MusicItem();
                    musicItem.music_name = reference.getName();
                    Log.d("songs", uri.toString()+"  ,"+reference.getName());
                    musicItem.music_url = uri.toString();
                    arrMusicList.add(musicItem);

                }).addOnSuccessListener(uri -> {
                    progressBar.setVisibility(View.GONE);
                    list_recyclerview.setAdapter(adapter);
                });
            }

        });


    }
}