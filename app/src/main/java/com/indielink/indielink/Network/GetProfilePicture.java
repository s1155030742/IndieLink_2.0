package com.indielink.indielink.Network;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.indielink.indielink.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by a-kele on 11/10/2015.
 * logic for getting facebook profile pic and stored it in memory
 */
public class GetProfilePicture extends AsyncTask<Void, Void, Drawable> {

    private final String mUrl;
    private ImageView pic ;
    private Drawable result;

    public GetProfilePicture(String url, ImageView ProfilePicture){
        mUrl = url;
        pic = ProfilePicture;
    }

    @Override
    protected Drawable doInBackground(Void... params) {
        try {
            if (result == null) {
                result = Drawable.createFromStream((InputStream) new URL(mUrl).getContent(), "src");
            }
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Drawable result)
    {
        if(pic != null)
        {
        pic.setImageDrawable(result);
        }
    }

}