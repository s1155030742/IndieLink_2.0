package com.indielink.indielink.CustomAdapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.indielink.indielink.Audio.Audio;
import com.indielink.indielink.R;
import com.indielink.indielink.RecommendMusicFragment;
import com.indielink.indielink.RecommendSound.RecommendTrackItem;
import java.util.List;

public class MyRecommendMusicRecyclerViewAdapter extends RecyclerView.Adapter<MyRecommendMusicRecyclerViewAdapter.ViewHolder> {

    public List<RecommendTrackItem> mRecommendTrackList;
    public Activity mActivity;
    public RecommendMusicFragment mFragment;

    public MyRecommendMusicRecyclerViewAdapter(List<RecommendTrackItem> recommendTrackItems, RecommendMusicFragment recommendMusicFragment) {
        mRecommendTrackList = recommendTrackItems;
        mActivity = recommendMusicFragment.getActivity();
        mFragment = recommendMusicFragment;
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
        holder.mNameView.setText(item.name);

        holder.mNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Audio audio = Audio.getInstance();
                TextView t = (TextView) v;
                String fileName = t.getText().toString();

                int id;
                for (RecommendTrackItem item : mRecommendTrackList) {
                    if (item.name == fileName) {
                        id = item.id;
                        if(id==audio.id) {
                            audio.stopPlaying();
                            audio.id = -1;
                        } else
                        {
                            audio.id = id;
                            audio.startPlayingLocal(mActivity);
                        }
                        break;
                    }
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
        public final TextView mNameView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.recommend_name);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}