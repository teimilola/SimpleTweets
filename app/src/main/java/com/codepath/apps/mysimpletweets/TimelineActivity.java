package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.Fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.Fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;

public class TimelineActivity extends AppCompatActivity {
    private final int REQUEST_CODE= 20;
    SmartFragmentStatePagerAdapter adapterViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(" ");
        // Display icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_actionbar_tweet_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //Get ViewPager
        ViewPager vpPager= (ViewPager)findViewById(R.id.viewpager);
        //Set ViewPager adapter for the pager
        adapterViewPager= new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        //Find the pager sliding tabs
        PagerSlidingTabStrip tabStrip= (PagerSlidingTabStrip)findViewById(R.id.tabs);
        //Attach the tab strip to the viewpager
        tabStrip.setViewPager(vpPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void onProfileView(MenuItem mi) {
            //Launch the profile view
            // Toast.makeText(this, "Invisible", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
        i.putExtra("code", 10);
        //i.putExtra("screenName", mtweets.get().getUser().getScreenName());
        startActivity(i);
    }

    public void onComposeTweet(MenuItem item) {
        //launch Compose Tweet view
        //Toast.makeText(this, "Composed", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        i.putExtra("code", 30);
        //i.putExtra("profile", mtweets.get().getUser().getProfileImageUrl());
       startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = (Tweet) data.getExtras().getParcelable("tweet");
            HomeTimelineFragment fragmentHomeTweets =
                    (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
            fragmentHomeTweets.appendTweet(tweet);
        }
    }


    //Return the order of the fragments in the view pager
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter{
        private String tabTitles[]= {"Home", "Mentions"};

        //Adapter gets manager which it uses to insert or remove fragments from the activity
        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        //Controls order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return new HomeTimelineFragment();
            }
            else if(position==1){
                return new MentionsTimelineFragment();
            }
            else{
                return null;
            }
        }

        //Returns tabTitle at the top
        @Override
        public CharSequence getPageTitle(int position){
            return tabTitles[position];
        }

        //Tells it how many tabs there are to slide between
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }




}
