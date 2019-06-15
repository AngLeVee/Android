package com.android.example.popularmovies;

public class Trailer {
    private final String mName;
    private final String mPath;

    public Trailer(String name, String path){
        mName = name;
        mPath = path;
    }

    public String getName (){ return mName; }
    public String getPath (){ return mPath; }
}
