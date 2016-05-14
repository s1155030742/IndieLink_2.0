package com.indielink.indielink.CustomAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.indielink.indielink.Audio.Audio;
import com.indielink.indielink.Network.HttpPost;
import com.indielink.indielink.R;
import com.indielink.indielink.Profile.SoundTrackContent.SoundTrackItem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class MySoundTrackRecyclerViewAdapter extends RecyclerView.Adapter<MySoundTrackRecyclerViewAdapter.ViewHolder> {

    public List<SoundTrackItem> mValues;
    private Audio mAudio = null;
    private Context mContext;
    private String mUserId;

    public MySoundTrackRecyclerViewAdapter(List<SoundTrackItem> items,
                                           String FilePath,
                                           Audio audio,
                                           Context context,
                                           String userId) {
        mValues = items;
        mAudio = audio;
        mAudio.mFilePath = FilePath+"/";
        mContext = context;
        mUserId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_soundtrack, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mImgUpload.setImageResource(android.R.drawable.ic_menu_upload);
        holder.mImgRemove.setImageResource(android.R.drawable.ic_menu_delete);
        holder.mImgRecommend.setImageResource(android.R.drawable.btn_star);
        holder.mNameView.setText(mValues.get(position).name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.mImgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Url = "http://137.189.97.88:8080/user/soundtrack/"+mUserId;

                TextView t = holder.mNameView;
                String fileName =  t.getText().toString();
                String filePath =  mAudio.mFilePath + fileName;
                File file = new File(filePath);

                //reference: http://stackoverflow.com/questions/10039672/android-how-to-read-file-in-bytes
                int size = (int) file.length();
                byte[] bytes = new byte[size];
                try {
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                    buf.read(bytes, 0, bytes.length);
                    buf.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                HttpPost httpPost = (new HttpPost(mContext){
                    @Override
                    public void onHttpResponse(){
                        makeToast("Upload Complete!");
                    }
                });
                httpPost.loading();
                mAudio.audio_analysis(filePath,filePath,"");
                httpPost.resume();
                httpPost.UploadFile(Url, bytes, filePath);
            }
        });

        holder.mNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Audio audio = Audio.getInstance();
                TextView t = (TextView) v;
                String FileName =  audio.mFilePath + t.getText().toString();

                if(FileName.toString().equals(audio.mFileName.toString())) {
                    audio.stopPlaying();
                    audio.mFileName = "";
                } else
                {
                    audio.mFileName = FileName;
                    audio.startPlaying();
                }
            }
        });

        holder.mImgRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Url = "http://137.189.97.88:8080/band/recommend";

                TextView t = holder.mNameView;
                String fileName = t.getText().toString();
                String filePath = mAudio.mFilePath + fileName;
                File file = new File(filePath);


                HttpPost httpPost = (new HttpPost(mContext) {
                    @Override
                    public void onHttpResponse() {
                        makeToast("Upload Complete!");
                    }
                });
                httpPost.loading();
                mAudio.audio_analysis(filePath, filePath, "");
                httpPost.resume();
            }
        });

        holder.mImgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t = holder.mNameView;
                Audio audio = Audio.getInstance();
                String FileName =  audio.mFilePath + t.getText().toString();
                File file = new File(FileName);
                file.delete();
                mValues.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImgUpload;
        public final ImageView mImgRecommend;
        public final ImageView mImgRemove;
        public final TextView mNameView;
        public SoundTrackItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mImgUpload = (ImageView) view.findViewById(R.id.upload);
            mImgRecommend = (ImageView) view.findViewById(R.id.recommend);
            mImgRemove = (ImageView) view.findViewById(R.id.remove);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}