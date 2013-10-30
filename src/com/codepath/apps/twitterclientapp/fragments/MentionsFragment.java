package com.codepath.apps.twitterclientapp.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.widget.Toast;

import com.codepath.apps.twitterclientapp.TwitterApp;
import com.codepath.apps.twitterclientapp.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class MentionsFragment extends TweetsListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list contents
                // Make sure you call listView.onRefreshComplete()
                // once the loading is done. This can be done from here or any
                // place such as when the network request has completed successfully.
            	if(loadOffline) {
            		lvTweets.onRefreshComplete();
            		Toast.makeText(getActivity(), "You are currently offline due to internet connectivity problem!", Toast.LENGTH_LONG).show();
            	}
            	else {
            		performInitialLoad();
            	}
            }
        });
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				RequestParams requestParams = new RequestParams();
				requestParams.put(MAX_ID_KEY, ""+maxId);
				requestParams.put(COUNT_REQUEST_PARAM_KEY, DEFAULT_NUMBER_OF_TWITTER_RESULTS_TO_FETCH);
				getActivity().setProgressBarIndeterminateVisibility(true);
				TwitterApp.getRestClient().getMentions(new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						getActivity().setProgressBarIndeterminateVisibility(false);
						moreTweetsAreLoaded(jsonTweets);
					}
				}, requestParams);

			}
		});	
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Toast.makeText(getActivity(), "loadOffline = "+loadOffline , Toast.LENGTH_LONG).show();
		
		
	}
	
	@Override
	protected void performInitialLoad() {
		RequestParams requestParams = new RequestParams();
		requestParams.put(COUNT_REQUEST_PARAM_KEY, DEFAULT_NUMBER_OF_TWITTER_RESULTS_TO_FETCH);
		getActivity().setProgressBarIndeterminateVisibility(true);
		TwitterApp.getRestClient().getMentions(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				getActivity().setProgressBarIndeterminateVisibility(false);
				lvTweets.onRefreshComplete();
				getAdapter().clear();
				getAdapter().notifyDataSetChanged();
				moreTweetsAreLoaded(jsonTweets);
			}
		}, requestParams);
	}

}
