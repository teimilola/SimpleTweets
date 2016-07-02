package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class TweetDetailsActivity extends AppCompatActivity {

    private TwitterClient client;
    Tweet tweet;
    Tweet retweet;
    Tweet fav;
    private boolean isFavorited;
    private boolean isRetweeted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        tweet= getIntent().getParcelableExtra("tweet");

        client = TwitterApplication.getRestClient();

        final int favCount;
        final int rtCount;

        final ImageView ivProfileImage= (ImageView)findViewById(R.id.ivProfileImage);
        TextView tvUsername= (TextView)findViewById(R.id.tvUserName);
        TextView tvTweet= (TextView)findViewById(R.id.tvTweet);
        final TextView tvRTCount= (TextView)findViewById(R.id.tvRTCount);
        final TextView tvFavCount= (TextView)findViewById(R.id.tvLikeCount);
        ImageView ivReply= (ImageView)findViewById(R.id.ivReply);
        final ImageView ivFavorite= (ImageView)findViewById(R.id.ivFavorite);
        final ImageView ivRetweet= (ImageView)findViewById(R.id.ivRetweet);
        ImageView ivEmbedded= (ImageView)findViewById(R.id.ivEmbedded);

        favCount= tweet.getFavCount();
        rtCount= tweet.getRtCount();
        isFavorited= tweet.isFavorited();
        isRetweeted= tweet.isRetweeted();

        tvUsername.setText(tweet.getUser().getScreenName());
        tvTweet.setText(tweet.getBody());
        tvFavCount.setText(String.valueOf(tweet.getFavCount()));

        if(tweet.getRtCount()==1){
            tvRTCount.setText(String.valueOf(rtCount) + " RETWEET");
        }
        else {
            tvRTCount.setText(String.valueOf(rtCount) + " RETWEETS");
        }

        if(tweet.getFavCount()==1){
            tvFavCount.setText(String.valueOf(favCount) + " LIKE");
        }
        else{
            tvFavCount.setText(String.valueOf(favCount) + " LIKES");
        }

        ivProfileImage.setImageResource(android.R.color.transparent); //clear out the old image for a recycled view
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).transform(new RoundedTransformation(10, 10)).fit().centerCrop().into(ivProfileImage);
        Picasso.with(this).load(tweet.getMediaUrl()).into(ivEmbedded);
        ivProfileImage.setTag(tweet.getUser().getScreenName());
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(TweetDetailsActivity.this, ProfileActivity.class);
                i.putExtra("screen_name", ivProfileImage.getTag().toString());
                i.putExtra("code", 35);
                startActivity(i);
            }
        });

        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(TweetDetailsActivity.this, ComposeActivity.class);
                i.putExtra("screen_name", ivProfileImage.getTag().toString());
                i.putExtra("code", 40);
                startActivity(i);
            }
        });

        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRetweeted){
                    unRetweet(tweet.getUid());
                    ivRetweet.setImageResource(R.drawable.ic_retweet_icon);
                    isRetweeted= false;
                }else {
                    retweet(tweet.getUid());
                    ivRetweet.setImageResource(R.drawable.ic_retweet_clicked);
                    //tvRTCount.setText(String.valueOf(rtCount+1) + " RETWEETS");
                }
            }
        });

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavorited){
                    deleteFavorite(tweet.getUid());
                    ivFavorite.setImageResource(R.drawable.ic_favorite_icon);
                    isFavorited= false;
                }else {
                    favorite(tweet.getUid());
                    ivFavorite.setImageResource(R.drawable.ic_favorited);
                    // tvFavCount.setText(String.valueOf(favCount+1) + " LIKES");
                }
            }
        });

        if(isFavorited){
            ivFavorite.setImageResource(R.drawable.ic_favorited);
        }
        else{
            ivFavorite.setImageResource(R.drawable.ic_favorite_icon);
        }

        if(isRetweeted){
            ivRetweet.setImageResource(R.drawable.ic_retweet_clicked);
        }else{
            ivRetweet.setImageResource(R.drawable.ic_retweet_icon);
        }

        TextView tvDate= (TextView)findViewById(R.id.tvDate);

        String date= getRelativeTimeAgo(tweet.getCreatedAt());

        if(date.contains("ago")){
            date=  date.substring(0, date.length()-4);
        }
        if(date.contains("hours")){
            date= date.substring(0,(date.length()-6));
            date= date+"h";
        }else if(date.contains("hour")){
            date= date.substring(0,(date.length()-5));
            date= date+"h";
        }
        if(date.contains("minutes")){
            date= date.substring(0, (date.length()-8));
            date= date+ "m";
        }
        if(date.contains("seconds")){
            date= date.substring(0, (date.length()-8));
            date= date+ "s";
        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        if(date.contains("Jan")||date.contains("Feb")||date.contains("Mar")||date.contains("Apr")){
            date= date.substring(3,(date.length()-6))+ " "+ date.substring(0,3) +" "+ date.substring((date.length()-5),date.length());
        }
        if(date.contains("May")||date.contains("Jun")||date.contains("July")||date.contains("Aug")){
            date= date.substring(3,(date.length()-6))+ " "+ date.substring(0,3) +" "+ date.substring((date.length()-5),date.length());
        }
        if(date.contains("Sep")||date.contains("Oct")||date.contains("Nov")||date.contains("Dec")){
            date= date.substring(3,(date.length()-6))+ " "+ date.substring(0,3) +" "+ date.substring((date.length()-5),date.length());
        }
        if(date.contains("2016")){
            date=date.substring(0,(date.length()-6));
        }
        tvDate.setText(date);

    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
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

        return relativeDate;
    }

    public void retweet(long id){
        retweet= new Tweet();
        client.postRetweet(id, new JsonHttpResponseHandler() {
            //SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("DEBUG", json.toString());
                retweet = Tweet.fromJSON(json);
            }

            //FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // Log.d("DEBUG", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }
        });
    }

    public void favorite(long id){
        fav= new Tweet();
        client.postFavorite(id, new JsonHttpResponseHandler() {
            //SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("DEBUG", json.toString());
                fav = Tweet.fromJSON(json);
            }

            //FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // Log.d("DEBUG", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }
        });


    }

    public void deleteFavorite(long id){
        fav= new Tweet();
        client.deleteFavorite(id, new JsonHttpResponseHandler() {
            //SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("DEBUG", json.toString());
                fav = Tweet.fromJSON(json);
            }

            //FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // Log.d("DEBUG", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }
        });
    }

    public void unRetweet(long id){
        retweet= new Tweet();
        client.unRetweet(id, new JsonHttpResponseHandler() {
            //SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("DEBUG", json.toString());
                retweet = Tweet.fromJSON(json);
            }

            //FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // Log.d("DEBUG", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }
        });
    }

}
