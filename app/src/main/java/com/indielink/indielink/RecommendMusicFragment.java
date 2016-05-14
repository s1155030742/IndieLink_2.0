package com.indielink.indielink;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indielink.indielink.Audio.Audio;
import com.indielink.indielink.CustomAdapter.MyRecommendMusicRecyclerViewAdapter;
import com.indielink.indielink.Profile.SoundTrackContent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecommendMusicFragment extends Fragment {

    private static final String LOG_TAG = "IndieLinkAudio";
    private File dir =null;
    private static String FilePath = null;
    private static String mFileName = null;
    private Audio audio = null;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private String UserName;
    private List<Integer> UserSoundIdList = new ArrayList<Integer>();
    private MyRecommendMusicRecyclerViewAdapter mAdapter;

    public RecommendMusicFragment() {
        audio = Audio.getInstance();
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecommendMusicFragment newInstance(int columnCount) {
        RecommendMusicFragment fragment = new RecommendMusicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            UserName = getArguments().getString("UserName");
            UserSoundIdList = getArguments().getIntegerArrayList("user_sound_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommendmusic_list, container, false);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            RootPage rootPage = (RootPage) getActivity();
            mAdapter = new MyRecommendMusicRecyclerViewAdapter(getListOfFile(),audio,view.getContext(),rootPage.getUser_id());
            recyclerView.setAdapter(mAdapter);

            //suppose get list of id from server from /band/recommend
            recyclerView.setAdapter(new MyRecommendMusicRecyclerViewAdapter(getMusicList(UserSoundIdList),audio,view.getContext(),rootPage.getUser_id()));
        }
        return view;
    }
    private List<SoundTrackContent.SoundTrackItem> getListOfFile()
    {
        List<SoundTrackContent.SoundTrackItem> listOfSounds = new ArrayList<SoundTrackContent.SoundTrackItem>();
        Log.d("Files", "Path: " + FilePath);
        File f = new File(FilePath);
        File file[] = f.listFiles();
        Log.d("Files", "Size: "+ file.length);
        for (int i=0; i < file.length; i++)
        {
            Log.d("Files", "FileName:" + file[i].getName());
            listOfSounds.add(new SoundTrackContent.SoundTrackItem(String.valueOf(i+1),  file[i].getName()));
        }
        return listOfSounds;
    }
    //after volley u get a list of user_sound_id
    private List<SoundTrackContent.SoundTrackItem> getMusicList(List<Integer> IdList)
    {
        List<SoundTrackContent.SoundTrackItem> MusicList = new ArrayList<>();
        for(Integer Id : IdList){
          //  MusicList.add(new mSoundTrackNames.GetSoundName(Id));
        }
        return MusicList;
    }
}
