package com.indielink.indielink;

import android.app.Activity;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indielink.indielink.Network.GetProfilePicture;
import com.indielink.indielink.Profile.ProfileContent;

import org.w3c.dom.Text;

import java.util.HashMap;


public class ProfileFragment extends Fragment implements AbsListView.OnItemClickListener {

    private String url;
    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //get cached user data
        HashMap<String,String> user = ProfileContent.GetUserProfile();

        // Set Profile Image
        ImageView ProfilePicture = (ImageView) view.findViewById(R.id.ProfilePicture);
        new GetProfilePicture(ProfileContent.ProfilePictureURL,ProfilePicture).execute();

        //Set Name
        ((TextView) view.findViewById(R.id.UserName)).setText(user.get("UserName"));

        //Set Age
        ((TextView) view.findViewById(R.id.UserAge)).setText(user.get("UserAge"));

        //Set Gender
        ((TextView) view.findViewById(R.id.UserGender)).setText(user.get("UserGender"));

        //Set Instrument
        ((TextView) view.findViewById(R.id.UserInstrument)).setText("test");

        //Set AboutMe
        ((TextView) view.findViewById(R.id.ProfileAboutMe)).setText("test");

        // Set button onClick Handler
        final Button button = (Button) view.findViewById(R.id.EditProfile);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = new EditProfileFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack("EditProfile")
                        .replace(R.id.frame_container, fragment).commit();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(ProfileContent.ITEMS.get(position).id);
        }*/
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }
}
