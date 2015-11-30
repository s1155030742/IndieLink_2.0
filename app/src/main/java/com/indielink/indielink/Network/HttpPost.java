package com.indielink.indielink.Network;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
public class HttpPost extends AppCompatActivity{

    JSONObject jsRep;

    protected JSONObject PostJSON(String Url, JSONObject js)
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
        Volley.newRequestQueue(super.getApplicationContext()).add(jsonRequest);
        return jsRep;
    }

}
