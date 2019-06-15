package com.android.example.popularmovies.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.example.popularmovies.MainActivity;
import com.android.example.popularmovies.R;
import com.android.example.popularmovies.utils.JsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final MovieAdapterOnClickHandler mClickHandler;
    private final int mPadding;
    private String[] mPosterPaths;
    private JSONArray mMovieData;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler, int padding){
        mClickHandler = clickHandler;
        mPadding = padding;
    }

    public void setMovieData(JSONArray movieArray){
        mMovieData = movieArray;
        mPosterPaths = JsonUtils.getMoviePosters(mMovieData);
    }

    public interface MovieAdapterOnClickHandler { void onClick(JSONObject movie); }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.poster_list_item, viewGroup, false);
        view.setPadding(0, 0, mPadding, mPadding);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder movieAdapterViewHolder, int pos) {
        Picasso.get().load(MainActivity.POSTER_BASE_PATH + MainActivity.POSTER_WIDTH + mPosterPaths[pos]).into(movieAdapterViewHolder.poster);
    }

    @Override
    public int getItemCount()
    {
        if (mMovieData == null) return 0;
        return mMovieData.length();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView poster;

        MovieAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.list_poster_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPos = getAdapterPosition();
            mClickHandler.onClick(mMovieData.optJSONObject(adapterPos));
        }
    }
}
