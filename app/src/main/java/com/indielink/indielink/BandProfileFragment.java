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
    public BandProfileFragment() {}
    private boolean mChecked;

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
        View view = inflater.inflate(R.layout.fragment_band_profile, container, false);

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
        new GetProfilePicture(bandProfileContent.BandPictureURL,ProfilePicture).execute();

        // Set button onClick Handler
        final Button button = (Button) view.findViewById(R.id.ApplicationButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: HTTP POST to: /user/invite
                JSONObject obj = new JSONObject();
                try {
                    obj.put("access_token", AccessToken.getCurrentAccessToken().getToken());
                    obj.put("fb_user_id",  AccessToken.getCurrentAccessToken().getUserId());
                    obj.put("identity", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Post to server
                HttpPost httpPost = new HttpPost();
                JSONObject response;
                response = httpPost.PostJSONResponseJSON("http://137.189.97.88:8080/user/invite", obj);
                try{
                    JSONArray Users = response.getJSONArray("user");
                    for (int i=0; i<Users.length(); i++)
                    {
                        JSONObject User = Users.getJSONObject(i);
                        ApplicationListContent.addItem(User);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Fragment fragment = new ApplicationListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack("ApplicationList")
                        .replace(R.id.frame_container, fragment).commit();
            }
        });

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
        return view;
    }
}
