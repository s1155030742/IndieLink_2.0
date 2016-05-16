package com.indielink.indielink.CustomAdapter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.indielink.indielink.Audio.Audio;
import com.indielink.indielink.R;
import com.indielink.indielink.RecommendSound.RecommendTrackItem;

import java.io.File;
import java.util.List;

public class MyRecommendMusicRecyclerViewAdapter extends RecyclerView.Adapter<MyRecommendMusicRecyclerViewAdapter.ViewHolder> {

    public List<RecommendTrackItem> mRecommendTrackList;
    public Audio mAudio;
    public Activity mActivity;

    public MyRecommendMusicRecyclerViewAdapter(List<RecommendTrackItem> recommendTrackItems,Audio audio, Activity activity) {
        mRecommendTrackList = recommendTrackItems;
        mAudio = audio;
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recommendmusic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        RecommendTrackItem item = mRecommendTrackList.get(position);
        holder.mImgPlay.setImageResource(android.R.drawable.ic_media_play);
        holder.mImgPause.setImageResource(android.R.drawable.ic_media_pause);
        holder.mNameView.setText(item.name);

        holder.mImgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.mImgPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.mNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Audio audio = Audio.getInstance();
                TextView t = (TextView) v;
                String fileName = t.getText().toString();

                MediaPlayer player = null;
                int id;
                for (RecommendTrackItem item : mRecommendTrackList) {
                    if(item.name == fileName){
                        id = item.id;
                        player = MediaPlayer.create(mActivity, id);
                        break;
                    }
                }
                if(player != null) {
                    //TODO
                    player.start();
                    player.pause();
                    player.seekTo(player.getCurrentPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecommendTrackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImgPlay;
        public final ImageView mImgPause;
        public final TextView mNameView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImgPlay = (ImageView) view.findViewById(R.id.recommend_play);
            mImgPause = (ImageView) view.findViewById(R.id.recommend_pause);
            mNameView = (TextView) view.findViewById(R.id.recommend_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}