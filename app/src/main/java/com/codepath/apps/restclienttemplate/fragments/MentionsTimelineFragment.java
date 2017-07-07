package com.codepath.apps.restclienttemplate.fragments;

import com.codepath.apps.restclienttemplate.helpers.CustomResponseHandler;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

/**
 * Created by fmonsalve on 7/7/17.
 *
 */

public class MentionsTimelineFragment extends TweetsListFragment {

    @Override
    public void populateTimeline(int page){
        client.getMentionsTimeline(new CustomResponseHandler("PopulateMentionsTimeline"){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addItems(response);
                ((ProgressIndicatorListener)getActivity()).hideProgressIndicators();
            }
        });
    }

}
