package com.codepath.apps.twitterclientapp.utils;

import java.util.ArrayList;

import com.codepath.apps.twitterclientapp.models.Tweet;

public class TweetUtility {
	
	public static void removeDuplicateTweet(ArrayList<Tweet> tweets2, long maxId) {
		if (tweets2 != null && tweets2.size() > 0) {
			int tweetIndexToRemove = 0;
			for (int i = 0; i < tweets2.size(); i++) {
				Tweet singleTweet = tweets2.get(i);	
				if (singleTweet.getTweetId() == maxId) {
					tweetIndexToRemove = i;
					break;
				}
			}
			tweets2.remove(tweetIndexToRemove);
		}
	}

}
