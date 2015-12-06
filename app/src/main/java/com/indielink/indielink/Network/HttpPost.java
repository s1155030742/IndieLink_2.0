package com.indielink.indielink.Network;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
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

/**
 * Created by Hong on 23/11/2015.
 */
public class HttpPost extends Application{

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

    public JSONObject PostJSONResponseJSON(String Url, JSONObject JSONToPost)
    {
        //This is a sychonize JSON request resposne

        final String TAG = Url;

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Url, JSONToPost, future, future);

        // If you want to be able to cancel the request:
        //future.setRequest(requestQueue.add(request));

        // Otherwise:
        //requestQueue.add(request);
        Volley.newRequestQueue(mContext).add(jsonRequest);

        try {
            JSONObject response = future.get();
            // do something with response
            return response;

        } catch (InterruptedException e) {
            // handle the error
        } catch (ExecutionException e) {
            // handle the error
        }
        return null;
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
