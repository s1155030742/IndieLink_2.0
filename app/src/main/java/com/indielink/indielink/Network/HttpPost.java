package com.indielink.indielink.Network;

import android.app.Activity;
import android.app.Application;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.DocumentsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpHeaderParser;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Hong on 23/11/2015.
 */

public class HttpPost extends Activity{

    ProgressDialog progress;
    public static Context mContext = MainActivity.getContext();
    public JSONObject JSONResponse;

    //reference: http://stackoverflow.com/questions/32240177/working-post-multipart-request-with-volley-and-without-httpentity
    final String twoHyphens = "--";
    final String lineEnd = "\r\n";
    final String boundary = "apiclient-" + System.currentTimeMillis();
    final String mimeType = "multipart/form-data;boundary=" + boundary;


    public HttpPost(){
        JSONResponse = null;
        mContext = MainActivity.getContext();
    }

    public HttpPost(Context c){
        JSONResponse = null;
        mContext = c;
    }

    public void PostJSONResponseJSON(String Url, JSONObject JSONToPost) {
        final String tag = "POSTJSON";
        //final Object lock = new Object();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Url, JSONToPost, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(tag, response.toString());
                        JSONResponse = response;
                        resume();
                        onHttpResponse(JSONResponse);

                        //lock.notifyAll();
                        //Log.d(tag, "notifyAll");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(tag, "Error: " + error.getMessage());
                    }
                });
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);
        loading();

        //super.onPause();
        /*while(JSONResponse==null)
            synchronized (lock) {
                try {
                    Log.d(tag, "wait");
                    lock.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }*/
        //return JSONResponse;
    }

    public void onHttpResponse(JSONObject JSONResponse){
        //for overriding
    }

    public void onHttpResponse() {
        //for orverriding
    }

    public void loading(){
        progress = ProgressDialog.show(mContext, "Please wait", "loading...", true);
    }

    public void loading(String s){
        progress = ProgressDialog.show(mContext, "Please wait", "loading" + s + "...", true);
    }

    public void resume(){
        progress.dismiss();
    }


    //reference: http://stackoverflow.com/questions/32240177/working-post-multipart-request-with-volley-and-without-httpentity
    public void UploadFile(String Url, byte[] fileData, String fileName) {

        final String tag = "Upload File";
        byte[] multipartBody = new byte[0];

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            // the first file
            buildPart(dos, fileData, fileName);
            // send multipart form data necesssary after file data
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // pass to multipart body
            multipartBody = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //this is a POST method
        MultipartRequest multipartRequest = new MultipartRequest(Url, null, mimeType, multipartBody,
                new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.d(tag, response.toString());
                resume();
                onHttpResponse();  //for overiding

            }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(tag, "Error: " + error.getMessage());
            }
        });

        //VolleySingleton.getInstance(mContext).addToRequestQueue(multipartRequest);
        Volley.newRequestQueue(mContext).add(multipartRequest);
        loading();

    }

    public void UploadDrawable(String Url, int id, String imageName){
        UploadFile(Url, getFileDataFromDrawable(mContext, id), imageName);
    }

    //reference: http://stackoverflow.com/questions/32240177/working-post-multipart-request-with-volley-and-without-httpentity
    private void buildPart(DataOutputStream dataOutputStream, byte[] fileData, String fileName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""
                + fileName + "\"" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

    private byte[] getFileDataFromDrawable(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}


//reference: http://stackoverflow.com/questions/32240177/working-post-multipart-request-with-volley-and-without-httpentity
class MultipartRequest extends Request<NetworkResponse> {
    private final Response.Listener<NetworkResponse> mListener;
    private final Response.ErrorListener mErrorListener;
    private final Map<String, String> mHeaders;
    private final String mMimeType;
    private final byte[] mMultipartBody;

    public MultipartRequest(String url, Map<String, String> headers, String mimeType, byte[] multipartBody, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = headers;
        this.mMimeType = mimeType;
        this.mMultipartBody = multipartBody;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return mMimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mMultipartBody;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(
                    response,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }
}


/*HttpPost require an intentservice as linking to network is not permitted in main thread
Post and get synchonzied json request via FutureRequest class in volley
 */
//reference http://sohailaziz05.blogspot.hk/2012/05/intentservice-providing-data-back-to.html

/*
public class HttpPost implements HttpPostResultReceiver.Receiver {

    int REQUEST_TIMEOUT = 10;
    final Object lock = new Object();
    public static Context mContext = MainActivity.getContext();
    final String tag = "HttpPost";

    public JSONObject JSONResponse;
    public HttpPostResultReceiver mReceiver;

    public HttpPost() {
        mReceiver = new HttpPostResultReceiver(new Handler());

        mReceiver.setReceiver(this);

    }


    public JSONObject PostJSONResponseJSON(String Url, JSONObject JsonToPost) {

        //start a new HttpPostIntentService

        Intent intent = new Intent();
        intent.setClass(mContext,
                HttpPostIntentService.class);
        intent.putExtra(HttpPostIntentService.Url, Url);
        intent.putExtra(HttpPostIntentService.JsonToPostToString, JsonToPost.toString());
        intent.putExtra("receiverTag", mReceiver);

        mContext.startService(intent);

        //TODO: wait until ReceiveResult, try to use future class
        synchronized (lock) {
            try {
                Log.d(tag, "wait");
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //wait();
        Log.d(tag,JSONResponse.toString());
        return JSONResponse;
    }

    //TODO: wake up when ReceiveResult, try to use future class
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        // TODO Auto-generated method stub

        if(resultData!=null) {

            JSONResponse = resultDataToJsonObject(resultData);
        }
        //notify();
        lock.notifyAll();
        Log.d(tag, "notifyAll");
    }

    public JSONObject resultDataToJsonObject(Bundle resultData) {
        JSONObject json = new JSONObject();
        Set<String> keys = resultData.keySet();
        for (String key : keys) {
            try {
                // json.put(key, bundle.get(key)); see edit below
                json.put(key, JSONObject.wrap(resultData.get(key)));
            } catch(JSONException e) {
                //Handle exception here
            }
        }
        return json;
    }

}



class HttpPostIntentService extends IntentService {

    public static String Url, JsonToPostToString;
    public static Context mContext = MainActivity.getContext();


    public HttpPostIntentService() {
        super("HttpPostIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.d(TAG, "onCreate()");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("inside intentservice","");

        String u = intent.getStringExtra(Url);
        JSONObject jsPost = null;

        try {
            jsPost = new JSONObject(intent.getStringExtra("Json Object"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsRep =  PostJSONResponseJSON(u, jsPost);

        ResultReceiver rec = intent.getParcelableExtra("receiverTag");

        Bundle b= new Bundle();
        b.putString("jsRep",jsRep.toString());
        rec.send(0, b);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.d(TAG, "onDestroy()");
    }

    public JSONObject PostJSONResponseJSON(String Url, JSONObject jsPost) {
        int REQUEST_TIMEOUT = 10;

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Url, jsPost, future, future);
        Volley.newRequestQueue(mContext).add(request);

        try {
            Log.d("future request","waiting");
            JSONObject js = future.get(REQUEST_TIMEOUT, TimeUnit.SECONDS); // this will block (forever)
            return js;
        } catch (InterruptedException e) {
            // exception handling
        } catch (ExecutionException e) {
            // exception handling
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class HttpPostResultReceiver extends ResultReceiver {

    private Receiver mReceiver;

    public HttpPostResultReceiver(Handler handler) {
        super(handler);
        // TODO Auto-generated constructor stub
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);

    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }

};

*/


//only use this tutorialhttp://www.truiton.com/2015/02/android-volley-making-synchronous-request/

/*
public class HttpPost extends Activity{

    public JSONObject jsRep;
    public static Context mContext = MainActivity.getContext();

    public HttpPost(){
        jsRep = null;
        mContext = MainActivity.getContext();
    }

    public HttpPost(Context c) {
        jsRep = null;
        mContext = c;
    }

    public JSONObject PostJSONResponseJSON(String Url, JSONObject JSONToPost) {

        String TAG = "PostJSONRepJSON";
        final Object lock = new Object();

        startParsingTask(Url, JSONToPost);

        Log.d(TAG, "returning");

        synchronized (lock) {
            while(jsRep==null)
                try {
                    lock.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }

            Log.d(TAG, jsRep.toString());
        return jsRep;
    }

    public void startParsingTask(final String Url, final JSONObject JSONToPost) {
        Thread threadA = new Thread() {
            public void run() {
                ThreadB threadB = new ThreadB(Url,JSONToPost,mContext);
                JSONObject jsonObject = null;
                try {
                    jsonObject = threadB.execute().get(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                final JSONObject receivedJSONObject = jsonObject;
                jsRep = receivedJSONObject;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        jsRep = receivedJSONObject;

                        /*
                        mTextView.setText("Response is: " + receivedJSONObject);
                        if (receivedJSONObject != null) {
                            try {
                                mTextView.setText(mTextView.getText() + "\n\n" +
                                        receivedJSONObject.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        *//*
                    }
                });
            }
        };
        threadA.start();
    }

    private class ThreadB extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        public String Url;
        public JSONObject jsPost;

        public ThreadB(String s,JSONObject jsObj, Context ctx) {
            mContext = ctx;
            Url = s;
            jsPost = jsObj;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            final RequestFuture<JSONObject> futureRequest = RequestFuture.newFuture();
            final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method
                    .POST,Url ,jsPost, futureRequest, futureRequest);
            //jsonRequest.setTag(REQUEST_TAG);  //useless
            Volley.newRequestQueue(mContext).add(jsonRequest);
            try {
                Log.d("future request","waiting");
                return futureRequest.get(10, TimeUnit.SECONDS);
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
}
*/


//Walker hv rebuilt this class on 4/2/2016 in order to eable this class to sent synchorzied request and dont use below code
/*
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

    public HttpPost(Context context) {
        mContext = context;
        jsRep = null;
        mInstance = this;
    }

    /*public void PostJSONResponseJSON(String Url, JSONObject JSONToPost){
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
    }*/

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



    public JSONObject PostJSONResponseJSON(String Url, JSONObject JSONToPost){
        int REQUEST_TIMEOUT = 10;

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Url, JSONToPost, future, future);
        Volley.newRequestQueue(mContext).add(request);

        try {
            JSONObject js =  future.get(REQUEST_TIMEOUT, TimeUnit.SECONDS); // this will block (forever)
            return js;
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
*/
