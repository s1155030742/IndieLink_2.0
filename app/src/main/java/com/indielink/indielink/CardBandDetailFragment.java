package com.indielink.indielink;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.indielink.indielink.Network.GetProfilePicture;
import com.indielink.indielink.Profile.BandProfileContent;


public class CardBandDetailFragment extends Fragment {

    private BandProfileContent bandProfileContent;
    public static CardBandDetailFragment newInstance(String param1, String param2) {
        CardBandDetailFragment fragment = new CardBandDetailFragment();
        return fragment;
    }

    public CardBandDetailFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bandProfileContent = (BandProfileContent) this.getArguments()
                .getSerializable("userBand");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_band_detail, container, false);
        ImageView ProfilePicture = (ImageView) view.findViewById(R.id.BandProfilePicture);






        new GetProfilePicture("URL",ProfilePicture).execute();
        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
}
