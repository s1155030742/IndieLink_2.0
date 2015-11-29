package com.indielink.indielink.Profile;

import android.text.format.Time;

import com.facebook.Profile;
import com.indielink.indielink.Network.GetProfilePicture;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ProfileContent {

    private static HashMap<String,String> User = new HashMap<String,String>();
    public static String ProfilePictureURL;

    public static void InitializeProfile(JSONObject object)
    {
        try {
            //get url from Fackbook Graph API
            String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
            if(User.size()==0)
            {
                String gender = object.get("gender").toString();
                int birth = Integer.parseInt(object.get("birthday").toString().substring(6, 10));
                Time currentTime = new Time();
                currentTime.setToNow();
                String age = String.valueOf(currentTime.year-birth);

                Profile profile = Profile.getCurrentProfile();
                User.put("UserName",profile.getName());
                User.put("UserAge", age);
                User.put("UserGender",gender);

                //TODO: HTTP POST to server to get user information
                User.put("Instrument","");
                User.put("AboutMe","");
            }
            if(url != ProfilePictureURL && !url.isEmpty())
            {
                ProfilePictureURL = url;
                new GetProfilePicture(ProfileContent.ProfilePictureURL,null).execute();
            }
            return;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String,String> GetUserProfile()
    {
        return User;
    }
}
