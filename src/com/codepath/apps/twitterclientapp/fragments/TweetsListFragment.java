package com.codepath.apps.twitterclientapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.activeandroid.query.Select;
import com.codepath.apps.twitterclientapp.ProfileActivity;
import com.codepath.apps.twitterclientapp.R;
import com.codepath.apps.twitterclientapp.TweetsAdapter;
import com.codepath.apps.twitterclientapp.models.Tweet;
import com.codepath.apps.twitterclientapp.utils.TweetUtility;
import com.codepath.apps.twitterclientapp.utils.UserUtils;

import eu.erikw.PullToRefreshListView;

public class TweetsListFragment extends Fragment {
	
	public static final String DEFAULT_NUMBER_OF_TWITTER_RESULTS_TO_FETCH = "25";
	public static final String MAX_ID_KEY = "max_id";
	public static final String COUNT_REQUEST_PARAM_KEY = "count";
	
	protected TweetsAdapter adapter;
	protected PullToRefreshListView lvTweets;
	protected ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	protected long maxId;
	protected boolean loadOffline;
	
	
	public boolean isLoadOffline() {
		return loadOffline;
	}

	public void setLoadOffline(boolean loadOffline) {
		this.loadOffline = loadOffline;
	}

	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragment_tweets_list, parent, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		lvTweets = (PullToRefreshListView) getActivity().findViewById(R.id.lvTweets);
		setAdapter(new TweetsAdapter(getActivity(), tweets));
		lvTweets.setAdapter(getAdapter());
		
		if(loadOffline) {
			tweets = new Select().from(Tweet.class).execute();
			identifyMaxId(tweets);
			TweetUtility.removeDuplicateTweet(tweets, maxId);
			getAdapter().addAll(tweets);
			getAdapter().notifyDataSetChanged();
		}
		else {
			this.performInitialLoad();
		}
	}
	
	protected void performInitialLoad() {
		
	}
	
	public TweetsAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(TweetsAdapter tweetsAdapter) {
		this.adapter = tweetsAdapter;
	}
	
	protected void moreTweetsAreLoaded(JSONArray jsonTweets) {
		tweets = Tweet.fromJson(jsonTweets);
		identifyMaxId(tweets);
		TweetUtility.removeDuplicateTweet(tweets, maxId);
		getAdapter().addAll(tweets);
		getAdapter().notifyDataSetChanged();
	}

	protected void identifyMaxId(ArrayList<Tweet> tweetsToParse) {
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

}
