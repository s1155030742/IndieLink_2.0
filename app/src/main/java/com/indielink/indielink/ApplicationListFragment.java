package com.indielink.indielink;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.indielink.indielink.ApplicationList.ApplicationListContent;
import com.indielink.indielink.Profile.BandProfileContent;

public class ApplicationListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;
    private BandProfileContent bandProfileContent;

    // TODO: Rename and change types of parameters
    public static ApplicationListFragment newInstance(String param1, String param2) {
        ApplicationListFragment fragment = new ApplicationListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ApplicationListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bandProfileContent = (BandProfileContent) this.getArguments()
                .getSerializable("userBand");
        if (getArguments() != null) {
        }

        // TODO: Change Adapter to display your content
        mAdapter = new ArrayAdapter<ApplicationListContent.ApplicationItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, ApplicationListContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v("Item Clicked", String.valueOf(id));
        /*
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(ApplicationListContent.ITEMS.get(position).id);
        }
        */
        DialogFragment fragment = new ReplyFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("id",String.valueOf(id));
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), "Reply");
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();
        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }
}
