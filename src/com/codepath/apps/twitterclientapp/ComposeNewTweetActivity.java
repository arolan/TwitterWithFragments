package com.codepath.apps.twitterclientapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclientapp.models.Tweet;
import com.codepath.apps.twitterclientapp.utils.UserUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeNewTweetActivity extends Activity {

	private static final int MAX_TWEET_CHARACTERS = 140;
	public static final String NEW_TWEET_KEY = "newTweet";
	EditText etComponseNewTweetText;
	ImageView ivCurrentUserImage;
	TextView tvCurrentUserScreenName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Request the feature before setting content view
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_compose_new_tweet);
		etComponseNewTweetText = (EditText) findViewById(R.id.etNewTweetText);
		ivCurrentUserImage = (ImageView) findViewById(R.id.ivCurrentUserPhoto);
		ImageLoader.getInstance().displayImage(UserUtils.getCurrentLoggedInUser().getProfileImageUrl(), ivCurrentUserImage);
		tvCurrentUserScreenName = (TextView) findViewById(R.id.tvScreenName);
		tvCurrentUserScreenName.setText("@"+UserUtils.getCurrentLoggedInUser().getScreenName());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_new_tweet, menu);
		return true;
	}

	public void onCancelBtnClicked(View v) {
		Intent tweetResult = new Intent(this, TimelineActivity.class);
		setResult(RESULT_CANCELED, tweetResult);
		finish();
	}

	public void onTweetBtnClicked(View v) {
		if(etComponseNewTweetText.getText().toString().length() > MAX_TWEET_CHARACTERS)
		{
			Toast.makeText(getApplicationContext(), "Your tweet should not exceed "+ 
					MAX_TWEET_CHARACTERS + " characters.", Toast.LENGTH_LONG).show();
		}
		else {
			Intent tweetResult = new Intent(this, TimelineActivity.class);
			Tweet tweet = createNewTweet();
			tweetResult.putExtra(NEW_TWEET_KEY, tweet );
			setResult(RESULT_OK, tweetResult);

			TwitterApp.getRestClient().postTweet(new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray jsonTweets) {
			
				}
			}, tweet);

			finish();
		}
	}

	private Tweet createNewTweet() {
		Tweet tweet = new Tweet();
		tweet.setTweetId(-1);
		tweet.setBodyText(etComponseNewTweetText.getText().toString());
		tweet.setRetweeted(false);
		tweet.setFavourited(false);

		try {
			DateFormat df = new SimpleDateFormat(Tweet.EEE_MMM_D_HH_MM_SS_Z_YYYY);
			tweet.setTimeStamp(df.parse(df.format(new Date())));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		tweet.setUser(UserUtils.getCurrentLoggedInUser());
		return tweet;
	}

}
