package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by temilola on 6/27/16.
 */
/*
"user":{
    "location":"Johannesburg",
    "statuses_count":21339,
    "profile_banner_url":"https:...jpg",
    "id":59147657,
    "created_at":"Wed Jul 22 15:01:24 +0000 2009",
    "followers_count":1373,
    "profile_image_url_https":"https...jpg",
    "profile_background_image_url":"http:....jpeg",
    "profile_background_image_url_https":"https...jpeg",
    "friends_count":659,
    "screen_name":"KingIdowu_",
    }
*/
public class User {
    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    //list the attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;

    //deserialize the user json into a user object
    public static User fromJSON(JSONObject json){
        User u = new User();
        try {
            u.name= json.getString("name");
            u.uid= json.getLong("id");
            u.screenName= json.getString("screen_name");
            u.profileImageUrl= json.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return a user
        return u;
    }
}
