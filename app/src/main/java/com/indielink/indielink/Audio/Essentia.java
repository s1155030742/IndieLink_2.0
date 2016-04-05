package com.indielink.indielink.Audio;

/**
 * Created by indielink on 4/5/16.
 */

public class Essentia {
    private native void print();
    public static void main(String[] args) {
        new Essentia().print();
    }
    static {
        System.loadLibrary("essentia");
    }
}
