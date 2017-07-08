package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.helpers.CustomResponseHandler;
import com.codepath.apps.restclienttemplate.helpers.TwitterApp;
import com.codepath.apps.restclienttemplate.helpers.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

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

    //USED TO SAVE ANY REPLY TWEETS COMPOSED FROM THE DETAIL VIEW
    ArrayList<Parcelable> tweetArrayList;
    //USED TO RETURN WHICH POSITION THE TWEET IS IN THE RECYCLER VIEW
    int position;
    Tweet retweetedTweet;


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
        position = intent.getIntExtra("position",0);
        tweetArrayList=new ArrayList<>();

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(this, 5, 0))
                .into(ivProfileImage);
        tvName.setText(tweet.user.name);
        tvScreenName.setText("@"+tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvTimeStamp.setText(tweet.createdAt);
        if (tweet.retweeted){
            ivRetweet.setImageDrawable(getResources().getDrawable(R.drawable.ic_vector_retweet));
        }
        if (tweet.favorited){
            ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_vector_heart));
        }

        client = TwitterApp.getRestClient();

        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this,ComposeActivity.class);
                i.putExtra("reply_to","@"+tweet.user.screenName+" ");
                i.putExtra("uid",tweet.uid);
                startActivityForResult(i,20);
            }
        });

        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweet.retweeted) {
                    client.unRetweet(tweet.uid,new CustomResponseHandler("Unretweet"){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ivRetweet.setImageDrawable(getResources().getDrawable(R.drawable.ic_vector_retweet_stroke));
                            tweet.retweeted = false;
//                            tweetArrayList.remove(Parcels.wrap(retweetedTweet));
//                            retweetedTweet=null;
                        }
                    });
                } else {
                    client.retweet(tweet.uid,new CustomResponseHandler("Retweet"){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ivRetweet.setImageDrawable(getResources().getDrawable(R.drawable.ic_vector_retweet));
                            tweet.retweeted = true;
//                            try {
//                                retweetedTweet = Tweet.fromJSON(response);
//                                tweetArrayList.add(Parcels.wrap(retweetedTweet));
//                            }catch (JSONException e){
//                                e.printStackTrace();
//                            }
                        }
                    });
                }
            }
        });

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweet.favorited) {
                    client.unFavoriteTweet(tweet.uid,new CustomResponseHandler("Unfavorite"){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_vector_heart_stroke));
                            tweet.favorited = false;
                        }
                    });
                } else {
                    client.favoriteTweet(tweet.uid,new CustomResponseHandler("Favorite"){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_vector_heart));
                            tweet.favorited=true;
                        }
                    });
                }
            }
        });

    }

    @Override
    //REQUEST CODES:
    // 20-new tweet (from compose)
    // 21-from detail activity: contains both a possible tweet reply and the updated tweet object
    // 22-from profile activity: doesn't include params... only refreshes the recycler view
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == 20) {
            // Extract name value from result extras
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            tweetArrayList.add(Parcels.wrap(tweet));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent();
            i.putExtra("detail_tweet",Parcels.wrap(tweet));
            i.putExtra("tweet_list",tweetArrayList);
            i.putExtra("position",position);
            setResult(RESULT_OK,i);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
