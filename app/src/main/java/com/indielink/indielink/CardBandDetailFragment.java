package com.indielink.indielink;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.indielink.indielink.Network.GetProfilePicture;
import com.indielink.indielink.Network.HttpPost;
import com.indielink.indielink.Profile.BandProfileContent;
import com.indielink.indielink.Profile.InstrumentList;
import com.indielink.indielink.Profile.ProfileContent;
import com.indielink.indielink.Profile.TrackScore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class CardBandDetailFragment extends Fragment {

    String tag = "CardBandDetailFragment";
    String Url = "http://137.189.97.88:8080/band/recruit";

    JSONObject JSONToPost,JSONRep;

    CheckBox vocal;
    CheckBox bass;
    CheckBox guitar;
    CheckBox drum;
    CheckBox keyboard;
    CheckBox other;

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
        final View view = inflater.inflate(R.layout.fragment_card_band_detail, container, false);
        ImageView ProfilePicture = (ImageView) view.findViewById(R.id.BandProfilePicture);
        new GetProfilePicture(bandProfileContent.BandPictureURL, ProfilePicture).execute();

        //Set Name
        ((TextView) view.findViewById(R.id.BandName)).setText(bandProfileContent.BandName);
        //Set AboutMe
        ((TextView) view.findViewById(R.id.AboutMe)).setText(bandProfileContent.BandAboutMe);
        //Set Instrument
        vocal = (CheckBox) view.findViewById(R.id.CheckVocal);
        bass = (CheckBox) view.findViewById(R.id.CheckBass);
        guitar = (CheckBox) view.findViewById(R.id.CheckGuitar);
        drum = (CheckBox) view.findViewById(R.id.CheckDrum);
        keyboard = (CheckBox) view.findViewById(R.id.CheckKeyboard);
        other = (CheckBox) view.findViewById(R.id.CheckOthers);

        //Set Instrument
        vocal.setClickable(!bandProfileContent.Vacancy.get("vocal"));
        bass.setClickable(!bandProfileContent.Vacancy.get("bass"));
        guitar.setClickable(!bandProfileContent.Vacancy.get("guitar"));
        drum.setClickable(!bandProfileContent.Vacancy.get("drum"));
        keyboard.setClickable(!bandProfileContent.Vacancy.get("keyboard"));
        other.setClickable(!bandProfileContent.Vacancy.get("other"));

        // Set button onClick Handler
        final Button button = (Button) view.findViewById(R.id.ApplyButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ArrayList<String> InstrumentList = new ArrayList<String>();

                /*if (vocal.isChecked()) InstrumentList.add("vocal");
                if (bass.isChecked()) InstrumentList.add("bass");
                if (guitar.isChecked()) InstrumentList.add("guitar");
                if (drum.isChecked()) InstrumentList.add("drum");
                if (keyboard.isChecked()) InstrumentList.add("keyboard");
                if (other.isChecked()) InstrumentList.add("other");*/

                JSONToPost = new JSONObject() {
                    {
                        try {
                            put("access_token", AccessToken.getCurrentAccessToken().getToken());
                            put("fb_user_id", AccessToken.getCurrentAccessToken().getUserId());
                            put("band_id", bandProfileContent.id);
                            put("identity", "");
                            put("instrument", (new InstrumentList((new ArrayList<Integer>(Arrays.asList(
                                    R.id.CheckVocal,
                                    R.id.CheckGuitar,
                                    R.id.CheckBass,
                                    R.id.CheckDrum,
                                    R.id.CheckKeyboard,
                                    R.id.CheckOthers))), view)).getAddList());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                /*
                for(String Instrument : InstrumentList){
                    //make JSONObject for http post
                    JSONObject obj = new JSONObject();
                    try
                    {
                        obj.put("access_token", AccessToken.getCurrentAccessToken().getToken());
                        obj.put("fb_user_id", AccessToken.getCurrentAccessToken().getUserId());
                        obj.put("band_id", bandProfileContent.id);
                        obj.put("identity","");
                        obj.put("instrument", Instrument);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    */

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, Url, JSONToPost, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(tag, response.toString());
                                JSONRep = response;
                                //onCreateViewFromJSON(view);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(tag, "Error: " + error.getMessage());
                            }
                        });
                Volley.newRequestQueue(view.getContext()).add(jsonObjectRequest);
            }
        });
        return view;
    }

                    /*
                    //Post to server
                    HttpPost httpPost = new HttpPost();
                    JSONObject response = httpPost.PostJSONResponseJSON("http://137.189.97.88:8080/band/recruit", obj);
                    try {
                        response.getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //TODO: HTTP POST to: /band/recruit
                    getFragmentManager().popBackStack();
                    */



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
}
