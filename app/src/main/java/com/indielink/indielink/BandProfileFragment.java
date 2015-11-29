package com.indielink.indielink;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.indielink.indielink.Network.GetProfilePicture;
import com.indielink.indielink.Profile.BandProfileContent;
import com.indielink.indielink.Profile.UserRole;

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

       //((TextView) view.findViewById(R.id.BandAboutMe)).setText(("Britpop"));

       // CheckBox checkedTextView = (CheckBox) view.findViewById(R.id.CheckVocal);
      //  if (checkedTextView.isChecked())
       //     checkedTextView.setChecked(false);
      //  else
       // checkedTextView.setChecked(true);
        CheckBox c = (CheckBox) view.findViewById(R.id.CheckVocal);
        if (c.isChecked())
            c.setEnabled(true);

        else
            c.setEnabled(false);

        ((CheckBox) view.findViewById(R.id.CheckBass)).setChecked(true);
        ((CheckBox) view.findViewById(R.id.CheckGuitar)).setChecked(false);
        ((CheckBox) view.findViewById(R.id.CheckDrum)).setChecked(true);
        ((CheckBox) view.findViewById(R.id.CheckKeyboard)).setChecked(true);
        ((CheckBox) view.findViewById(R.id.CheckOthers)).setChecked(true);

        //((TextView) view.findViewById(R.id.GuitarVacancy)).setText(("Available"));
//        ((TextView) view.findViewById(R.id.VocalVacancy)).setText(("Available"));
 //       ((TextView) view.findViewById(R.id.BassVacancy)).setText(("Available"));
  //      ((TextView) view.findViewById(R.id.KeyboardVacancy)).setText(("Available"));
   //     ((TextView) view.findViewById(R.id.DrumVacancy)).setText(("Occupied"));

        //Set bandName
        ((TextView) view.findViewById(R.id.BandName)).setText(bandProfileContent.BandName);

        //Set bandAboutMe
        ((TextView) view.findViewById(R.id.BandAboutMe)).setText(bandProfileContent.BandAboutMe);

        //Set BandPicture
        ImageView ProfilePicture = (ImageView) view.findViewById(R.id.BandProfilePicture);
        new GetProfilePicture(bandProfileContent.BandPictureURL,ProfilePicture).execute();

        Switch RoleSwitch = (Switch) view.findViewById(R.id.ChangeRole);
        if(UserRole.GetUserRole()=="bandProfileContent.BandName")
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
                    UserRole.IsBand(bandProfileContent.BandName);
                }
                else {
                    UserRole.IsMusician();
                }
                Log.v("Switch State=", "" + UserRole.GetUserRole());
            }
        });
        return view;
    }
}
