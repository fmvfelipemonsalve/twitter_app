package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.helpers.TwitterApp;
import com.codepath.apps.restclienttemplate.helpers.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailsActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    TextView tvName;
    TextView tvScreenName;
    TextView tvBody;
    TextView tvTimeStamp;
    ImageView ivReply;
    ImageView ivRetweet;
    ImageView ivFavorite;

    Tweet tweet;
    TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivProfileImage = (ImageView) findViewById(R.id.ivProfilePicture);
        tvName = (TextView) findViewById(R.id.tvName);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);
        ivReply = (ImageView) findViewById(R.id.ivReply);
        ivRetweet = (ImageView) findViewById(R.id.ivRetweet);
        ivFavorite = (ImageView) findViewById(R.id.ivFavorite);

        Intent intent=getIntent();
        tweet = Parcels.unwrap(intent.getParcelableExtra("tweet"));

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(this, 5, 0))
                .into(ivProfileImage);
        tvName.setText(tweet.user.name);
        tvScreenName.setText(tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvTimeStamp.setText(tweet.createdAt);
        if (tweet.retweeted){
            ivRetweet.setColorFilter(R.color.medium_green);
        }
        if (tweet.favorited){
            ivFavorite.setColorFilter(R.color.medium_red);
        }

        client = TwitterApp.getRestClient();

        final AsyncHttpResponseHandler handler=new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(DetailsActivity.this,"Action Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(DetailsActivity.this,"Action Failed", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        };

        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweet.retweeted) {
                    ivRetweet.setImageDrawable(getResources().getDrawable(R.drawable.ic_vector_retweet_stroke));
                    tweet.retweeted = false;
                    client.unRetweet(tweet.uid,handler);
                } else {
                    ivRetweet.setImageDrawable(getResources().getDrawable(R.drawable.ic_vector_retweet));
                    tweet.retweeted = true;
                    client.retweet(tweet.uid,handler);
                }
            }
        });

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweet.favorited) {
                    ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_vector_heart_stroke));
                    tweet.favorited = false;
                    client.unFavoriteTweet(tweet.uid,handler);
                } else {
                    ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_vector_heart));
                    tweet.favorited=true;
                    client.favoriteTweet(tweet.uid,handler);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == 21) {
            // Extract name value from result extras
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
//            TimelineActivity.tweets.add(0,tweet);
//            tweetAdapter.notifyItemInserted(0);
//            rvTweets.scrollToPosition(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(this,TimelineActivity.class);
            startActivity(i);

        }
        return super.onKeyDown(keyCode, event);
    }

}
