package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by fmonsalve on 6/26/17.
 */
@Parcel
public class User {
    //list of attributes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    public int following;
    public int followers;
    public String tagline;

    //deserialize json
    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user=new User();

        //extract values
        user.name=jsonObject.getString("name");
        user.uid=jsonObject.getLong("id");
        user.screenName=jsonObject.getString("screen_name");
        user.profileImageUrl=jsonObject.getString("profile_image_url");
        user.followers=jsonObject.getInt("followers_count");
        user.following=jsonObject.getInt("friends_count");
        user.tagline=jsonObject.getString("description");

        return user;
    }
}
