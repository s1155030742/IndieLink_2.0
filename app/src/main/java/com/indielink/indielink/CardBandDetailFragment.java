package com.indielink.indielink;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

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
                .getSerializable("SelectedBand");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_band_detail, container, false);
        ImageView ProfilePicture = (ImageView) view.findViewById(R.id.BandProfilePicture);
        new GetProfilePicture(bandProfileContent.BandPictureURL,ProfilePicture).execute();

        //Set Name
        ((TextView) view.findViewById(R.id.BandName)).setText(bandProfileContent.BandName);
        //Set AboutMe
        ((TextView) view.findViewById(R.id.AboutMe)).setText(bandProfileContent.BandAboutMe);
        //Set Instrument
        ((CheckBox) view.findViewById(R.id.CheckVocal)).setClickable(!bandProfileContent.Vacancy.get("vocal"));
        ((CheckBox) view.findViewById(R.id.CheckBass)).setClickable(!bandProfileContent.Vacancy.get("bass"));
        ((CheckBox) view.findViewById(R.id.CheckGuitar)).setClickable(!bandProfileContent.Vacancy.get("guitar"));
        ((CheckBox) view.findViewById(R.id.CheckDrum)).setClickable(!bandProfileContent.Vacancy.get("drum"));
        ((CheckBox) view.findViewById(R.id.CheckKeyboard)).setClickable(!bandProfileContent.Vacancy.get("keyboard"));
        ((CheckBox) view.findViewById(R.id.CheckOthers)).setClickable(!bandProfileContent.Vacancy.get("other"));

        // Set button onClick Handler
        final Button button = (Button) view.findViewById(R.id.ApplyButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: HTTP POST to: /band/recruit
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
}
