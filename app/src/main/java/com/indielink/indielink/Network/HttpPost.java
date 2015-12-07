package com.indielink.indielink.Network;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.RequestFuture;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Hong on 23/11/2015.
 */
public class HttpPost {

    Context mContext;
    JSONObject jsReq;

    public HttpPost() {
        mContext = MainActivity.getContext();
        jsReq = new JSONObject();
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

    public JSONObject PostJSONResponseJSON(String Url, JSONObject JSONToPost)
    {
        //reference website: http://programminglife.io/android-volley-synchronous-request/
        //This is a sychonize JSON request resposne
        final String tag = Url;
        final DataCallback callback = new DataCallback(){
            @Override
            public void onSuccess(JSONObject result) {
                jsReq = result;
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Url, JSONToPost, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(tag, response.toString());
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(tag, "Error: " + error.getMessage());
                    }
                });

        Volley.newRequestQueue(mContext).add(jsonObjectRequest);
        return jsReq;

    }

    public interface DataCallback {
        void onSuccess(JSONObject result);
    }

    public void PostJSON(String Url, JSONObject JSONToPost)
    {
        final String TAG = Url;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Url, JSONToPost,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        VolleyLog.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        Volley.newRequestQueue(mContext).add(jsonRequest);
    }

}
