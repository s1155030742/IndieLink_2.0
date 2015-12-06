package com.indielink.indielink.Profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by a-kele on 11/22/2015.
 */

public class BandProfileContent implements Serializable
{
    public String BandName;
    public String BandAboutMe;
    public String id;
    public String BandPictureURL = "http://137.189.97.88:8080/upload/";
    public HashMap<String,Boolean> Vacancy = new HashMap<String,Boolean>();

    public BandProfileContent(String Name,String AboutMe,String id,ArrayList<String> Instrument)
    {

        // TODO add parameter after Json is confirm
        setVacancy();
        this.BandName = Name;
        this.BandAboutMe = AboutMe;
        this.id = id;
        this.BandPictureURL+=(id+".jpg");

        for(String key : Instrument)
        {
            Vacancy.put(key,true);
        }
    }

    public void setVacancy()
    {
        this.Vacancy.put("vocal",false);
        this.Vacancy.put("bass",false);
        this.Vacancy.put("guitar",false);
        this.Vacancy.put("",false);
        this.Vacancy.put("",false);
        this.Vacancy.put("",false);
    }
}

