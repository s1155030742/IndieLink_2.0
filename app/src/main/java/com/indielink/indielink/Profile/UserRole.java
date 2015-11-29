package com.indielink.indielink.Profile;

/**
 * Created by lawFuck on 2015/11/20.
 */
public class UserRole {
    private static String Band;

    public static void IsBand(String band)
    {
        Band=band;
    }

    public static void IsMusician()
    {
        Band="";
    }

    public static String GetUserRole()
    {
        return Band;
    }
}
