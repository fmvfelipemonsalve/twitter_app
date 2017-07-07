package com.codepath.apps.restclienttemplate.fragments;

import com.codepath.apps.restclienttemplate.helpers.CustomResponseHandler;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

/**
 * Created by fmonsalve on 7/6/17.
 */

public class HomeTimelineFragment extends TweetsListFragment {

    @Override
    public void populateTimeline(int page){
        client.getHomeTimeline(new CustomResponseHandler("PopulateHomeTimeline"){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addItems(response);
                ((ProgressIndicatorListener)getActivity()).hideProgressIndicators();
            }
        });
    }

}
