package com.indielink.indielink.Network;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.indielink.indielink.MainActivity;
import com.indielink.indielink.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;

/**
 * Created by Hong on 23/11/2015.
 */
public class HttpPost extends Application{

    JSONObject jsRep;
    String fbid;
    Context mContext;

    public HttpPost() {
        mContext = MainActivity.getContext();
    }

    /*
    protected JSONObject JSONObjectEncode (String[] args)
    {
        /*JSONObject obj = new JSONObject();

        obj.put("name", "foo");
        obj.put("num", new Integer(100));
        obj.put("balance", new Double(1000.21));
        obj.put("is_vip", new Boolean(true));

        return obj;
        return null;
    }
    */

    public JSONObject PostJSON(String Url, JSONObject js)
    {
        final String TAG = Url;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        VolleyLog.d(TAG, response.toString());
                        jsRep = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        Volley.newRequestQueue(mContext).add(jsonRequest);
        return jsRep;
    }

}
