package com.codepath.apps.twitterclientapp.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Tweets")
public class Tweet extends Model implements Serializable  {
	
	
	public static final String EEE_MMM_D_HH_MM_SS_Z_YYYY = "EEE MMM d HH:mm:ss Z yyyy";
	/**
	 * 
	 */
	private static final long serialVersionUID = -417201535553824684L;
	
	@Column(name = "User")
	private User user;
	
	@Column(name = "BodyText")
	private String bodyText;
	
	@Column(name = "TweetId")
	private long tweetId;
	
	@Column(name = "Favourited")
	private boolean favourited;
	
	@Column(name = "Retweeted")
	private boolean retweeted;
	
	@Column(name = "TimeStamp")
	private Date timeStamp;
	
	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getBodyText() {
		return this.bodyText;
	}

	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}

	
	public boolean isFavourited() {
		return this.favourited;
	}

	public void setFavourited(boolean favourited) {
		this.favourited = favourited;
	}

	public boolean isRetweeted() {
		return this.retweeted;
	}

	public void setRetweeted(boolean retweeted) {
		this.retweeted = retweeted;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	public long getTweetId() {
		return tweetId;
	}

	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}

	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		try {
			
			tweet.bodyText = jsonObject.getString("text");
			tweet.tweetId = jsonObject.getLong("id");
			tweet.favourited = jsonObject.getBoolean("favorited");
			tweet.retweeted = jsonObject.getBoolean("retweeted");
			
			String createdAtDate = jsonObject.getString("created_at");
			if(createdAtDate != null) {
				DateFormat df = new SimpleDateFormat(EEE_MMM_D_HH_MM_SS_Z_YYYY);
				tweet.timeStamp = df.parse(createdAtDate);
				Log.d("DEBUG", "timeStamp = " + tweet.timeStamp.toString());
			}
			
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		tweet.save();
		return tweet;
	}
	
	public static ArrayList<Tweet> fromJson(JSONArray jsonArrray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArrray.length());
		for (int i = 0; i < jsonArrray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArrray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			Tweet tweet = Tweet.fromJson(tweetJson);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}
		return tweets;
	}
}
