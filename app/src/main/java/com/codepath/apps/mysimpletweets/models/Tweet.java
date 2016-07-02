package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*[{"retweeted_status":{"contributors":null,"text":"One must mentally prepare for that heatwave...",
"lang":"en","entities":{"urls":[],"hashtags":[],"user_mentions":[],"symbols":[]},
"in_reply_to_status_id_str":null,"is_quote_status":false,"id":747406520845991936,
"in_reply_to_user_id_str":null,"source":"<a href=\"http:\/\/twitter.com\/download\/iphone\"
rel=\"nofollow\">Twitter for iPhone<\/a>","favorited":false,"in_reply_to_status_id":null,
"retweet_count":6,"in_reply_to_user_id":null,"created_at":"Mon Jun 27 12:29:35 +0000 2016",
"favorite_count":1,"id_str":"747406520845991936","place":null,
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

//Parse the JSON + store the data, encapsulate state logic or display login
public class Tweet implements Parcelable{
    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public int getFavCount() {
        return favCount;
    }

    public int getRtCount() {
        return rtCount;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    //list out the attributes
    private String mediaUrl;
    private int rtCount;
    private int favCount;
    private boolean favorited;
    private boolean retweeted;
    private String body;
    private long uid;
    private User user; //store embedded user object
    private String createdAt;

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mediaUrl);
        out.writeInt(rtCount);
        out.writeInt(favCount);
        out.writeInt(favorited ? 1 : 0);
        out.writeInt(retweeted ? 1 : 0);
        out.writeLong(uid);
        out.writeString(body);
        out.writeString(createdAt);
        out.writeParcelable(user, flags);
    }

    private Tweet(Parcel in){
        mediaUrl=in.readString();
        rtCount= in.readInt();
        favCount= in.readInt();
        favorited  = (in.readInt() == 0) ? false : true;
       retweeted = (in.readInt() == 0) ? false : true;
        uid= in.readLong();
        body= in.readString();
        createdAt= in.readString();
        user = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Tweet> CREATOR
            = new Parcelable.Creator<Tweet>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    public Tweet(){

    }

    //Deserailizse the JSON
    //tweet from JSON <==> Tweet
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        //extract values form the JSON, store here
        try {
            JSONArray media = jsonObject.getJSONObject("entities").optJSONArray("media");
            if(media != null && media.length() > 0) {
                tweet.mediaUrl =media.getJSONObject(0).getString("media_url");
            }
            tweet.body= jsonObject.getString("text");
            tweet.uid= jsonObject.getLong("id");
            tweet.createdAt= jsonObject.getString("created_at");
            tweet.user= User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.rtCount= jsonObject.getInt("retweet_count");
            tweet.favCount= jsonObject.getInt("favorite_count");
            tweet.favorited= jsonObject.getBoolean("favorited");
            tweet.retweeted= jsonObject.getBoolean("retweeted");
            if(!jsonObject.isNull("retweeted_status")){
                JSONObject retweetedJson= jsonObject.getJSONObject("retweeted_status");
                tweet.favCount= retweetedJson.getInt("favorite_count");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){
        ArrayList<Tweet> tweets= new ArrayList<>();
        for(int i=0; i< jsonArray.length(); i++){
            JSONObject tweetJson= null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet= Tweet.fromJSON(tweetJson);
                if(tweet != null){
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }
}
