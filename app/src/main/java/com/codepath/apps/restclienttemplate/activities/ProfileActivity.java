package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.helpers.CustomResponseHandler;
import com.codepath.apps.restclienttemplate.helpers.TwitterApp;
import com.codepath.apps.restclienttemplate.helpers.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    UserTimelineFragment userTimelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //set up the toolbar as the main action bar
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client= TwitterApp.getRestClient();

        String screenName=getIntent().getStringExtra("screen_name");
        //create the user fragment
        userTimelineFragment = UserTimelineFragment.newInstance(screenName);
        //display the user timeline fragment inside the container (dinamically)
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //make change
        ft.replace(R.id.flContainer,userTimelineFragment);
        //commit
        ft.commit();

        client.getUserInfo(new CustomResponseHandler("ProfileActivity"){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //deserialize user object
                    User user = User.fromJson(response);
                    //set title of Toolbar based on user info
                    getSupportActionBar().setTitle(String.format("@%s",user.screenName));
                    //populate user headline
                    populateUserHeadline(user);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void populateUserHeadline(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(user.name);
        tvTagline.setText(user.tagline);
        tvFollowers.setText(String.format("%s Followers",user.followers));
        tvFollowing.setText(String.format("%s Following",user.following));
        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == 20) {
            // Extract name value from result extras
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            userTimelineFragment.addTweetToList(tweet);
        } else if (resultCode == RESULT_OK && requestCode == 21) {
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            int position = data.getIntExtra("position",0);
            userTimelineFragment.replaceTweetInList(tweet,position);

            ArrayList<Parcelable> tweetArrayList = data.getParcelableArrayListExtra("tweet_list");
            for (int i = 0; i < tweetArrayList.size(); i++){
                userTimelineFragment.addTweetToList((Tweet) Parcels.unwrap(tweetArrayList.get(i)));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent();
            setResult(RESULT_OK,i);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
