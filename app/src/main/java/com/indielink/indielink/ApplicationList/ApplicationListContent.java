package com.indielink.indielink.ApplicationList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ApplicationListContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<ApplicationItem> ITEMS = new ArrayList<ApplicationItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, ApplicationItem> ITEM_MAP = new HashMap<String, ApplicationItem>();

    static {
        // Add 3 sample items.
        addItem(new ApplicationItem("1", "Item 1"));
        addItem(new ApplicationItem("2", "Item 2"));
        addItem(new ApplicationItem("3", "Item 3"));
    }

    private static void addItem(ApplicationItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ApplicationItem {
        public String id;
        public String content;

        public ApplicationItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
