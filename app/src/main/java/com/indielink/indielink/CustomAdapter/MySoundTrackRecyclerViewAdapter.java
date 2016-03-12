package com.indielink.indielink.CustomAdapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indielink.indielink.Audio.Audio;
import com.indielink.indielink.Network.HttpPost;
import com.indielink.indielink.R;
import com.indielink.indielink.SoundTrackFragment.OnListFragmentInteractionListener;
import com.indielink.indielink.Profile.SoundTrackContent.SoundTrackItem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import butterknife.OnClick;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SoundTrackItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySoundTrackRecyclerViewAdapter extends RecyclerView.Adapter<MySoundTrackRecyclerViewAdapter.ViewHolder> {

    public List<SoundTrackItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Audio audio = null;
    private Context mContext;


    public MySoundTrackRecyclerViewAdapter(List<SoundTrackItem> items,
                                           OnListFragmentInteractionListener listener,
                                           String FilePath,
                                           Audio audi,
                                           Context c) {
        mValues = items;
        mListener = listener;
        audio = audi;
        audio.mFilePath = FilePath+"/";
        mContext = c;
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
        holder.mNameView.setText(mValues.get(position).name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mImgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Url = "http://137.189.97.88:8080/ip/user/soundtrack";

                TextView t = holder.mNameView;
                String fileName =  t.getText().toString();
                String filePath =  audio.mFilePath + fileName;
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

                (new HttpPost(mContext){
                    @Override
                    public void onHttpResponse(){
                        makeToast("Upload Complete!");
                    }
                }).UploadFile(Url, bytes,filePath);

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
        public final ImageView mImgRemove;
        public final TextView mNameView;
        public SoundTrackItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mImgUpload = (ImageView) view.findViewById(R.id.upload);
            mImgRemove = (ImageView) view.findViewById(R.id.remove);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
