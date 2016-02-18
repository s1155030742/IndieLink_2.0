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
    private static MediaPlayer mPlayer = null;

    public static boolean play = false;
    public static String mFilePath = null;
    public static String mFileName = null;

    private Audio(){}

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

    public static void onPlay(boolean start) {
        if (play) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private static void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.release();
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed" + mFileName);
        }
    }

    private static void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }
}
