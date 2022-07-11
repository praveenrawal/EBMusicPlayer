package com.example.emotion_detection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    Context context;
    ArrayList<MusicItem> arrMusicList;


    public MusicListAdapter(Context context, ArrayList<MusicItem> arrMusicList){
        this.arrMusicList = arrMusicList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_music_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.music_name.setText(arrMusicList.get(position).music_name);

        holder.itemView.setOnClickListener(view -> {

            Intent intent = new Intent(context, MusicPlayerActivity.class);
            intent.putExtra("musiclist", arrMusicList);
            intent.putExtra("position", position);
            intent.putExtra("music_name", arrMusicList.get(position).music_name);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return arrMusicList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView music_name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            music_name = itemView.findViewById(R.id.custom_music_tv_name);

        }
    }
}
