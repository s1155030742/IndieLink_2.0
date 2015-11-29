package com.indielink.indielink;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.indielink.indielink.Network.GetProfilePicture;
import com.indielink.indielink.Profile.ProfileContent;

import java.util.HashMap;


public class CardDetailFragment extends Fragment {



    public static CardDetailFragment newInstance(String param1, String param2) {
        CardDetailFragment fragment = new CardDetailFragment();
        return fragment;
    }

    public CardDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_detail, container, false);

        //get cached user data TODO: get this data by HTTP POST
        HashMap<String,String> user = ProfileContent.GetUserProfile();

        //Set Name
        ((TextView) view.findViewById(R.id.Name)).setText("UserName");

        //Set Age
        ((TextView) view.findViewById(R.id.Age)).setText("UserAge");

        //Set Gender
        ((TextView) view.findViewById(R.id.Gender)).setText("UserGender");

        //Set Instrument
        ((TextView) view.findViewById(R.id.Instrument)).setText("UserGender");

        //Set AboutMe
        ((TextView) view.findViewById(R.id.AboutMe)).setText("UserGender");

        // Set Profile Image, TODO: put server URL into GetProfilePicture();
        ImageView ProfilePicture = (ImageView) view.findViewById(R.id.MusicianProfilePicture);
        new GetProfilePicture("URL",ProfilePicture).execute();

        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }


    public static final CardDetailFragment newInstance()
    {
        CardDetailFragment f = new CardDetailFragment();
        return f;
    }




}
