package com.indielink.indielink.ApplicationList;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationListContent{

    public static ArrayList<ApplicationItem> RecruitList = new ArrayList<ApplicationItem>();

    private static void addItem(ApplicationItem item) {
        RecruitList.add(item);
    }

    public static void addItem(JSONObject item) {
        RecruitList.add(new ApplicationItem(item));
    }

    public static void removeItem(int id) {
        RecruitList.remove(id);
    }
    /**
     * A dummy item representing a piece of content.
     */
    public static class ApplicationItem implements Serializable {
        public String id;
        public String name;
        public String instrument;
        public String about_me;

        private ApplicationItem(String id, String name,String instrument,String about_me) {
            this.id = id;
            this.name = name;
            this.instrument = instrument;
            this.about_me = about_me;
        }

        public ApplicationItem(JSONObject item) {
            try
            {
                this.id = item.getString("recruit_id");
                this.name = item.getString("name");
                this.instrument = item.getString("instrument");
                this.about_me =item.getString("about_me");
            }catch (Exception x){}
        }

        @Override
        public String toString() {
            return (name+" - "+instrument);
        }
    }
}
