package com.indielink.indielink.Network;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.indielink.indielink.MainActivity;
import com.indielink.indielink.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.indielink.indielink.RootPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.ref.ReferenceQueue;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Hong on 23/11/2015.
 */
public class HttpPost extends Application{

    private static Context mContext;
    JSONObject jsRep;
    private static HttpPost mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Context getContext(){
        return mContext;
    }

    public static synchronized HttpPost getInstance() {
        return mInstance;
    }

    public HttpPost() {
        mContext = MainActivity.getContext();
        jsRep = null;
        mInstance = this;
    }

    public void PostJSONResponseJSON(String Url, JSONObject JSONToPost){
        final String tag = "POSTJSON";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Url, JSONToPost, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(tag, response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(tag, "Error: " + error.getMessage());
                    }
                });
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);
        //return jsRep;
    }

/*
    public JSONObject PostJSONResponseJSON(final String Url, final JSONObject JSONToPost){

        final CountDownLatch latchA = new CountDownLatch(1);

        Thread t = new Thread(new Runnable() {
            HttpPost httpPost;
            @Override
            public void run() {
                Log.d("RT", "Thread t Begins");
                ThreadA threadA = new ThreadA(Url, JSONToPost);
                try {
                    JSONObject jsonObject = threadA.execute().get(10, TimeUnit.SECONDS);
                    this.httpPost.jsReq = jsonObject;
                    //parseA(jsonObject);
                    latchA.countDown();
                    Log.d("RT", "Thread t Ends");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            latchA.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("RT", "StepA END");
        return jsReq;
    }



    public JSONObject NotUIPostJSONResponseJSON(String Url, JSONObject JSONToPost){
        int REQUEST_TIMEOUT = 10;

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Url, JSONToPost, future, future);
        Volley.newRequestQueue(mContext).add(request);

        try {
            return future.get(REQUEST_TIMEOUT, TimeUnit.SECONDS); // this will block (forever)
        } catch (InterruptedException e) {
            // exception handling
        } catch (ExecutionException e) {
            // exception handling
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
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

    /*
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

        //HttpPost.getInstance().addQueue(jsonObjectRequest);
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);

        return jsReq;
    }

    public void addQueue(JsonObjectRequest  jsonObjectRequest){
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);
    }

    public interface DataCallback {
        void onSuccess(JSONObject result);
    }
    */


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


    //Transform bitmap to base 64 String for uploading to web sever
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(String Url, Bitmap imageToUploadBitmap){

        //reference: https://www.simplifiedcoding.net/android-volley-tutorial-to-upload-image-to-server/

        //Showing the progress dialog
        //final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);

        final Bitmap bmp = imageToUploadBitmap;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        //loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //loading.dismiss();

                        //Showing toast
                        Toast.makeText(mContext, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bmp);

                //Getting Image Name
                //String name = editTextName.getText().toString().trim();   //useless

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("HardCodePICURL", image);
                //params.put(KEY_NAME, name);   //useless

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}


/*
class ThreadA extends AsyncTask<Void, Void, JSONObject> {
    String Url;
    JSONObject JSONToPost;

    public ThreadA(String inputUrl, JSONObject js) {
        Url = inputUrl;
        JSONToPost = js;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        final RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Url, JSONToPost, future, future);
        Volley.newRequestQueue(HttpPost.getContext()).add(request);
        try {
            return future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }
}
*/