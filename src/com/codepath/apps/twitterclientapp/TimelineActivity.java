package com.codepath.apps.twitterclientapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.codepath.apps.twitterclientapp.models.Tweet;
import com.codepath.apps.twitterclientapp.models.User;
import com.codepath.apps.twitterclientapp.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {

	public static final String LOAD_OFFLINE_KEY = "loadOffline";
	private static final String DEFAULT_NUMBER_OF_TWITTER_RESULTS_TO_FETCH = "25";
	private static final String MAX_ID_KEY = "max_id";
	private static final String COUNT_REQUEST_PARAM_KEY = "count";
	private static final int COMPOSE_NEW_ACTIVITY_REQUEST_CODE = 0;
	private static User currentUser;
	private long maxId;
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	private TweetsAdapter adapter;
	private boolean loadOffline;
	PullToRefreshListView lvTweets;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		loadOffline = Boolean.valueOf(this.getIntent().getStringExtra(LOAD_OFFLINE_KEY));
		Toast.makeText(getApplicationContext(), "loadOffline = "+loadOffline , Toast.LENGTH_LONG).show();
		
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		adapter = new TweetsAdapter(getBaseContext(), tweets);
		lvTweets.setAdapter(adapter);
		
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list contents
                // Make sure you call listView.onRefreshComplete()
                // once the loading is done. This can be done from here or any
                // place such as when the network request has completed successfully.
            	if(loadOffline) {
            		lvTweets.onRefreshComplete();
            		Toast.makeText(getApplicationContext(), "You are currently offline due to internet connectivity problem!", Toast.LENGTH_LONG).show();
            	}
            	else {
            		performInitialLoad();
            	}
            }
        });
		
		if(loadOffline) {
			tweets = new Select().from(Tweet.class).execute();
			identifyMaxId(tweets);
			removeDuplicateTweet(tweets);
			adapter.addAll(tweets);
			adapter.notifyDataSetChanged();
		}
		else {
			performInitialLoad();

			TwitterApp.getRestClient().getUserDetails(new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject userJson) {
					TimelineActivity.currentUser = User.fromJson(userJson);
				}
			});

		}
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				RequestParams requestParams = new RequestParams();
				requestParams.put(MAX_ID_KEY, ""+maxId);
				requestParams.put(COUNT_REQUEST_PARAM_KEY, DEFAULT_NUMBER_OF_TWITTER_RESULTS_TO_FETCH);
				
				TwitterApp.getRestClient().getHomeTimeLine(new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						moreTweetsAreLoaded(jsonTweets);
					}
				}, requestParams);

			}
		});
	}

	private void performInitialLoad() {
		RequestParams requestParams = new RequestParams();
		requestParams.put(COUNT_REQUEST_PARAM_KEY, DEFAULT_NUMBER_OF_TWITTER_RESULTS_TO_FETCH);
	
		TwitterApp.getRestClient().getHomeTimeLine(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				lvTweets.onRefreshComplete();
				adapter.clear();
				adapter.notifyDataSetChanged();
				moreTweetsAreLoaded(jsonTweets);
			}
		}, requestParams);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	public void composeNewTweet(MenuItem mi) {
		if(loadOffline) {
			Toast.makeText(getApplicationContext(), "You are currently offline due to internet connectivity problem!", Toast.LENGTH_LONG).show();
		}
		else {
			Intent composeIntent = new Intent(this, ComposeNewTweetActivity.class);
			startActivityForResult(composeIntent, COMPOSE_NEW_ACTIVITY_REQUEST_CODE);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == COMPOSE_NEW_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Tweet tweet = (Tweet) data.getSerializableExtra(ComposeNewTweetActivity.NEW_TWEET_KEY);
				adapter.insert(tweet, 0);
				adapter.notifyDataSetChanged();
			}
			else if (resultCode == RESULT_CANCELED) {
				
			}
		}
	}

	private void moreTweetsAreLoaded(JSONArray jsonTweets) {
		tweets = Tweet.fromJson(jsonTweets);
		identifyMaxId(tweets);
		removeDuplicateTweet(tweets);
		adapter.addAll(tweets);
		adapter.notifyDataSetChanged();
	}

//	private void saveLoadedDataToDB() {
//		for (int i = 0; i < tweets.size(); i++) {
//			Tweet singleTweet = tweets.get(i);	
//			singleTweet.save();
//		}
//		
//		User currentUser = getCurrentLoggedInUser();
//		if (currentUser != null) {
//			currentUser.save();
//		}
//		
//	}

	private void removeDuplicateTweet(ArrayList<Tweet> tweets2) {
		if (tweets2 != null && tweets2.size() > 0) {
			int tweetIndexToRemove = 0;
			for (int i = 0; i < tweets2.size(); i++) {
				Tweet singleTweet = tweets2.get(i);	
				if (singleTweet.getTweetId() == this.maxId) {
					tweetIndexToRemove = i;
					break;
				}
			}
			tweets2.remove(tweetIndexToRemove);
		}
	}

	private void identifyMaxId(ArrayList<Tweet> tweetsToParse) {
		if (tweetsToParse != null && tweetsToParse.size()>0) {
			Tweet t = tweetsToParse.get(0);
			if(t != null) {
				maxId = t.getTweetId();
			}
		}
		
		for (int i = 0; i < tweetsToParse.size(); i++) {
			Tweet singleTweet = tweetsToParse.get(i);
			long idToCompare = singleTweet.getTweetId();
			if (maxId > idToCompare) {
				maxId = idToCompare;
			}
		}
	}

	public static User getCurrentLoggedInUser() {
		return TimelineActivity.currentUser;
	}

}
