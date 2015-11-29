package com.indielink.indielink;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SearchFragment extends Fragment {

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;
   // private ArrayList<Integer> array_image;

    @InjectView(R.id.frame) SwipeFlingAdapterView flingContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

       //arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item, R.id.helloText, al);

        ButterKnife.inject(this,view);

       // ArrayList<Integer> array_image = new ArrayList<Integer>();


        al = new ArrayList<>();
        al.add("Muse");
        al.add("Beatles");
        al.add("Oasis");
        al.add("Blur");
        al.add("Guns N' roses");
        al.add("Libertines");

        arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item, R.id.BandTextView, al);

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
                al.add("Band ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
               //makeToast(SearchFragment.this, "Clicked!");

                //FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                //fragmentTransaction.add(R.id.carddetail,CardDetailFragment.newInstance());


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack("CardDetail")
                        .replace(R.id.frame_container, new CardDetailFragment()).commit();

                Log.d("LIST", "clicked");
            }
        });
        return view;
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT);
    }


    @OnClick(R.id.right)
    public void right() {
        /**
         * Trigger the right event manually.
         */
        flingContainer.getTopCardListener().selectRight();
    }

    @OnClick(R.id.left)
    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }
}

