package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    Tweet tweet;
    private TwitterClient client;
    String status;
    EditText etTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etTweet= (EditText)findViewById(R.id.etTweet);
        Button btnTweet= (Button)findViewById(R.id.btnTweet);
        ImageView ivProfileImage= (ImageView) findViewById(R.id.ivProfileImage);

        //Get Client
        client = TwitterApplication.getRestClient(); //singleton client
        tweet= new Tweet();
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status= etTweet.getText().toString();
                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                client.postTweet(status, new JsonHttpResponseHandler() {
                    //SUCCESS
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                        Log.d("DEBUG", json.toString());
                        tweet= Tweet.fromJSON(json);
                        Intent data= new Intent();
                        data.putExtra("tweet", tweet);
                        // Activity finished ok, return the data
                        setResult(RESULT_OK, data); // set result code and bundle data for response
                        finish(); //

                        //Deserialize JSON
                        //Create Models and add to adapter
                        //Load the model data into listview

                       // addAll(Tweet.fromJSONArray(json));
                        //  Log.d("DEBUG", aTweets.toString());
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





}
