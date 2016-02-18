package com.indielink.indielink.CustomAdapter;

import android.content.Context;
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
import com.indielink.indielink.R;
import com.indielink.indielink.SoundTrackFragment.OnListFragmentInteractionListener;
import com.indielink.indielink.Profile.SoundTrackContent.SoundTrackItem;

import java.io.File;
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
    //private MySoundTrackRecyclerViewAdapter mAdaptor = this;
    public MySoundTrackRecyclerViewAdapter(List<SoundTrackItem> items, OnListFragmentInteractionListener listener,String FilePath) {
        mValues = items;
        mListener = listener;
        audio.mFilePath = FilePath+"/";
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

        holder.mNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Audio audio = Audio.getInstance();
                TextView t = (TextView) v;
                String FileName =  audio.mFilePath + t.getText().toString();

                if(audio.play == false){
                    audio.mFileName = FileName;
                    audio.play = true;
                    audio.onPlay(audio.play);
                }else
                {
                    if(audio.mFileName == FileName){
                        audio.play = false;
                        audio.onPlay(audio.play);
                    } else {
                        audio.onPlay(audio.play);
                        audio.mFileName = FileName;
                        audio.play = true;
                        audio.onPlay(audio.play);
                    }
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
                Log.d("LOG :::", "Element " + holder.getAdapterPosition() + " clicked.");
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
