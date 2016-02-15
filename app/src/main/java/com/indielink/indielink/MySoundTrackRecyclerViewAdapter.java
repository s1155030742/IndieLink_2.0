package com.indielink.indielink;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.indielink.indielink.SoundTrackFragment.OnListFragmentInteractionListener;
import com.indielink.indielink.Profile.SoundTrackContent.SoundTrackItem;
import  com.indielink.indielink.CustomListener.OnSwipeTouchListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SoundTrackItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySoundTrackRecyclerViewAdapter extends RecyclerView.Adapter<MySoundTrackRecyclerViewAdapter.ViewHolder> {

    public List<SoundTrackItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final OnSwipeTouchListener mOnSwipeListener;

    public MySoundTrackRecyclerViewAdapter(List<SoundTrackItem> items, OnListFragmentInteractionListener listener,OnSwipeTouchListener OnSwipe ) {
        mValues = items;
        mListener = listener;
        mOnSwipeListener = OnSwipe;
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
        holder.mImg.setImageResource(android.R.drawable.ic_menu_upload);
        holder.mIdView.setText(mValues.get(position).id);
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

        holder.mView.setOnTouchListener(mOnSwipeListener);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final ImageView mImg;
        public final TextView mNameView;
        public SoundTrackItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mImg = (ImageView) view.findViewById(R.id.upload);
            mNameView = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
