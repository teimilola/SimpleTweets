package com.codepath.apps.mysimpletweets.models;

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
public class Tweet {
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
    //list out the attributes
    private String body;
    private long uid;
    private User user; //store embedded user object
    private String createdAt;

    //Deserailizse the JSON
    //tweet from JSON <==> Tweet
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        //extract values form the JSON, store here
        try {
            tweet.body= jsonObject.getString("text");
            tweet.uid= jsonObject.getLong("id");
            tweet.createdAt= jsonObject.getString("created_at");
            tweet.user= User.fromJSON(jsonObject.getJSONObject("user"));
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
