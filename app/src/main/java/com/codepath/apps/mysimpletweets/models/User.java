package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

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
public class User implements Parcelable {
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

    public String getTagline() {
        return tagline;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    //list the attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String tagline;
    private int followersCount;
    private int followingCount;

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeLong(uid);
        out.writeString(screenName);
        out.writeString(profileImageUrl);
        out.writeString(tagline);
        out.writeInt(followersCount);
        out.writeInt(followingCount);
    }

    private User(Parcel in){
        name= in.readString();
        uid= in.readLong();
       screenName= in.readString();
        profileImageUrl= in.readString();
        tagline= in.readString();
        followersCount= in.readInt();
        followingCount= in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(){

    }

    //deserialize the user json into a user object
    public static User fromJSON(JSONObject json){
        User u = new User();
        try {
            u.name= json.getString("name");
            u.uid= json.getLong("id");
            u.screenName= json.getString("screen_name");
            u.profileImageUrl= json.getString("profile_image_url");
            u.tagline= json.getString("description");
            u.followersCount= json.getInt("followers_count");
            u.followingCount= json.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return a user
        return u;
    }
}
