package com.android.example.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.android.example.popularmovies.R;
import com.android.example.popularmovies.Review;
import com.android.example.popularmovies.Trailer;

import java.util.HashMap;
import java.util.List;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    //region Variables
    private final static String YOUTUBE_BASE_PATH = "http://www.youtube.com/watch?v=";
    private final Context mContext;
    private final List<String> mListTitle;
    private final HashMap<String, List<Object>> mListDetail;
    //endregion

    /**
     * Constructor for the custom ExpandableAdapter
     *
     * @param context              Application context
     * @param expandableListTitle  List of String titles for groups: Trailers, Reviews
     * @param expandableListDetail HashMap with Strings: same as expandableListTitle; List<Object>: either Trailers or Reviews
     */
    public ExpandableAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<Object>> expandableListDetail) {
        mContext = context;
        mListTitle = expandableListTitle;
        mListDetail = expandableListDetail;
    }

    //region Get ID
    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }
    //endregion

    //region Get Count
    @Override
    public int getGroupCount() {
        return mListTitle.size();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return mListDetail.get(this.mListTitle.get(listPosition)).size();
    }
    //endregion

    //region Get Object
    @Override
    public Object getGroup(int listPosition) {
        return mListTitle.get(listPosition);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return mListDetail.get(mListTitle.get(listPosition)).get(expandedListPosition);
    }
    //endregion

    //region Get Views
    @Override
    public View getGroupView(final int listPosition, final boolean isExpanded, View view, final ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater != null ? layoutInflater.inflate(R.layout.expand_list_group, null) : null;
        }
        assert view != null;
        TextView listTitleTextView = view.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return view;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View view, ViewGroup parent) {
        if (getGroup(listPosition) == "Trailers") {
            final Trailer expandedListTrailer = (Trailer) getChild(listPosition, expandedListPosition);
            if (view == null)
                view = inflateTrailer();

            TextView expandedListTextView = view.findViewById(R.id.expandedListItemT);
            if (expandedListTextView == null) {
                view = inflateTrailer();
                expandedListTextView = view.findViewById(R.id.expandedListItemT);
            }
            expandedListTextView.setText(expandedListTrailer.getName());
            expandedListTextView.setOnClickListener(view1 -> {
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(YOUTUBE_BASE_PATH + expandedListTrailer.getPath()));
                mContext.startActivity(webIntent);
            });
        } else {
            final Review expandedListReview = (Review) getChild(listPosition, expandedListPosition);
            if (view == null)
                view = inflateReview();

            TextView expandedListAuthorTV = view.findViewById(R.id.author_tv);
            if (expandedListAuthorTV == null) {
                view = inflateReview();
                expandedListAuthorTV = view.findViewById(R.id.author_tv);
            }
            expandedListAuthorTV.setText(mContext.getString(R.string.author, expandedListReview.getAuthor()));

            TextView expandedListContentTV = view.findViewById(R.id.content_tv);
            expandedListContentTV.setText(expandedListReview.getContent());
        }
        return view;
    }
    //endregion

    //region Inflate Layouts
    private View inflateTrailer() {
        LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return layoutInflater != null ? layoutInflater.inflate(R.layout.trailer_list_item, null) : null;
    }

    private View inflateReview() {
        LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return layoutInflater != null ? layoutInflater.inflate(R.layout.review_list_item, null) : null;
    }
    //endregion

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
