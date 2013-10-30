package com.codepath.apps.twitterclientapp;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.codepath.apps.twitterclientapp.fragments.HomeTimeLineFragment;
import com.codepath.apps.twitterclientapp.fragments.MentionsFragment;
import com.codepath.apps.twitterclientapp.fragments.TweetsListFragment;
import com.codepath.apps.twitterclientapp.models.Tweet;
import com.codepath.apps.twitterclientapp.models.User;
import com.codepath.apps.twitterclientapp.utils.UserUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;

public class TimelineActivity extends FragmentActivity implements TabListener {

	private static final String HOME_TIMELINE_FRAGMENT_TAG = "HomeTimelineFragment";
	public static final int COMPOSE_NEW_ACTIVITY_REQUEST_CODE = 0;
	public static final String LOAD_OFFLINE_KEY = "loadOffline";
	private boolean loadOffline;
	TweetsListFragment fragmentTweets;
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Request the feature before setting content view
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        
		setContentView(R.layout.activity_timeline);
		loadOffline = Boolean.valueOf(this.getIntent().getStringExtra(LOAD_OFFLINE_KEY));
		
		setupNavigationTabs();

		if (!loadOffline) {
			TwitterApp.getRestClient().getUserDetails(new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject userJson) {
					UserUtils.setCurrentLoggedInUser(User.fromJson(userJson));
				}
			}, null);
		}
	}

	private void setupNavigationTabs() {
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tabHome = actionBar.newTab().setText("Home").setTag(HOME_TIMELINE_FRAGMENT_TAG).setIcon(R.drawable.ic_home)
				.setTabListener(this);
		Tab tabMentions = actionBar.newTab().setText("Mentions").setTag("MentionsTimelineFragment").
				setIcon(R.drawable.ic_mentions).setTabListener(this);
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
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
			actionBar.getTabAt(0).select();
		}
	}
	
	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra(ProfileActivity.USER_PROFILE_OBJECT_KEY, UserUtils.getCurrentLoggedInUser());
		startActivity(i);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == COMPOSE_NEW_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Tweet tweet = (Tweet) data.getSerializableExtra(ComposeNewTweetActivity.NEW_TWEET_KEY);
				fragmentTweets.getAdapter().insert(tweet, 0);
				fragmentTweets.getAdapter().notifyDataSetChanged();
			}
			else if (resultCode == RESULT_CANCELED) {
				
			}
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		 
		if (tab.getTag() == HOME_TIMELINE_FRAGMENT_TAG) {
			//set the fragment in the framelayout to the home timeline
			fragmentTweets = new HomeTimeLineFragment();
			fts.replace(R.id.tab_frame_container, fragmentTweets);
		} else {
			//set the fragment in the framelayout to the mentions timeline
			fragmentTweets = new MentionsFragment();
			fts.replace(R.id.tab_frame_container, fragmentTweets);
		}
		fts.commit();
		
		fragmentTweets.setLoadOffline(loadOffline);

	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void onTweetAuthorClick(View v) 
	{
		PullToRefreshListView lvTweets = (PullToRefreshListView) fragmentTweets.getActivity().findViewById(R.id.lvTweets);
		int selectedPosition = lvTweets.getPositionForView(v);
		TweetsAdapter ta = fragmentTweets.getAdapter();
		if (!ta.isEmpty()) {
			Tweet selectedTweet = ta.getItem(selectedPosition-1);
			Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
			i.putExtra(ProfileActivity.USER_PROFILE_OBJECT_KEY, selectedTweet.getUser());
			startActivity(i);
		}
	}

}
