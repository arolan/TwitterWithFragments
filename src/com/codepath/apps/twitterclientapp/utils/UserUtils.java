package com.codepath.apps.twitterclientapp.utils;

import com.codepath.apps.twitterclientapp.models.User;

public class UserUtils {
	
	private static User currentLoggedInUser;

	public static User getCurrentLoggedInUser() {
		return currentLoggedInUser;
	}

	public static void setCurrentLoggedInUser(User currentLoggedInUser) {
		UserUtils.currentLoggedInUser = currentLoggedInUser;
	}

}
