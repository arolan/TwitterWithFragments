package com.codepath.apps.twitterclientapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclientapp.fragments.UserTimelineFragment;
import com.codepath.apps.twitterclientapp.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	public static final String USER_PROFILE_OBJECT_KEY = "USER_PROFILE_OBJECT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Request the feature before setting content view
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_profile);
		User u = (User) getIntent().getSerializableExtra(USER_PROFILE_OBJECT_KEY);
		populateProfileHeader(u);
		
		FragmentManager manager = getSupportFragmentManager();
		UserTimelineFragment utf = (UserTimelineFragment) manager.findFragmentById(R.id.fragmentUserTimeLine);
		utf.setUserId(u.getUserId());
		
	}

	private void populateProfileHeader(User u) {
		getActionBar().setTitle("@"+u.getScreenName());
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		tvName.setText(u.getName());
		tvTagline.setText(u.getTagLine());
		tvFollowers.setText(u.getFollowersCount() + " Followers");
		tvFollowing.setText(u.getFriendsCount() + " Following");
		//load profile image
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}
	
	public void onTweetAuthorClick(View v) {
		//We leave it empty. For now, we do not allow explicitly navigation to itself.
		//the reason we put this is that we have reusable fragment_tweets_list used here (with 
		//onTweetAuthorClick method defined in xml). Alternatively, I could just
		//define onClick method in TimelineActivity directly.
	}
}
