package com.indielink.indielink.Profile;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.indielink.indielink.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Hong on 10/12/2015.
 */
public class TrackScore {

    public ArrayList<String> TrackStyleList, TrackScoreList;


    public ArrayList<String> getNewTrackStyleList(){
        return new ArrayList<String>(Arrays.asList(
                "blues",
                "country",
                "electronic",
                "hard_rock",
                "britpop",
                "jazz",
                "pop_rock",
                "metal",
                "post_rock"
        ));
    }

    public int getNumOfTrack(){
        return getTrackStyleList().size();
    }


    public  ArrayList<String> getTrackStyleList(){return TrackStyleList;}

    public ArrayList<String> getTrackScoreList(ArrayList<String> trackScoreList){
        return trackScoreList.size()==getNumOfTrack()?trackScoreList:null;
    }

    public ArrayList<String> getTrackScoreList(ArrayList<?> List, View view) {
        ArrayList<String> temp = new ArrayList<String>();

        if (List.get(0).getClass().equals(int.class)) {

            ArrayList<RadioGroup> temp2 = new ArrayList<RadioGroup>();
            for (Object rgid: List) temp2.add( (RadioGroup) view.findViewById((int) rgid));
            temp = getTrackScoreList(temp2, view);

        } else if (List.get(0).getClass().equals(RadioGroup.class)) {

            for (Object rg : List)
                temp.add(((RadioButton) view.findViewById(
                        ((RadioGroup) rg).getCheckedRadioButtonId())).getText().toString());

        }
        return temp;
    }



    public TrackScore(ArrayList<String> trackScoreList){
        TrackStyleList = getNewTrackStyleList();
        TrackScoreList = getTrackScoreList(trackScoreList);
    }

    public TrackScore(ArrayList<?> List, View getView) {
        TrackStyleList = getNewTrackStyleList();
        TrackScoreList = getTrackScoreList (List, getView);
    }

    public JSONObject putInJSON(JSONObject js) throws JSONException{
        for( int i = 0; i<9; i++)
            js.put(TrackStyleList.get(i), TrackScoreList.get(i));
        return js;
    }

}


