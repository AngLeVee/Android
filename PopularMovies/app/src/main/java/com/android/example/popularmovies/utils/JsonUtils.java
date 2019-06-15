package com.android.example.popularmovies.utils;

import com.android.example.popularmovies.database.Movie;
import com.android.example.popularmovies.Review;
import com.android.example.popularmovies.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    //region Keys
    private static final String ID_KEY = "id";
    private static final String TITLE_KEY = "title";
    private static final String ORIGINAL_TITLE_KEY = "original_title";
    private static final String RELEASE_KEY = "release_date";
    private static final String RATING_KEY = "vote_average";
    private static final String VOTES_KEY = "vote_count";
    private static final String SYNOPSIS_KEY = "overview";
    private static final String POSTER_KEY = "poster_path";
    private static final String NAME_KEY = "name";
    private static final String PATH_KEY = "key";
    private static final String AUTHOR_KEY = "author";
    private static final String CONTENT_KEY = "content";
    //endregion

    //region MainActivity
    public static Movie parseMovieJson (String json) {
        try{
            Movie movie;
            JSONObject data = new JSONObject(json);
            String id = data.optString(ID_KEY);
            String title = data.optString(TITLE_KEY);
            movie = new Movie(id, title);
            movie.setOrigTitle(data.optString(ORIGINAL_TITLE_KEY));
            movie.setRelease(data.optString(RELEASE_KEY));
            movie.setRating(data.optString(RATING_KEY));
            movie.setVotes(data.optString(VOTES_KEY));
            movie.setSynopsis(data.optString(SYNOPSIS_KEY));
            movie.setPoster(data.optString(POSTER_KEY));
            return movie;
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getMoviePosters(JSONArray array){
        String[] posters = new String[array.length()];
        if (posters.length > 0){
            for (int i = 0; i < array.length(); i++){
                JSONObject movie = array.optJSONObject(i);
                posters[i] = movie.optString(POSTER_KEY);
            }
            return posters;
        }
        else
            return null;
    }
    //endregion

    //region DetailActivity
    public static List<Object> getTrailers(JSONArray array){
        List<Object> trailers = new ArrayList<>();
        if (array.length() > 0){
            for (int i = 0; i < array.length(); i++){
                JSONObject trailer = array.optJSONObject(i);
                trailers.add(new Trailer(trailer.optString(NAME_KEY), trailer.optString(PATH_KEY)));
            }
            return trailers;
        }
        else
            return null;
    }

    public static List<Object> getReviews(JSONArray array){
        List<Object> reviews = new ArrayList<>();
        if (array.length() > 0){
            for (int i = 0; i < array.length(); i++){
                JSONObject review = array.optJSONObject(i);
                reviews.add(new Review(review.optString(AUTHOR_KEY), review.optString(CONTENT_KEY)));
            }
            return reviews;
        }
        else
            return null;

    }
    //endregion

}
