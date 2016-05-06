package com.indielink.indielink.Audio;

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

    private Audio(){
        play = false;
    }

    static {
        System.loadLibrary("essentia");
        System.loadLibrary("MusicExtractor");
        System.loadLibrary("audio_analysis");
    }

    public native int audio_analysis(String audioFilename,String outputFilename,String profileFilename);

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
}
