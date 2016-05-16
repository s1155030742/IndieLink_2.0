package com.indielink.indielink.RecommendSound;


public class RecommendTrackItem {
    public final Integer id;
    public final String name;

    public RecommendTrackItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
