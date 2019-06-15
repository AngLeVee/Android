package com.android.example.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import com.android.example.popularmovies.BuildConfig;
import com.android.example.popularmovies.Request;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String MOVIE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY = BuildConfig.API_KEY;

    //region Parameters
    private final static String POPULAR_PARAM = "popular";
    private final static String TOP_RATED_PARAM = "top_rated";
    private final static String API_PARAM = "api_key";
    private final static String PAGE_PARAM = "page";
    private final static String TRAILER_PARAM = "videos";
    private final static String REVIEW_PARAM = "reviews";
    //endregion

    /**
     * Builds the URL used to request a list of movies.
     *
     * @param sortPopular true = sort by popularity; false = sort by top rated
     * @param page the page to query
     * @return URL for query
     */
    public static URL buildURL (boolean sortPopular, int page){
        //region Build URI
        String sortParam;
        if (sortPopular)
            sortParam = POPULAR_PARAM;
        else
            sortParam = TOP_RATED_PARAM;

        if (page == 0) page = 1;

        Uri builtURI = Uri.parse(MOVIE_URL).buildUpon()
                .appendEncodedPath(sortParam)
                .appendQueryParameter(API_PARAM, API_KEY)
                .appendQueryParameter(PAGE_PARAM, String.valueOf(page))
                .build();
        //endregion

        //region Build URL
        URL url = null;
        try {
            url = new URL(builtURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //endregion

        Log.v(TAG, "Built URL " + url);

        return url;
    }

    /**
     * Builds the URL used to either request the trailers or the reviews
     *
     * @param request Request variable for query
     * @param id Movie id to use in query
     * @return URL for query
     */
    public static URL buildURL (Request request, String id){
        if (id.isEmpty()) return null;

        //region Build URI
        String requestParam;
        if (request == Request.TRAILERS)
            requestParam = TRAILER_PARAM;
        else
            requestParam = REVIEW_PARAM;

        Uri builtURI = Uri.parse(MOVIE_URL).buildUpon()
                .appendEncodedPath(id)
                .appendEncodedPath(requestParam)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();
        //endregion

        //region Build URL
        URL url = null;
        try {
            url = new URL(builtURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //endregion

        Log.v(TAG, "Built URL " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
