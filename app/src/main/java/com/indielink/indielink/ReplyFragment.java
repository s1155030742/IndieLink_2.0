package com.indielink.indielink;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.indielink.indielink.ApplicationList.ApplicationListContent;
import com.indielink.indielink.Network.HttpPost;
import com.indielink.indielink.Profile.BandProfileContent;
import com.indielink.indielink.Profile.InstrumentList;
import com.indielink.indielink.Profile.TrackScore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ReplyFragment extends DialogFragment {

    private OnFragmentInteractionListener mListener;
    private ApplicationListContent.ApplicationItem Candidate;

    String tag = "ReplyFragment";
    String Url = "http://137.189.97.88:8080/user/reply";

    JSONObject JSONToPost, JSONRep;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Candidate = (ApplicationListContent.ApplicationItem) this.getArguments()
                .getSerializable("candidate");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreateDialog(savedInstanceState);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_reply_dialog, null);
        builder.setView(view).setTitle(Candidate.toString()).setMessage(Candidate.about_me)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        /*
                        JSONObject obj = new JSONObject();
                        try
                        {
                            obj.put("access_token", AccessToken.getCurrentAccessToken().getToken());
                            obj.put("fb_user_id", AccessToken.getCurrentAccessToken().getUserId());
                            obj.put("recruit_id", Candidate.id.toString());
                            obj.put("reply",true);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        //Post to server
                        HttpPost httpPost = new HttpPost();
                        JSONObject response = httpPost.PostJSONResponseJSON("http://137.189.97.88:8080/user/reply", obj);
                        try {
                            response.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        */

                        JSONToPost = new JSONObject() {
                            {
                                try {
                                    put("access_token", AccessToken.getCurrentAccessToken().getToken());
                                    put("fb_user_id", AccessToken.getCurrentAccessToken().getUserId());
                                    put("recruit_id", Candidate.id.toString());
                                    put("reply",true);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.POST, Url, JSONToPost, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d(tag, response.toString());
                                        JSONRep = response;
                                        onCreateViewFromJSON(view);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.d(tag, "Error: " + error.getMessage());
                                    }
                                });
                        Volley.newRequestQueue(view.getContext()).add(jsonObjectRequest);

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("access_token", AccessToken.getCurrentAccessToken().getToken());
                            obj.put("fb_user_id", AccessToken.getCurrentAccessToken().getUserId());
                            obj.put("recruit_id", "");
                            obj.put("reply", false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Post to server
                        HttpPost httpPost = new HttpPost();
                        JSONObject response = httpPost.PostJSONResponseJSON("http://137.189.97.88:8080/user/reply", obj);
                        try {
                            response.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        */
                        JSONToPost = new JSONObject() {
                            {
                                try {
                                    put("access_token", AccessToken.getCurrentAccessToken().getToken());
                                    put("fb_user_id", AccessToken.getCurrentAccessToken().getUserId());
                                    put("recruit_id", "");
                                    put("reply", false);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.POST, Url, JSONToPost, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d(tag, response.toString());
                                        JSONRep = response;
                                        onCreateViewFromJSON(view);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.d(tag, "Error: " + error.getMessage());
                                    }
                                });
                        Volley.newRequestQueue(view.getContext()).add(jsonObjectRequest);
                    }
                });
        return builder.create();
    }

    public void onCreateViewFromJSON(View view){
        try {
            JSONRep.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ReplyFragment newInstance(String param1, String param2) {
        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ReplyFragment() {
        // Required empty public constructor
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
