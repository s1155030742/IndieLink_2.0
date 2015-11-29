package com.indielink.indielink.Profile;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by a-kele on 11/22/2015.
 */

public class BandProfileContent implements Serializable
{
    public String BandName;
    public String BandAboutMe;
    public String BandPictureURL;
    public HashMap<String,Boolean> Vacancy = new HashMap<String,Boolean>();

    public BandProfileContent(String Name)
    {
        // TODO add parameter after Json is confirm
        this.BandName = Name;
        this.BandAboutMe = "This is a HardCode test";
        this.BandPictureURL = "137.189.97.88";
    }
}

