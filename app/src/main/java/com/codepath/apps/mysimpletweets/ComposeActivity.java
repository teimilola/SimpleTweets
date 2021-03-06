package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    Tweet tweet;
    User user;
    private TwitterClient client;
    EditText etTweet;
    Button btnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etTweet = (EditText) findViewById(R.id.etTweet);
        String status;
         btnTweet= (Button) findViewById(R.id.btnTweet);
        ImageView ivExit = (ImageView) findViewById(R.id.ivExit);
        ivExit.setImageResource(R.drawable.ic_exit_icon);
        final ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        TwitterClient newClient = TwitterApplication.getRestClient();
        final String screenName = getIntent().getStringExtra("screen_name");
        int code = getIntent().getIntExtra("code", 400);
        user = new User();
        newClient.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                Picasso.with(getApplicationContext()).load(user.getProfileImageUrl()).transform(new RoundedTransformation(10, 10)).fit().centerCrop().into(ivProfileImage);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        if(code == 40){
            etTweet.setText("@"+screenName+ " ");
            int position = etTweet.length();
            etTweet.setSelection(position);
            postTweet();

        }
        else if(code== 30) {
            postTweet();
        }

    }

    public void postTweet(){
        client = TwitterApplication.getRestClient(); //singleton client
        tweet = new Tweet();
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status;
                status = etTweet.getText().toString();
                //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                client.postTweet(status, new JsonHttpResponseHandler() {
                    //SUCCESS
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                        Log.d("DEBUG", json.toString());
                        tweet = Tweet.fromJSON(json);
                        Intent data = new Intent();
                        data.putExtra("tweet", tweet);
                        // Activity finished ok, return the data
                        setResult(RESULT_OK, data); // set result code and bundle data for response
                        finish(); //
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
        });

    }


    public void onExit(View view) {
        finish();
    }
}
