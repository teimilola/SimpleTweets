package com.codepath.apps.mysimpletweets.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter aTweets;
    ListView lvTweets;

    //inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fagment_tweet_list, container, false);
        //find the listview
        lvTweets= (ListView)view.findViewById(R.id.lvTweets);
        //connect adapter to listview
        lvTweets.setAdapter(aTweets);
        return view;
    }

   /* public interface TweetListListener {
        void onGetTweetData(ArrayList<Tweet> tweets);
    }*/

    /*public static TweetsListFragment newInstance(Tweet tweet) {
        TweetsListFragment frag = new TweetsListFragment();
        Bundle args = new Bundle();
        args.putParcelable("tweet", tweet);
        frag.setArguments(args);
        return frag;
    }*/

    //creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Create the arraylist (data source)
        tweets= new ArrayList<>();
        //construct the adapter
        aTweets= new TweetArrayAdapter(getActivity(), tweets);

        /*// Return tweets back to activity through the implemented listener
        TweetListListener listener = (TweetListListener) getActivity();
        listener.onGetTweetData(tweets);*/
    }

    public void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
    }

    public void add(int position, Tweet tweet){
        tweets.add(position, tweet);
        aTweets.notifyDataSetChanged();
        lvTweets.setSelection(0);
    }

}
