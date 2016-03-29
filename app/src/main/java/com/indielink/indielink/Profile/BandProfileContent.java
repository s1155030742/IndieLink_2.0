package com.indielink.indielink.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class BandProfileContent implements Serializable
{
    public String BandName;
    public String BandAboutMe;
    public String id;
    public String BandPictureURL = "http://137.189.97.88:8080/uploads/";
    public HashMap<String,Boolean> Vacancy = new HashMap<String,Boolean>();

    public BandProfileContent(JSONObject js) throws JSONException {

        this.BandName = js.get("name").toString();
        this.BandAboutMe = js.get("about_me").toString();
        this.id = js.get("band_id").toString();

        this.BandPictureURL+=(id+".jpg");

        setVacancy();

    }

    public BandProfileContent(String Name,String AboutMe,String id,ArrayList<String> Instrument)
    {

        //setVacancy();
        this.BandName = Name;
        this.BandAboutMe = AboutMe;
        this.id = id;
        this.BandPictureURL+=(id+".jpg");

        /*for(String key : Instrument)
        {
            Vacancy.put(key,true);
        }*/
        setVacancy(Instrument);
    }

    public void setVacancy()
    {
        //should this be "true rather than "false" as default setting?
        this.Vacancy.put("vocal",false);
        this.Vacancy.put("bass", false);
        this.Vacancy.put("guitar",false);
        this.Vacancy.put("drum",false);
        this.Vacancy.put("keyboard",false);
        this.Vacancy.put("other", false);
    }

    public void setVacancy(ArrayList<String> Instrument)
    {
        setVacancy();
        for(String key : Instrument) Vacancy.put(key,true);
    }

    public void setVacancy(String Instrument) {
        this.Vacancy.put(Instrument, true);
    }

    public void setVacancy(String Instrument, boolean isVacan) {
        this.Vacancy.put(Instrument, isVacan);
    }

    public boolean isInstrumentExist(String Instrument) {
        return  this.Vacancy.containsKey(Instrument);
    }

    public boolean isVacan(String Instrument) {
        return isInstrumentExist(Instrument) ? this.Vacancy.get(Instrument): false;
    }


}

