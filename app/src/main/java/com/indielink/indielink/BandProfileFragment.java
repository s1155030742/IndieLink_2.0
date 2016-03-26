package com.indielink.indielink;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.indielink.indielink.ApplicationList.ApplicationListContent;
import com.indielink.indielink.CustomAdapter.RowItem;
import com.indielink.indielink.Network.GetProfilePicture;
import com.indielink.indielink.Network.HttpPost;
import com.indielink.indielink.Profile.BandProfileContent;
import com.indielink.indielink.Profile.UserRole;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BandProfileFragment extends Fragment {

    private BandProfileContent bandProfileContent;

    public BandProfileFragment() {
    }

    String tag = "BandProfileFragment";
    String Url = "http://137.189.97.88:8080/user/invite";

    JSONObject JSONRep, JSONToPost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get Selected BandProfileContent
        bandProfileContent = (BandProfileContent) this.getArguments()
                .getSerializable("userBand");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_band_profile, container, false);

        JSONToPost = new JSONObject() {{try {
                //things to put in json
            put("access_token", AccessToken.getCurrentAccessToken().getToken());
            put("fb_user_id", AccessToken.getCurrentAccessToken().getUserId());
            put("identity", "");

            } catch (JSONException e) {
            e.printStackTrace();
            }
        }};

        (new HttpPost(view.getContext()) {
            @Override
            public void onHttpResponse(JSONObject jsRep) {
                //action onResponse and pass data from response to activity
                Log.d(tag, jsRep.toString());
                JSONRep = jsRep;
                //onCreateViewFromJSON(view);
            }
        }).PostJSONResponseJSON(Url,JSONToPost);

        ((CheckBox) view.findViewById(R.id.CheckVocal)).setChecked(bandProfileContent.Vacancy.get("vocal"));
        ((CheckBox) view.findViewById(R.id.CheckBass)).setChecked(bandProfileContent.Vacancy.get("bass"));
        ((CheckBox) view.findViewById(R.id.CheckGuitar)).setChecked(bandProfileContent.Vacancy.get("guitar"));
        ((CheckBox) view.findViewById(R.id.CheckDrum)).setChecked(bandProfileContent.Vacancy.get("drum"));
        ((CheckBox) view.findViewById(R.id.CheckKeyboard)).setChecked(bandProfileContent.Vacancy.get("keyboard"));
        ((CheckBox) view.findViewById(R.id.CheckOthers)).setChecked(bandProfileContent.Vacancy.get("other"));

        //Set bandName
        ((TextView) view.findViewById(R.id.BandName)).setText(bandProfileContent.BandName);

        //Set bandAboutMe
        ((TextView) view.findViewById(R.id.BandAboutMe)).setText(bandProfileContent.BandAboutMe);

        //Set BandPicture
        ImageView ProfilePicture = (ImageView) view.findViewById(R.id.BandProfilePicture);
        new GetProfilePicture(bandProfileContent.BandPictureURL, ProfilePicture).execute();

        // Set button onClick Handler
        final Button button = (Button) view.findViewById(R.id.ApplicationButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONToPost = new JSONObject() {
                    {
                        try {
                            put("access_token", AccessToken.getCurrentAccessToken().getToken());
                            put("fb_user_id", AccessToken.getCurrentAccessToken().getUserId());
                            put("identity", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, Url, JSONToPost, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(tag, response.toString());
                                JSONRep = response;
                                onCreateViewFromJSON(view);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(tag, "Error: " + error.getMessage());
                            }
                        });
                Volley.newRequestQueue(view.getContext()).add(jsonObjectRequest);

            }});

        return view;
    }

    public void onCreateViewFromJSON(View view) {

        try {
            JSONArray Users = JSONRep.getJSONArray("user");
            for (int i = 0; i < Users.length(); i++) {
                JSONObject User = Users.getJSONObject(i);
                ApplicationListContent.addItem(User);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Fragment fragment = new ApplicationListFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction().addToBackStack("ApplicationList")
                .replace(R.id.frame_container, fragment).commit();

        view.requestLayout();

    }


}



        //TODO　Implement only on sem2
        /*
        Switch RoleSwitch = (Switch) view.findViewById(R.id.ChangeRole);
        if(UserRole.GetUserRole()== bandProfileContent.id)
        {
            RoleSwitch.setChecked(true);
        }
        else
        {
            RoleSwitch.setChecked(false);
        }
        RoleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (CompoundButton buttonView,boolean isChecked){
                if (isChecked) {
                    UserRole.IsBand(bandProfileContent.id);
                }
                else {
                    UserRole.IsMusician();
                }
                Log.v("Switch State=", "" + UserRole.GetUserRole());
            }
        });*/
        //return view;
