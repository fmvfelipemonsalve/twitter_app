package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.helpers.TweetAdapter;
import com.codepath.apps.restclienttemplate.helpers.TwitterApp;
import com.codepath.apps.restclienttemplate.helpers.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Felipe Monsalve on 7/3/17.
 *
 */

public abstract class TweetsListFragment extends Fragment {

    public interface ProgressIndicatorListener{
        void hideProgressIndicators();
    }

    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;

    TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client= TwitterApp.getRestClient();
        populateTimeline(0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_tweets_list,container,false);

        rvTweets= (RecyclerView) v.findViewById(R.id.rvTweet);
        //init the ArrayList (data source)
        tweets = new ArrayList<>();
        //construct the adapter from this data source
        tweetAdapter=new TweetAdapter(tweets);
        //RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);

        return v;
    }

    public void addItems(JSONArray response){
        // Remember to CLEAR OUT old items before appending in the new ones
        tweetAdapter.clear();
        // ...the data has come back, add new items to your adapter...
        tweetAdapter.addAll(response);
    }

    //requires that all subclasses of TweetListFragment implement a method to populate the timeline
    public abstract void populateTimeline(int page);
}
