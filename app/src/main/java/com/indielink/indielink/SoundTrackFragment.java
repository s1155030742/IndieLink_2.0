package com.indielink.indielink;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
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

import com.facebook.internal.Utility;
import com.indielink.indielink.Audio.Audio;
import com.indielink.indielink.CustomAdapter.MySoundTrackRecyclerViewAdapter;
import com.indielink.indielink.Profile.SoundTrackContent.SoundTrackItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    private Long mTsLong;

    // 音頻獲取源
    private int audioSource = MediaRecorder.AudioSource.MIC;
    // 設置音頻采样率，44100是目前的標准，但是某些設備仍然支持22050，16000，11025
    private static int sampleRateInHz = 44100;
    // 設置音頻的錄制的聲道CHANNEL_IN_STEREO为雙聲道，CHANNEL_CONFIGURATION_MONO为單聲道
    private static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    // 音頻數據格式:PCM 16位每個样本。保證設備支持。PCM 8位每個样本。不一定能得到設備支持。
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    // 緩沖區字節大小
    private int bufferSizeInBytes = 0;
    private AudioRecord audioRecord;

    private boolean mIsRecord = false;

    public SoundTrackFragment() {
        FilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/IndieLinkAudio";
        dir = new File(FilePath);
        audio = Audio.getInstance();
        if(!dir.exists() || !dir.isDirectory()) {
            // mkdirs if dir do not exist
            dir.mkdirs();
        }

        CopyProfile(FilePath);
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

        // 獲得緩沖區字節大小
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, audioFormat);
        // 創建AudioRecord對象
        audioRecord = new AudioRecord(audioSource, sampleRateInHz,
                channelConfig, audioFormat, bufferSizeInBytes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soundtrack_list, container, false);

        //Set button onClick Handler
        final ToggleButton record = (ToggleButton) view.findViewById(R.id.RecordButton);
        record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onRecord(isChecked);
                mIsRecord = isChecked;
                isChecked = !isChecked;
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // Set the adapter
        if (recyclerView instanceof RecyclerView) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));//这里用线性显示 类似于listview
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));//这里用线性宫格显示 类似于grid view
            }
            RootPage rootPage = (RootPage) getActivity();

            mAdapter = new MySoundTrackRecyclerViewAdapter(getListOfFile(), FilePath,  audio,(RootPage) getActivity(),rootPage.getUser_id());
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        if(mIsRecord) stopRecording();
        super.onDestroy();
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
            mTsLong = System.currentTimeMillis()/1000;
            mFileName = FilePath + "/" + UserName + mTsLong.toString();
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName + ".3gp");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
        // 開启音頻文件寫入線程
        new Thread(new AudioRecordThread()).start();
    }

    private void stopRecording() {
        String file = mFileName.split(FilePath+"/")[1];

            audioRecord.stop();
            audioRecord.release();//釋放資源
            audioRecord = null;

        mAdapter.mValues.add(new SoundTrackItem(String.valueOf(mAdapter.getItemCount() + 1),file ));
        mAdapter.notifyDataSetChanged();
    }

    class AudioRecordThread implements Runnable {
        @Override
        public void run() {
            writeDateTOFile();//往文件中寫入裸數據
            copyWaveFile(mFileName+".raw", mFileName+".wav");//给裸數據加上頭文件

        }
    }

    /**
     * 這裏將數據寫入文件，但是並不能播放，因为AudioRecord獲得的音頻是原始的裸音頻，
     * 如果需要播放就必須加入一些格式或者編碼的頭信息。但是這样的好處就是你可以對音頻的 裸數據進行處理，比如你要做一個愛說話的TOM
     * 貓在這裏就進行音頻的處理，然後重新封裝 所以說這样得到的音頻比較容易做一些音頻的處理。
     */
    private void writeDateTOFile() {
        // new一個byte數組用來存一些字節數據，大小为緩沖區大小
        byte[] audiodata = new byte[bufferSizeInBytes];
        FileOutputStream fos = null;
        int readsize = 0;
        try {
            File file = new File(mFileName+".raw");
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);// 建立一個可存取字節的文件
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (mIsRecord == true) {
            try {
                readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes);
                if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
                    fos.write(audiodata);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 這裏得到可播放的音頻文件
    private void copyWaveFile(String inFilename, String outFilename) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = sampleRateInHz;
        int channels = 2;
        long byteRate = 16 * sampleRateInHz * channels / 8;
        byte[] data = new byte[bufferSizeInBytes];
        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;
            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 這裏提供一個頭信息。插入這些信息就可以得到可以播放的文件。
     * 为我为啥插入這44個字節，這個還真沒深入研究，不過你隨便打開一個wav
     * 音頻的文件，可以發現前面的頭文件可以說基本一样哦。每種格式的文件都有
     * 自己特有的頭文件。
     */
    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen,
                                     long totalDataLen, long longSampleRate, int channels, long byteRate)
            throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = 16; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }

    public void CopyProfile(String filePath){

        String profileFileName = "profile.yaml";
        InputStream in = null;
        OutputStream out = null;
        try
        {
            in = getResources().openRawResource(R.raw.profile);
            String newFileName = filePath + "/" + profileFileName;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.d("tag", e.getMessage());
        }finally{
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    Log.d("closing input stream",e.getMessage());
                }
            }
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    Log.d( "closing output stream",e.getMessage());
                }
            }
        }

    }
}