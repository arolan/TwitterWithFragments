package com.codepath.apps.twitterclientapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.codepath.oauth.OAuthLoginActivity;

public class LoginActivity extends OAuthLoginActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
    @Override
    public void onLoginSuccess() {
    	 proceedToTimeLine();
    }

	private void proceedToTimeLine() {
		
    	 Intent i = new Intent(this, TimelineActivity.class);
    	 if(isInternetConnectionAvailable()) {
    		 i.putExtra(TimelineActivity.LOAD_OFFLINE_KEY, "false");
    	 } 
    	 else {
    		 i.putExtra(TimelineActivity.LOAD_OFFLINE_KEY, "true");
    	 }
    	 startActivity(i);
	}
    
    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e) {
    	e.printStackTrace();
    }
    
    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToRest(View view) {
    	if(isInternetConnectionAvailable()) {
    		getClient().connect();
    	}
    	else {
    		proceedToTimeLine();
    	}
    }
    
    public boolean isInternetConnectionAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

}
