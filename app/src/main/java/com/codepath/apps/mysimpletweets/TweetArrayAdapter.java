package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by temilola on 6/27/16.
 */
public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetArrayAdapter(Context context, List<Tweet> tweets){
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get tweet
        Tweet tweet= getItem(position);
        //Find or inflate the view
        if(convertView == null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        //find subviews
        ImageView ivProfileImage= (ImageView)convertView.findViewById(R.id.ivProfileImage);
        TextView tvUsername= (TextView)convertView.findViewById(R.id.tvUserName);
        TextView tvTweet= (TextView)convertView.findViewById(R.id.tvTweet);
        tvUsername.setText(tweet.getUser().getScreenName());
        tvTweet.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent); //clear out the old image for a recycled view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        TextView tvDate= (TextView)convertView.findViewById(R.id.tvDate);
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
        //return view
        return convertView;
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
}
