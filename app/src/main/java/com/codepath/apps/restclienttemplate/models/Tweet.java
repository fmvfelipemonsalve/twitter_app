package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by fmonsalve on 6/26/17.
 */
@Parcel
public class Tweet {

    //list of attributes
    public String body;
    public long uid; //db id for tweet
    public User user;
    public String createdAt;
    public boolean retweeted;
    public boolean favorited;
    public int retweet_count;
    public int favorite_count;

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet=new Tweet();

        //extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweet_count = jsonObject.getInt("retweet_count");
        tweet.favorite_count = jsonObject.getInt("favorite_count");



        return tweet;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        String myFormat = "mm yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        return reformatRelativeDate(relativeDate,rawJsonDate);
    }

    private static String reformatRelativeDate(String relativeDate,String rawDate){
        String[] parts = relativeDate.split(" ");
        String[] date = relativeDate.split(" ");
        if (parts[0].equals("in")){
            return relativeDate;
        }else if (parts[1].equals("month") || parts[1].equals("months")){
            return date[1];
        }else if (parts[1].equals("year") || parts[1].equals("years")){
            return date[5];
        }else{
            return parts[0]+parts[1].charAt(0);
        }
    }
}
