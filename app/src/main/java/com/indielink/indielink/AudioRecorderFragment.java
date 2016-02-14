package com.indielink.indielink;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class AudioRecorderFragment extends Fragment {

    private static final String LOG_TAG = "IndieLinkAudio";

    private File dir =null;

    private static String FilePath = null;
    private static String mFileName = null;

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    public AudioRecorderFragment() {
        FilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/IndieLinkAudio";
        dir = new File(FilePath);

        if(!dir.exists() || !dir.isDirectory()) {
            // mkdirs if dir do not exist
            dir.mkdirs();
        }
    }

    private void onRecord(boolean start) {
        if (start) {
            File f = new File(FilePath);
            File file[] = f.listFiles();
            Long tsLong = System.currentTimeMillis()/1000;
            mFileName = FilePath + "/indielinkAudio"+ tsLong.toString() + ".3gp";
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_recorder, container, false);

        //Set button onClick Handler
        final ToggleButton record = (ToggleButton) view.findViewById(R.id.RecordButton);
        record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onRecord(isChecked);
                isChecked = !isChecked;
            }
        });

        final ToggleButton play = (ToggleButton) view.findViewById(R.id.PlayButton);
        play.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onPlay(isChecked);
                isChecked = !isChecked;
            }
        });

        return view;
    }
}
