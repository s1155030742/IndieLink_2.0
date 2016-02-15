package com.indielink.indielink.Profile;

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
public class SoundTrackContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<SoundTrackItem> ITEMS = new ArrayList<SoundTrackItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SoundTrackItem> ITEM_MAP = new HashMap<String, SoundTrackItem>();

    //private static final int COUNT = 25;

    /*
    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createSoundTrackItem(i));
        }
    }
    */

    private static void addItem(SoundTrackItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    //private static SoundTrackItem createSoundTrackItem(int position) {
    //    return new SoundTrackItem(String.valueOf(position), "Item " + position, makeDetails(position));
    //}

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class SoundTrackItem {
        public final String id;
        public final String name;

        public SoundTrackItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
