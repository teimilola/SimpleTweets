package com.codepath.apps.mysimpletweets.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by temilola on 6/27/16.
 */
public abstract class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter aTweets;
    ListView lvTweets;
    protected SwipeRefreshLayout swipeContainer;


    //inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fagment_tweet_list, container, false);
        //find the listview
        lvTweets= (ListView)view.findViewById(R.id.lvTweets);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //connect adapter to listview
        lvTweets.setAdapter(aTweets);
        return view;
    }


    //creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Create the arraylist (data source)
        tweets= new ArrayList<>();
        //construct the adapter
        aTweets= new TweetArrayAdapter(getActivity(), tweets);

    }

    protected abstract void fetchTimelineAsync();


    public void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
    }

    public void clear(){
        aTweets.clear();
    }

    public void add(int position, Tweet tweet){
        tweets.add(position, tweet);
        aTweets.notifyDataSetChanged();
        lvTweets.setSelection(0);
    }

    public void onProfileView(View view) {


    }

}
