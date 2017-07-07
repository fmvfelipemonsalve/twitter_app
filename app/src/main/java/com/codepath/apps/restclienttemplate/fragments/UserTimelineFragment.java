package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;

import com.codepath.apps.restclienttemplate.helpers.CustomResponseHandler;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

/**
 * Created by fmonsalve on 7/7/17.
 *
 */

public class UserTimelineFragment extends TweetsListFragment {

    public static UserTimelineFragment newInstance(String screenName){
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Override
    public void populateTimeline(int page){
        String screenName=getArguments().getString("screen_name");
        client.getUserTimeline(screenName,new CustomResponseHandler("PopulateUserTimeline"){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addItems(response);
            }
        });
    }

}
