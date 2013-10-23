package com.codepath.apps.twitterclientapp.models;

import java.io.Serializable;

import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Users")
public class User extends Model implements Serializable {
	
	private static final long serialVersionUID = 4163248689148731203L;
	
	@Column(name = "Name")
	private String name;
	
	@Column(name = "UserId")
	private long userId;
	
	@Column(name = "ScreenName")
	private String screenName;
	
	@Column(name = "ProfileImageUrl")
	private String profileImageUrl;
	
	@Column(name = "ProfileBackgroundImageUrl")
	private String profileBackgroundImageUrl;
	
	@Column(name = "NumTweets")
	private int numTweets;
	
	@Column(name = "FollowersCount")
	private int followersCount;
	
	@Column(name = "FriendsCount")
	private int friendsCount;

	// Make sure to define this constructor (with no arguments)
	// If you don't, querying will fail to return results!
	public User() {
		super();
	}
	
	
	public User(String name, long id, String screenName,
			String profileImageUrl, String profileBackgroundImageUrl,
			int numTweets, int followersCount, int friendsCount) {
		super();
		this.name = name;
		this.userId = id;
		this.screenName = screenName;
		this.profileImageUrl = profileImageUrl;
		this.profileBackgroundImageUrl = profileBackgroundImageUrl;
		this.numTweets = numTweets;
		this.followersCount = followersCount;
		this.friendsCount = friendsCount;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	

	public long getUserId() {
		return userId;
	}


	public void setUserId(long userId) {
		this.userId = userId;
	}


	public String getScreenName() {
		return screenName;
	}


	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}


	public String getProfileImageUrl() {
		return profileImageUrl;
	}


	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}


	public String getProfileBackgroundImageUrl() {
		return profileBackgroundImageUrl;
	}


	public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
		this.profileBackgroundImageUrl = profileBackgroundImageUrl;
	}


	public int getNumTweets() {
		return numTweets;
	}


	public void setNumTweets(int numTweets) {
		this.numTweets = numTweets;
	}


	public int getFollowersCount() {
		return followersCount;
	}


	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}


	public int getFriendsCount() {
		return friendsCount;
	}


	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public static User fromJson(JSONObject json) {
		User u = new User();
		try {
			u.userId = json.getLong("id");
			u.name = json.getString("name");
			u.screenName = json.getString("screen_name");
			u.profileImageUrl = json.getString("profile_image_url");
			u.profileBackgroundImageUrl = json.getString("profile_background_image_url");
			u.numTweets = json.getInt("statuses_count");
			u.followersCount = json.getInt("followers_count");
			u.friendsCount = json.getInt("friends_count");
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		u.save();
		return u;
	}


}