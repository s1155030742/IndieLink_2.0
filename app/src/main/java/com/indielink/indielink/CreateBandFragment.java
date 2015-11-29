package com.indielink.indielink;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Hong on 16/11/2015.
 */
public class CreateBandFragment extends Fragment {

    public CreateBandFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_band, container, false);
        final Button button = (Button) view.findViewById(R.id.SubmitNewBand);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO HTTP POST　Create Band Info.
                ((RootPage)getActivity()).reload();
            }
        });
        return view;
    }
}
