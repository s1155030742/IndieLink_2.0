package com.indielink.indielink;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.indielink.indielink.CustomAdapter.CustomAdapter;
import com.indielink.indielink.CustomAdapter.RowItem;
import com.indielink.indielink.Network.HttpPost;
import com.indielink.indielink.Profile.BandProfileContent;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchFragment extends Fragment {

    private ArrayList<RowItem> al = new ArrayList<RowItem>();
    private CustomAdapter arrayAdapter;
    public ArrayList<BandProfileContent> SuggestedBands = new ArrayList<BandProfileContent>();

    String tag = "SearchFragment";
    String Url = "http://137.189.97.88:8080/band/detail";

    JSONObject JSONToPost, JSONRep;
    // private ArrayList<Integer> array_image;

    @InjectView(R.id.frame) SwipeFlingAdapterView flingContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        /*if(fragmentManager.findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment homeFragment = new Home();
            fragmentTransaction.replace(R.id.mainFragement, homeFragment, tag);
            fragmentTransaction.commit();
        }*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        (new HttpPost(view.getContext()) {
            @Override
            public void onHttpResponse(JSONObject jsRep) {
                //action onResponse and pass data from response to activity
                JSONRep = jsRep;
                onCreateViewFromJSON(view);
            }
        }).PostJSONResponseJSON(Url,
                new JSONObject() {{try {
                        //things to put in json
                        put("access_token", AccessToken.getCurrentAccessToken().getToken());
                        put("fb_user_id", AccessToken.getCurrentAccessToken().getUserId());
                        put("user_id", RootPage.user_id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }}
        );
        return view;
    }

        //Post to server
        //if(UserRole.GetUserRole() == "") {
        //          band/detail/
        //response = httpPost.PostJSONResponseJSON("http://137.189.97.88:8080/band/detail", obj);

    public void onCreateViewFromJSON(View view)
    {
            try{
                JSONArray Bands = JSONRep.getJSONArray("band");
                for (int i=0; i<Bands.length(); i++)
                {
                    JSONObject Band = Bands.getJSONObject(i);
                    BandProfileContent b = new BandProfileContent(Band);
                    SuggestedBands.add(b);
                    al.add(new RowItem(b.BandName,b.BandPictureURL));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        //}
        /*else
        {
            response = httpPost.PostJSONResponseJSON("http://137.189.97.88:8080/user/detail", obj);
            try{
                JSONArray Users = response.getJSONArray("user");
                for (int i=0; i<Users.length(); i++)
                {

                    al.add(new RowItem(Users.getJSONObject(i).getString("name"),PhotoUrl+Users.getJSONObject(i).getString("band_id")+".jpg"));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }*/

        ButterKnife.inject(this, view);
        // ArrayList<Integer> array_image = new ArrayList<Integer>();
        ArrayList<String> dummy = new ArrayList<String>();

        arrayAdapter = new CustomAdapter(getActivity(), al);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                //makeToast(SearchFragment.this, "Left!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                //makeToast(SearchFragment.this, "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                /**al.add("Band ".concat(String.valueOf(i)));
                 arrayAdapter.notifyDataSetChanged();
                 Log.d("LIST", "notified");
                 i++;**/
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                //dummy
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                String SelectedName = ((RowItem) dataObject).getText();
                //if (UserRole.GetUserRole() == "") {
                for(BandProfileContent item : SuggestedBands)
                {
                    Log.v(item.BandName,SelectedName);
                    if (item.BandName==SelectedName)
                    {
                        BandProfileContent SelectedBand = item;
                        Bundle bundle = new Bundle();
                        Fragment fragment = new CardBandDetailFragment();
                        bundle.putSerializable("SelectedBand",SelectedBand);
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().addToBackStack("CardBandDetail")
                                .replace(R.id.frame_container, fragment).commit();
                    }
                }
                // TODO: SEM2 sin add the below part
                /*}else{
                    fragmentManager.beginTransaction().addToBackStack("CardDetail")
                            .replace(R.id.frame_container, new CardDetailFragment()).commit();
                }*/
                //Log.d("Swipe card", "clicked");
            }
        });

        //return view;
        flingContainer.requestLayout();
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT);
    }
}

