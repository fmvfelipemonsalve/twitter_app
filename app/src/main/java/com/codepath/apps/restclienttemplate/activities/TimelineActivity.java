package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.ProgressIndicatorListener {

    //TweetsListFragment fragmentTweetsList;

    SwipeRefreshLayout swipeContainer;
    MenuItem miActionProgressItem;

    TweetsPagerAdapter adapter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //set up the toolbar as the main action bar
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        //create adapter to be used for the pager
        adapter = new TweetsPagerAdapter(getSupportFragmentManager(),this);
        //set the adapter for the pager
        vpPager.setAdapter(adapter);
        //setup the TabLayout to use the view pager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);


        //find the swipe refresh layout and add the onRefreshListener
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(R.color.twitter_blue);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //call method to repopulate the timeline
                getCurrentFragment().populateTimeline(0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Return to finish
        miActionProgressItem.setVisible(true);
        return true;
    }

    //code to make the action_view_progress loading item disappear
    //REQUIRED TO IMPLEMENT PROGRESS INDICATOR LISTENER
    @Override
    public void hideProgressIndicators() {
        // Hide progress item
        if (miActionProgressItem!=null)
            miActionProgressItem.setVisible(false);
        swipeContainer.setRefreshing(false);
    }

    //code to launch compose activity when compose in the toolbar is clicked
    public void onCompose(MenuItem mi){
        Intent i = new Intent(this,ComposeActivity.class);
        i.putExtra("reply_to","");
        i.putExtra("uid",0);
        startActivityForResult(i,20);
    }

    //code launched when compose activity is finished
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == 20) {
            // Extract name value from result extras
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
//            tweets.add(0,tweet);
//            tweetAdapter.notifyItemInserted(0);
//            rvTweets.scrollToPosition(0);
        }
    }

    private TweetsListFragment getCurrentFragment(){
        int pos=tabLayout.getSelectedTabPosition();
        TweetsListFragment fragment=((TweetsListFragment)adapter.getFragmentInstance(pos));
        return fragment;
    }

    public void onProfileView(MenuItem item) {
        Intent i = new Intent(this,ProfileActivity.class);
        startActivity(i);
    }
}
