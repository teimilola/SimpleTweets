package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        //return view
        return convertView;
    }
}
