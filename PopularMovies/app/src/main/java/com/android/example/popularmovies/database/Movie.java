package com.android.example.popularmovies.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Movie {
    @Ignore
    private final String[] months = new String[]{"January", "February", "March", "April", "May",
    "June", "July", "August", "September", "October", "November", "December"};

    //region Database Variables
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final String mId;

    @ColumnInfo(name = "title")
    private final String mTitle;
    //endregion

    //region Non-Database Variables
    @Ignore private String mOrigTitle;
    @Ignore private String mRelease;
    @Ignore private String mRating;
    @Ignore private String mVotes;
    @Ignore private String mSynopsis;
    @Ignore private String mPoster;
    //endregion

    public Movie (@NonNull String id, String title){
        mId = id;
        mTitle = title;
    }

    //region Setters
    public void setOrigTitle (String origTitle){ mOrigTitle = origTitle; }
    public void setRelease (String release){
        String[] date = release.split("-");
        //reformat to Month D, YYYY from year-month-day
        mRelease = months[Integer.parseInt(date[1])-1] + " " + date[2] + ", " + date[0];
    }
    public void setRating (String rating){ mRating = rating; }
    public void setVotes (String votes){ mVotes = votes; }
    public void setSynopsis (String synopsis){ mSynopsis = synopsis; }
    public void setPoster (String poster){ mPoster = poster; }
    //endregion

    //region Getters
    public String getId (){return mId; }
    public String getTitle (){ return mTitle; }
    public String getOrigTitle (){ return mOrigTitle; }
    public String getRelease (){ return mRelease; }
    public String getRating (){ return mRating; }
    public String getVotes (){ return mVotes; }
    public String getSynopsis (){ return mSynopsis; }
    public String getPoster (){ return mPoster; }
    //endregion
}
