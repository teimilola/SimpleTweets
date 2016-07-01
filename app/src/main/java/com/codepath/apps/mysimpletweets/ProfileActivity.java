package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.Fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Get screenName
        String screenName = getIntent().getStringExtra("screen_name");
        int code= getIntent().getIntExtra("code", 400);
        client= TwitterApplication.getRestClient();

        if (savedInstanceState == null) {
            //Create the user timeline fragment
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            //display User timeline Fragment within activity
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();

        }

        //Get Account Info
        if(code== 35){
                client.showUser(screenName, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        user= User.fromJSON(response);
                        Log.d("USER",response.toString());
                        //My current user accounts information
                        toolbar.setTitle("@" + user.getScreenName());
                        populateProfileHeader(user);
                    }
                });
        }
        else if(code== 10) {
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    //My current user accounts information
                    toolbar.setTitle("@" + user.getScreenName());
                    populateProfileHeader(user);
                }
            });
        }
    }

    private void populateProfileHeader(User user) {
        TextView tvName= (TextView)findViewById(R.id.tvName);
        TextView tvFollowers = (TextView)findViewById(R.id.tvFollowers);
        TextView tvFollowing= (TextView)findViewById(R.id.tvFollowing);
        TextView tvTagLine= (TextView)findViewById(R.id.tvTagLine);
        ImageView ivProfileImage= (ImageView)findViewById(R.id.ivProfileImage);
        tvName.setText(user.getName());
        tvTagLine.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);

    }
}
