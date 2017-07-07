package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.helpers.CustomResponseHandler;
import com.codepath.apps.restclienttemplate.helpers.TwitterApp;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    EditText etTweet;
    TextView tvCharacters;
    long uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        Intent i = getIntent();
        String reply_to = i.getStringExtra("reply_to");
        uid = i.getLongExtra("uid",0);

        etTweet = (EditText) findViewById(R.id.etTweet);
        tvCharacters = (TextView) findViewById(R.id.tvCharacters);

        etTweet.setText(reply_to);
        etTweet.setSelection(etTweet.getText().length());
        charCount();

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charCount();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void charCount(){
        int charCount=140-Integer.valueOf(etTweet.getText().length());
        tvCharacters.setText(String.format("%s",charCount));
        if (charCount<10)
            tvCharacters.setTextColor(Color.RED);
        else
            tvCharacters.setTextColor(Color.BLACK);
    }

    public void onSubmit(View view){
        String tweetString=etTweet.getText().toString();

        TwitterApp.getRestClient().sendTweet(tweetString, uid, new CustomResponseHandler("ComposeActivity"){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Intent i = new Intent(ComposeActivity.this,TimelineActivity.class);
                    i.putExtra("tweet", Parcels.wrap(Tweet.fromJSON(response)));
                    setResult(RESULT_OK,i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
