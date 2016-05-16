package com.indielink.indielink.Audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
import java.io.IOException;

/**
 * Created by E8400 on 2016/02/18.
 */

public class Audio {

    private static Audio instance = null;
    private static final String LOG_TAG = "IndieLinkAudio";
    public static MediaPlayer mPlayer = null;

    public static boolean play;
    public static String mFilePath = null;
    public static String mFileName = "";
    public static int id;

    private Audio(){
        play = false;
    }

    static {
        System.loadLibrary("essentia");
       System.loadLibrary("MusicExtractor");
        System.loadLibrary("AudioAnalysis");
    }

    public native int AudioAnalysis(String audioFilename,String outputFilename,String profileFilename);

    synchronized static public Audio getInstance() {
        if (instance == null) {
            synchronized(Audio.class){
                if(instance == null) {
                    instance = new Audio();
                }
            }
        }
        return instance;
    }

    static public MediaPlayer getPlayer() {
        if (mPlayer == null) {
                if(mPlayer == null) {
                    mPlayer = new MediaPlayer();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer player) {
                            play = false;
                            stopPlaying();
                        }
                    });
                }
        }
        return mPlayer;
    }

    public static void startPlaying() {
        try {
            if(mPlayer != null && mPlayer.isPlaying())
            {
                stopPlaying();
            }
            mPlayer = getPlayer();
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed" + mFileName);
        }
    }

    public static void stopPlaying() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    public static void startPlayingLocal(Context context) {
        try {
            if(mPlayer != null && mPlayer.isPlaying())
            {
                stopPlaying();
            }
            mPlayer = getPlayer();
            AssetFileDescriptor assetFileDescriptor = null;
            if(id>-1){
                assetFileDescriptor = context.getResources().openRawResourceFd(id);
            }
            if(assetFileDescriptor == null) return;
            mPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            assetFileDescriptor.close();
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed" + mFileName);
        }
    }
}
