package com.android.example.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.example.popularmovies.adapters.MovieAdapter;
import com.android.example.popularmovies.viewmodels.MainViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler {

    //region Static Variables
    public static String POSTER_WIDTH;
    public final static String POSTER_BASE_PATH = "http://image.tmdb.org/t/p/";
    public final static String MOVIE_EXTRA = "movie";
    public final static String RESULTS = "results";
    private final static String STATE_KEY = "scrollPos";
    private final static String PREF_SORT = "sort_popular";
    private final static String PREF_FAVE = "show_favorites";
    //endregion

    //region Views
    @BindView(R.id.movies_rv) RecyclerView mMovieRV;
    @BindView(R.id.error_ll) LinearLayout mErrorLL;
    @BindView(R.id.retry_butt) Button mRetryButt;
    @BindView(R.id.movie_pb) ProgressBar mMoviePB;
    //endregion

    //startActivityForResult wouldn't return correct value
    static boolean favesChanged = false;

    //region Private Variables
    private SharedPreferences.Editor mPrefsEditor;
    private MenuItem mPopular;
    private MenuItem mTopRated;
    private MenuItem mFavorites;
    private MainViewModel mMainVM;
    private MovieAdapter mMovieAdapter;
    private boolean mSortPopular;
    private boolean mShowFavorites;
    private int mScroll;
    private int mCurrentPos = -1;
    //endregion

    //region Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region Initialize
        ButterKnife.bind(this);
        SharedPreferences moviePrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mMainVM = ViewModelProviders.of(this).get(MainViewModel.class);
        mPrefsEditor = moviePrefs.edit();
        mSortPopular = moviePrefs.getBoolean(PREF_SORT, true);
        mShowFavorites = moviePrefs.getBoolean(PREF_FAVE, false);
        //endregion

        //region Calculate Sizing
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        //Using dp width instead of pixel width returns strange results for number of posters per row on some devices
        int width = displayMetrics.widthPixels;
        int pWidth;
        int iWidth = width / 3;
        if (iWidth < 342) {
            pWidth = 185;
            POSTER_WIDTH = "w185";
        } else if (iWidth < 500) {
            pWidth = 342;
            POSTER_WIDTH = "w342";
        } else {
            pWidth = 500;
            POSTER_WIDTH = "w500";
        }
        int remainder = width % pWidth;
        int spanCount = width / pWidth;
        int padding = remainder/(spanCount + 1);
        //endregion

        //region RecyclerView setup
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mMovieAdapter = new MovieAdapter(this, padding);
        mMovieRV.setLayoutManager(layoutManager);
        mMovieRV.setPadding(padding, padding/2, 0, 0);
        mMovieRV.setHasFixedSize(true);
        mMovieRV.setAdapter(mMovieAdapter);
        //endregion

        mRetryButt.setOnClickListener(view -> loadAllData());
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressBar();
        loadAllData();
        mMovieRV.getLayoutManager().scrollToPosition(mScroll);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int scroll = 0;
        RecyclerView.LayoutManager layoutManager = mMovieRV.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            scroll = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            if (scroll < 1)
                scroll = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        outState.putInt(STATE_KEY, scroll);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mScroll = savedInstanceState.getInt(STATE_KEY);
        mMovieRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = mMovieRV.getLayoutManager();
                int pos = 0;
                if (layoutManager instanceof GridLayoutManager) {
                    pos = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                    if (pos < 1)
                        pos = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                }
                if (pos < mScroll && pos != mCurrentPos) {
                    mCurrentPos = pos;
                    layoutManager.scrollToPosition(mScroll);
                } else {
                    layoutManager.scrollToPosition(mScroll);
                    recyclerView.removeOnScrollListener(this);
                }
            }
        });
    }
    //endregion

    //region Menu Functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        //Get references to menu items
        mPopular = menu.findItem(R.id.sort_popular);
        mTopRated = menu.findItem(R.id.sort_top_rated);
        mFavorites = menu.findItem(R.id.show_favorites);
        updateMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //region Set Booleans
        if (id == R.id.sort_popular) {
            mSortPopular = true;
            mShowFavorites = false;
        } else if (id == R.id.sort_top_rated) {
            mSortPopular = false;
            mShowFavorites = false;
        } else if (id == R.id.show_favorites) {
            //Allows toggling
            mShowFavorites = !mFavorites.isChecked();
        } else
            return super.onOptionsItemSelected(item);
        updateMenu();
        mPrefsEditor.putBoolean(PREF_SORT, mSortPopular);
        mPrefsEditor.putBoolean(PREF_FAVE, mShowFavorites);
        mPrefsEditor.apply();
        //endregion

        if (mErrorLL.getVisibility() == View.VISIBLE) {
            showProgressBar();
            loadAllData();
        } else {
            showProgressBar();
            loadMovieData();
            mMovieRV.scrollToPosition(mScroll);
        }
        return true;
    }

    /**
     * Function to set and update menu items and saved preferences
     */
    private void updateMenu() {
        mFavorites.setChecked(mShowFavorites);
        if (mShowFavorites){
            mPopular.setChecked(false);
            mTopRated.setChecked(false);
        } else {
            mPopular.setChecked(mSortPopular);
            mTopRated.setChecked(!mSortPopular);
        }
    }
    //endregion

    //region Movie Data Functions
    /**
     * Function to trigger initial load of movie data
     */
    private void loadAllData() {
        if (mShowFavorites) {
            mMainVM.getPopular().observe(this, mPopMovies -> {
                    mMainVM.getTopRated().observe(this, mTopRated -> {
                        //Only load favorites if both queries succeeded
                        if (mPopMovies != null && mTopRated != null)
                            mMainVM.getFavorites(favesChanged).observe(this, mFaveMovies -> {
                                setData(mFaveMovies);
                                //New favorites fetched
                                favesChanged = false;
                            });
                        else
                            showErrorMessage();
                    });
            });
        } else if (mSortPopular){
            mMainVM.getPopular().observe(this, this::setData);
            mMainVM.getTopRated();
        } else {
            mMainVM.getTopRated().observe(this, this::setData);
            mMainVM.getPopular();
        }
    }

    /**
     * Function to trigger retrieval of movie data
     */
    private void loadMovieData() {
        if (mShowFavorites) {
            mMainVM.getFavorites(favesChanged).observe(this, mFaveMovies -> {
                setData(mFaveMovies);
                //New favorites fetched
                favesChanged = false;
            });
        }else {
            if (mSortPopular){
                mMainVM.getPopular().observe(this, this::setData);
            } else {
                mMainVM.getTopRated().observe(this, this::setData);
            }
        }
    }

    /**
     * Function to reduce duplication of code to set movie data into the adapter
     * @param data the JSONArray that contains the movie data
     */
    private void setData(JSONArray data) {
        if(data == null)
            showErrorMessage();
        else {
            showRecyclerView();
            mMovieAdapter.setMovieData(data);
            mMovieAdapter.notifyDataSetChanged();
        }
    }
    //endregion

    //region Show/Hide views
    /**
     * Show RecyclerView once data is loaded, hide error LinearLayout and ProgressBar
     */
    private void showRecyclerView() {
        mMovieRV.setVisibility(View.VISIBLE);
        mErrorLL.setVisibility(View.GONE);
        mMoviePB.setVisibility(View.GONE);
    }

    /**
     * Show ProgressBar when loading, hide RecyclerView and error LinearLayout
     */
    private void showProgressBar() {
        mMovieRV.setVisibility(View.GONE);
        mErrorLL.setVisibility(View.GONE);
        mMoviePB.setVisibility(View.VISIBLE);
    }

    /**
     * Show error LinearLayout when something fails to load, hide RecyclerView and ProgressBar
     */
    private void showErrorMessage() {
        mMovieRV.setVisibility(View.GONE);
        mErrorLL.setVisibility(View.VISIBLE);
        mMoviePB.setVisibility(View.GONE);
    }
    //endregion

    /**
     * MovieAdapter onClick implementation, takes user to the DetailActivity for the movie poster clicked
     * @param movie JSONObject that contains movie information
     */
    @Override
    public void onClick(JSONObject movie) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra(MOVIE_EXTRA, movie.toString());
        startActivity(detailIntent);
    }
}
