package com.indielink.indielink;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.indielink.indielink.Network.HttpPost;

import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;

//import com.google.android.gms.plus.PlusOneButton;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    // The URL to +1.  Must be a valid URL.
    //private final String PLUS_ONE_URL = "http://developer.android.com";

    // The request code must be 0 or greater.
    //private static final int PLUS_ONE_REQUEST_CODE = 0;

    private Button mSubmitChangeButton;
    private String  fbid;
  //  private String access_token;
    private EditText aboutMe;
    private CheckBox isVocal, isGuitar, isBass, isDrum, isKeyboard,isOther;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EditProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        aboutMe = (EditText) view.findViewById(R.id.EditBandAboutMe);
        isVocal = (CheckBox) view.findViewById(R.id.EditProfileisVocal);
        isGuitar = (CheckBox) view.findViewById(R.id.EditProfileisGuitar);
        isBass = (CheckBox) view.findViewById(R.id.EditProfileisBass);
        isDrum = (CheckBox) view.findViewById(R.id.EditProfileisDrum);
        isKeyboard = (CheckBox) view.findViewById(R.id.EditProfileisKeyboard);
        isOther = (CheckBox) view.findViewById(R.id.EditProfileisOthers);
        /*
        aboutMe = (EditText) view.findViewById(R.id.EditBandAboutMe);
        isVocal = (CheckBox) view.findViewById(R.id.EditProfileisVocal);
        isGuitar = (CheckBox) view.findViewById(R.id.EditProfileisGuitar);
        isBass = (CheckBox) view.findViewById(R.id.EditProfileisBass);
        isDrum = (CheckBox) view.findViewById(R.id.EditProfileisDrum);
        isKeyboard = (CheckBox) view.findViewById(R.id.EditProfileisKeyboard);
        isOther = (CheckBox) view.findViewById(R.id.EditProfileisOthers);
        */

        // Set button onClick Handler
        final Button button = (Button) view.findViewById(R.id.SubmitNewBand);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //make instrument JsonArray first
                JSONArray list = new JSONArray();
                if (isVocal.isChecked()) list.put("vocal");
                if (isGuitar.isChecked()) list.put("guitar");
                if (isBass.isChecked()) list.put("bass");
                if (isDrum.isChecked()) list.put("drum");
                if (isKeyboard.isChecked()) list.put("keyboard");
                //if(isOther.isChecked())list.put("something");

                //aboutMe.setText("List Done");
                fbid = Profile.getCurrentProfile().getId();
                AccessToken access_token = AccessToken.getCurrentAccessToken();
                //make JSONObject for http post
                JSONObject obj = new JSONObject();

                try {
                    obj.put("about_me", aboutMe.getText());
                    obj.put("access_token", AccessToken.getCurrentAccessToken().getToken());
                    obj.put("fb_user_id",  AccessToken.getCurrentAccessToken().getUserId());
                    obj.put("instrument", list);

                    //aboutMe.setText("JSON done");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Post to server
                aboutMe.setText(obj.toString());
                HttpPost httpPost = new HttpPost();
                JSONObject response = httpPost.PostJSON("http://137.189.97.88:8080/user/edit", obj);
                try{
                response.getString("Status");


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
