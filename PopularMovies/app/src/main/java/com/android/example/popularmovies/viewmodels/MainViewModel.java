package com.android.example.popularmovies.viewmodels;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.android.example.popularmovies.database.AppDatabase;
import com.android.example.popularmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.android.example.popularmovies.MainActivity.RESULTS;

public class MainViewModel extends AndroidViewModel {

    //region Private Variables
    private static final String ID_KEY = "id";
    private final static int MAX_PAGE = 3;
    private final AppDatabase mMovieDB;
    private MutableLiveData<JSONArray> mPopMovies;
    private MutableLiveData<JSONArray> mTopMovies;
    private MutableLiveData<JSONArray> mFaveMovies;
    //endregion

    //region Public Functions
    public MainViewModel(@NonNull Application application) {
        super(application);
        mMovieDB = AppDatabase.getInstance(application.getApplicationContext());
    }

    /**
     * Function used to fetch list of movies ordered by popularity
     *
     * @return a JSONArray of movie data
     */
    public LiveData<JSONArray> getPopular() {
        if (mPopMovies == null || mPopMovies.getValue() == null)
            new MovieAsyncTask(true).execute();
        return mPopMovies;
    }

    /**
     * Function used to fetch list of movies ordered by top rating
     *
     * @return a JSONArray of movie data
     */
    public LiveData<JSONArray> getTopRated() {
        if (mTopMovies == null || mTopMovies.getValue() == null)
            new MovieAsyncTask(false).execute();
        return mTopMovies;
    }

    /**
     * Load movie ids of favorite movies and fetch appropriate movies from both loaded lists
     *
     * @param changed Whether the list of favorites has been changed since load/last load
     * @return JSONArray containing all loaded movies that are in the database
     */
    public LiveData<JSONArray> getFavorites(boolean changed) {
        if (mFaveMovies == null || changed)
            loadFavorites();
        return mFaveMovies;
    }
    //endregion

    //region Async
    private void loadFavorites() {
        mFaveMovies = new MutableLiveData<>();

        new AsyncTask<Void, Void, JSONArray>() {
            @Override
            protected JSONArray doInBackground(Void... voids) {
                if (mPopMovies == null || mPopMovies.getValue() == null
                        || mTopMovies == null || mTopMovies.getValue()== null)
                    return null;
                List<String> movieIds = mMovieDB.movieDao().loadFavorites();
                //Prevent pulling duplicates if movie is in both lists
                List<String> added = new ArrayList<>();
                JSONArray array = new JSONArray();
                JSONArray movies = mPopMovies.getValue();
                for (int i = 0; i < movies.length(); i++) {
                    JSONObject object = movies.optJSONObject(i);
                    String id = object.optString(ID_KEY);
                    if (movieIds.contains(id) && !added.contains(id)) {
                        array.put(object);
                        added.add(id);
                    }
                }
                movies = mTopMovies.getValue();
                for (int i = 0; i < movies.length(); i++) {
                    JSONObject object = movies.optJSONObject(i);
                    String id = object.optString(ID_KEY);
                    if (movieIds.contains(id) && !added.contains(id)) {
                        array.put(object);
                        added.add(id);
                    }
                }
                return array;
            }

            @Override
            protected void onPostExecute(JSONArray data) {
                mFaveMovies.setValue(data);
            }
        }.execute();
    }

    private class MovieAsyncTask extends AsyncTask<Void, Void, JSONArray> {

        boolean mSortPop;

        MovieAsyncTask (boolean sortPop) {
            mSortPop = sortPop;
        }

        @Override
        protected void onPreExecute(){
            if (mSortPop)
                mPopMovies = new MutableLiveData<>();
            else
                mTopMovies = new MutableLiveData<>();

        }

        @Override
        protected JSONArray doInBackground(Void... voids) {

            //If connection fails, return null
            if (checkInternetFail())
                return null;

            JSONArray array;
            URL movieRequestUrl = NetworkUtils.buildURL(mSortPop, 1);
            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);
                JSONObject object = new JSONObject(jsonMovieResponse);

                array = object.optJSONArray(RESULTS);

                for (int i = 2; i < MAX_PAGE + 1; i++) {
                    movieRequestUrl = NetworkUtils.buildURL(mSortPop, i);
                    jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);
                    object = new JSONObject(jsonMovieResponse);
                    JSONArray tempArray = object.optJSONArray(RESULTS);

                    for (int j = 0; j < tempArray.length(); j++)
                        array.put(tempArray.opt(j));
                }
                return array;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(JSONArray data){
            if (mSortPop)
                mPopMovies.setValue(data);
            else
                mTopMovies.setValue(data);
        }
    }
    //endregion

    /**
     * Check if the internet connection succeeds or fails
     *
     * @return true means the connection failed
     */
    private boolean checkInternetFail() {
        try {
            int timeout = 1500;
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("8.8.8.8", 53), timeout);
            socket.close();
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
