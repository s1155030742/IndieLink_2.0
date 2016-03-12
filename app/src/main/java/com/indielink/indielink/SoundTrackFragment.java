package com.indielink.indielink;

import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.indielink.indielink.Audio.Audio;
import com.indielink.indielink.CustomAdapter.MySoundTrackRecyclerViewAdapter;
import com.indielink.indielink.CustomListener.OnSwipeTouchListener;
import com.indielink.indielink.Profile.SoundTrackContent.SoundTrackItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SoundTrackFragment extends Fragment {

    private static final String LOG_TAG = "IndieLinkAudio";

    private File dir =null;

    private static String FilePath = null;
    private static String mFileName = null;

    private MediaRecorder mRecorder = null;
    private Audio audio = null;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private String UserName;

    private MySoundTrackRecyclerViewAdapter mAdapter;
    private OnListFragmentInteractionListener mListener;

    public SoundTrackFragment() {
        FilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/IndieLinkAudio";
        dir = new File(FilePath);
        audio = Audio.getInstance();
        if(!dir.exists() || !dir.isDirectory()) {
            // mkdirs if dir do not exist
            dir.mkdirs();
        }
    }

    @SuppressWarnings("unused")
    public static SoundTrackFragment newInstance(int columnCount) {
        SoundTrackFragment fragment = new SoundTrackFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soundtrack_list, container, false);

        //Set button onClick Handler
        final ToggleButton record = (ToggleButton) view.findViewById(R.id.RecordButton);
        record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            onRecord(isChecked);
                            isChecked = !isChecked;
                        }
                    });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        OnSwipeTouchListener mOnSwipe = new OnSwipeTouchListener(view.getContext()) {
            public void onSwipeRight() {
                //mAdapter.mValues
                //mAdapter.notifyDataSetChanged();
            }
        };

        // Set the adapter
        if (recyclerView instanceof RecyclerView) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));//这里用线性显示 类似于listview
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));//这里用线性宫格显示 类似于grid view
            }
            mAdapter = new MySoundTrackRecyclerViewAdapter(getListOfFile(), mListener, FilePath, audio);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*if (activity instanceof OnListFragmentInteractionListener) {
            //mListener = (OnListFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(SoundTrackItem item);
    }

    private List<SoundTrackItem> getListOfFile()
    {
        List<SoundTrackItem> listOfSounds = new ArrayList<SoundTrackItem>();
        Log.d("Files", "Path: " + FilePath);
        File f = new File(FilePath);
        File file[] = f.listFiles();
        Log.d("Files", "Size: "+ file.length);
        for (int i=0; i < file.length; i++)
        {
            Log.d("Files", "FileName:" + file[i].getName());
            listOfSounds.add(new SoundTrackItem(String.valueOf(i+1),  file[i].getName()));
        }
        return listOfSounds;
    }

    private void onRecord(boolean start) {
        if (start) {
            File f = new File(FilePath);
            File file[] = f.listFiles();
            Long tsLong = System.currentTimeMillis()/1000;
            mFileName = FilePath + "/" + UserName + tsLong.toString() + ".3gp";
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        String file = mFileName.split(FilePath+"/")[1];
        mAdapter.mValues.add(new SoundTrackItem(String.valueOf(mAdapter.getItemCount() + 1),file ));
        mAdapter.notifyDataSetChanged();
    }
}
