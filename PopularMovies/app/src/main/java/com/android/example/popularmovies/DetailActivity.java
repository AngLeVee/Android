package com.android.example.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.popularmovies.adapters.ExpandableAdapter;
import com.android.example.popularmovies.database.Movie;
import com.android.example.popularmovies.utils.JsonUtils;
import com.android.example.popularmovies.viewmodels.DetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    private static final String TRAILER_TITLE = "Trailers";
    private static final String REVIEW_TITLE = "Reviews";

    //region Views
    @BindView(R.id.poster_iv) ImageView mPosterIV;
    @BindView(R.id.title_tv) TextView mOrigTitleTV;
    @BindView(R.id.date_tv) TextView mReleaseDateTV;
    @BindView(R.id.rating_tv) TextView mUserRatingTV;
    @BindView(R.id.synopsis_tv) TextView mSynopsisTV;
    @BindView(R.id.favorite_cb) CheckBox mFavoriteCB;
    @BindView(R.id.detail_error_ll) LinearLayout mErrorLL;
    @BindView(R.id.detail_retry_butt) Button mRetryButt;
    @BindView(R.id.detail_pb) ProgressBar mDetailPB;
    @BindView(R.id.info_elv) ExpandableListView mInfoELV;
    //endregion

    //region Private Variables
    private Movie mMovie;
    private DetailViewModel mDetailVM;
    private ExpandableAdapter mExpandableAdapter;
    private List<String> mListTitles;
    private HashMap<String, List<Object>> mListMap;
    private boolean mTrailerError;
    private boolean mReviewError;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        mDetailVM = ViewModelProviders.of(this).get(DetailViewModel.class);

        //region Fetch movie data
        Intent intent = getIntent();
        if (intent != null)
            try {
                String movieString = intent.getStringExtra(MainActivity.MOVIE_EXTRA);
                mMovie = JsonUtils.parseMovieJson(movieString);
            } catch (NullPointerException e) { closeOnError(); }
        else
            closeOnError();
        if (mMovie == null)
            closeOnError();

        mDetailVM.getFavorite(mMovie.getId()).observe(this, mFavoriteCB::setChecked);
        //endregion

        //region Set and load data
        setTitle(mMovie.getTitle());
        Picasso.get().load(MainActivity.POSTER_BASE_PATH + MainActivity.POSTER_WIDTH + mMovie.getPoster()).into(mPosterIV);
        mOrigTitleTV.setText(mMovie.getOrigTitle());
        mReleaseDateTV.setText(mMovie.getRelease());
        mUserRatingTV.setText(getString(R.string.rating, mMovie.getRating(), mMovie.getVotes()));
        mSynopsisTV.setText(mMovie.getSynopsis());
        loadRequests();
        //endregion

        //region onClick listeners
        mRetryButt.setOnClickListener(view -> {
            showProgressBar();
            loadRequests();
        });

        mFavoriteCB.setOnClickListener(view -> {
            MainActivity.favesChanged = true;
            if (mFavoriteCB.isChecked())
                mDetailVM.setFavorite(mMovie);
            else
                mDetailVM.removeFavorite(mMovie);
        });

        mInfoELV.setOnGroupClickListener((expandableListView, view, i, l) -> {
            setListViewHeight(expandableListView, i);
            return false;
        });
        //endregion
    }

    /**
     * To be called when there is an error with loading Activity
     */
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
    }

    //region Request Functions
    /**
     * Function to load Trailers and Reviews through DetailViewModel and set them into the ExpandableListView
     * Called in onCreate and in the onClick for the Retry button
     */
    private void loadRequests() {
        mListTitles = new ArrayList<>();
        mListMap = new HashMap<>();
        mTrailerError = false;
        mReviewError = false;

        //region Get Trailers
        mDetailVM.getTrailers(mMovie.getId()).observe(this, mTrailers -> {
            if (mTrailers == null) { //null return = error w/ load
                setError(TRAILER_TITLE);
                return;
            } else if (mTrailers.length() > 0) {
                mListTitles.add(TRAILER_TITLE);
                mListMap.put(TRAILER_TITLE, JsonUtils.getTrailers(mTrailers));
                mExpandableAdapter.notifyDataSetChanged();
            } if (mListTitles.size() > 1) {
                setListViewHeight(mInfoELV, -1);
                //Set focus back to top of activity
                mPosterIV.requestFocus();
            }
            showELV();
        });
        //endregion

        //region Get Reviews
        mDetailVM.getReviews(mMovie.getId()).observe(this, mReviews -> {
            if (mReviews == null) { //null return = error w/ load
                setError(REVIEW_TITLE);
                return;
            } else if (mReviews.length() > 0) {
                mListTitles.add(REVIEW_TITLE);
                mListMap.put(REVIEW_TITLE, JsonUtils.getReviews(mReviews));
                mExpandableAdapter.notifyDataSetChanged();
            } if (mListTitles.size() > 1) {
                setListViewHeight(mInfoELV, -1);
                //Set focus back to top of activity
                mPosterIV.requestFocus();
            }
            showELV();
        });
        //endregion

        //If only created when not null, mInfoELV doesn't show after reload of data following network error
        mExpandableAdapter = new ExpandableAdapter(this, mListTitles, mListMap);
        mInfoELV.setAdapter(mExpandableAdapter);
    }

    /**
     * Meant for use in Async observation to set non-static and non-final error booleans
     *
     * @param error desired input is either TRAILER_TITLE or REVIEW_TITLE
     */
    private void setError(String error) {
        if (error.equals(TRAILER_TITLE))
            mTrailerError = true;
        else if (error.equals(REVIEW_TITLE))
            mReviewError = true;
        if (mTrailerError && mReviewError)
            showError();
    }
    //endregion

    //region Show/Hide views
    /**
     * Show the ExpandableListView when data has been loader, hide the ProgressBar and error LinearLayout
     */
    private void showELV() {
        mInfoELV.setVisibility(View.VISIBLE);
        mDetailPB.setVisibility(View.GONE);
        mErrorLL.setVisibility(View.GONE);
    }

    /**
     * Show the ProgressBar when loading data, hide the ExpandableListView and error LinearLayout
     */
    private void showProgressBar() {
        mInfoELV.setVisibility(View.GONE);
        mDetailPB.setVisibility(View.VISIBLE);
        mErrorLL.setVisibility(View.GONE);
    }

    /**
     * Show the error LinearLayout when there is an error loading data, and hide the ExpandableListView and ProgressBar
     */
    private void showError() {
        mInfoELV.setVisibility(View.GONE);
        mDetailPB.setVisibility(View.GONE);
        mErrorLL.setVisibility(View.VISIBLE);
    }
    //endregion

    /**
     * Function to programmatically set height of the ExpandableListView
     * created by following tutorial at https://thedeveloperworldisyours.com/android/expandable-listview-inside-scrollview/
     *
     * @param listView The ExpandableListView to be changed
     * @param group    The index of the group being clicked. Set to -1 for initial height set.
     */
    private void setListViewHeight(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (group >= 0 && ((listView.isGroupExpanded(i) && i != group)
                    || (!listView.isGroupExpanded(i) && i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
