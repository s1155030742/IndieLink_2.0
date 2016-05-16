package com.indielink.indielink;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indielink.indielink.Audio.Audio;
import com.indielink.indielink.CustomAdapter.MyRecommendMusicRecyclerViewAdapter;
import com.indielink.indielink.RecommendSound.RecommendTrackItem;

import java.util.ArrayList;
import java.util.List;

public class RecommendMusicFragment extends Fragment {

    private static final String LOG_TAG = "IndieLinkAudio";
    private Audio audio = null;
    private List<Integer> UserSoundIdList = new ArrayList<Integer>();
    private MyRecommendMusicRecyclerViewAdapter mAdapter;
    private SoundTrackNames mSoundTrackNames = null;

    public boolean IsPlaying = false;
    public static MediaPlayer player = null;

    public RecommendMusicFragment() {
        mSoundTrackNames = new SoundTrackNames();
        audio = Audio.getInstance();
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecommendMusicFragment newInstance(int columnCount) {
        RecommendMusicFragment fragment = new RecommendMusicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            UserSoundIdList = getArguments().getIntegerArrayList("user_sound_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommendmusic_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recommend_list);

        // Set the adapter
        if (recyclerView != null) {
            Context context = view.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mAdapter = new MyRecommendMusicRecyclerViewAdapter(getMusicList(UserSoundIdList), this);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    //after volley u get a list of user_sound_id
    private ArrayList<RecommendTrackItem> getMusicList(List<Integer> IdList)
    {
        ArrayList<RecommendTrackItem> MusicList = new ArrayList<RecommendTrackItem>();
        for(Integer Id : IdList){
            int id = -1;
            if(mSoundTrackNames.GetSoundName(Id)>=0)
            {
                id = mSoundTrackNames.GetSoundName(Id);
                String name = getResources().getResourceName(id).split("/")[1];
                RecommendTrackItem item = new RecommendTrackItem(id,name);
                MusicList.add(item);
            }
        }
        return MusicList;
    }
}
