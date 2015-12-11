package com.indielink.indielink.Profile;

import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Hong on 11/12/2015.
 */
public class InstrumentList {

    private ArrayList<String> AddList;
    private ArrayList<String> DefaultList;
    private int numOfInstrument;

    public ArrayList<String> getDefaultList(){
        return new ArrayList<String>(Arrays.asList(
                "vocal",
                "guitar",
                "bass",
                "drum",
                "keyboard",
                "other"
        ));
    }

    public int getNumOfInstrument(){
        return getDefaultList().size();
    }

    public ArrayList<String> getAddList(){
        return AddList;
    }

    public ArrayList<String> getNewAddList() {
        return new ArrayList<String>();
    }

    public ArrayList<String> getAddList(ArrayList<?> List, View view){

        ArrayList<String> temp = new ArrayList<String>();

        if(List.contains(int.class)){

            ArrayList<View> temp2 = new ArrayList<View>();
            for( Object index : List) temp2.add( view.findViewById( (int) index));
            temp = getAddList(temp2,view);

        }else if(List.contains(Switch.class)){

            for(int i=0;i<getNumOfInstrument();i++)
                if( ( (Switch) List.get(i)).isChecked())
                    temp.add(DefaultList.get(i));

        }else if(List.contains(CheckBox.class)){

            for(int i=0;i<getNumOfInstrument();i++)
                if( ( (CheckBox) List.get(i)).isChecked())
                    temp.add(DefaultList.get(i));

        }
        return temp;
    }

    public InstrumentList(ArrayList<?> List,View view){
        getDefaultList();
        getAddList(List, view);
    }

    public InstrumentList(){
        getDefaultList();
        getNewAddList();
    }

}
