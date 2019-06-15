package com.android.example.popularmovies.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.android.example.popularmovies.Request;
import com.android.example.popularmovies.database.AppDatabase;
import com.android.example.popularmovies.database.Movie;
import com.android.example.popularmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

import static com.android.example.popularmovies.MainActivity.RESULTS;

public class DetailViewModel extends AndroidViewModel {

    //region Private Variables
    private final AppDatabase mMovieDB;
    private MutableLiveData<JSONArray> mTrailers;
    private MutableLiveData<JSONArray> mReviews;
    private MutableLiveData<Boolean> mFavorite;
    private String mMovieID;
    //endregion

    //region Public Functions
    public DetailViewModel(@NonNull Application application) {
        super(application);
        mMovieDB = AppDatabase.getInstance(application.getApplicationContext());
    }

    public LiveData<JSONArray> getTrailers(String id) {
        mMovieID = id;
        if (mTrailers == null || mTrailers.getValue() == null)
            loadTrailers();
        return mTrailers;
    }

    public LiveData<JSONArray> getReviews(String id) {
        mMovieID = id;
        if (mTrailers == null || mTrailers.getValue() == null)
            loadReviews();
        return mReviews;
    }

    public LiveData<Boolean> getFavorite(String id) {
        mMovieID = id;
        if (mFavorite == null || mFavorite.getValue() == null)
            loadFavorite();
        return mFavorite;
    }

    public void setFavorite(Movie movie) {
        insertFavorite(movie);
    }

    public void removeFavorite(Movie movie) {
        deleteFavorite(movie);
    }
    //endregion

    //region Async Functions
    private void loadTrailers() {
        mTrailers = new MutableLiveData<>();

        new AsyncTask<Void, Void, JSONArray>() {
            @Override
            protected JSONArray doInBackground(Void... voids) {

                //If connection fails, return null
                if (checkInternetFail())
                    return null;

                URL requestUrl = NetworkUtils.buildURL(Request.TRAILERS, mMovieID);
                if (requestUrl != null)
                    try {
                        String jsonResponse = NetworkUtils
                                .getResponseFromHttpUrl(requestUrl);
                        JSONObject object = new JSONObject(jsonResponse);
                        return object.optJSONArray(RESULTS);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                else
                    return null;
            }

            @Override
            protected void onPostExecute(JSONArray data) {
                mTrailers.setValue(data);
            }
        }.execute();

    }

    private void loadReviews() {
        mReviews = new MutableLiveData<>();

        new AsyncTask<Void, Void, JSONArray>() {
            @Override
            protected JSONArray doInBackground(Void... voids) {

                //If connection fails, return null
                if (checkInternetFail())
                    return null;

                URL requestUrl = NetworkUtils.buildURL(Request.REVIEWS, mMovieID);
                if (requestUrl != null)
                    try {
                        String jsonRequestResponse = NetworkUtils
                                .getResponseFromHttpUrl(requestUrl);
                        JSONObject object = new JSONObject(jsonRequestResponse);
                        return object.optJSONArray(RESULTS);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                else
                    return null;
            }

            @Override
            protected void onPostExecute(JSONArray data) {
                mReviews.setValue(data);
            }
        }.execute();
    }

    private void loadFavorite() {
        mFavorite = new MutableLiveData<>();

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return mMovieDB.movieDao().loadMovie(mMovieID) != null;
            }

            @Override
            protected void onPostExecute(Boolean favorite) {
                mFavorite.setValue(favorite);
            }
        }.execute();
    }

    private void insertFavorite(Movie movie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mMovieDB.movieDao().insertMovie(movie);
                return null;
            }
        }.execute();
    }

    private void deleteFavorite(Movie movie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mMovieDB.movieDao().deleteMovie(movie);
                return null;
            }
        }.execute();
    }
    //endregion

    /**
     * Check if the internet connection succeeds or fails
     *
     * @return boolean where true means the connection failed
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
