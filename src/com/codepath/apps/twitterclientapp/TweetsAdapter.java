package com.codepath.apps.twitterclientapp;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclientapp.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet>{
	
	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}
	
	@Override
	public View getView(int position, View converView, ViewGroup parent) {
		View view = converView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item, null);
		}
		
		Tweet tweet = getItem(position);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), imageView);
		TextView nameView = (TextView) view.findViewById(R.id.tvName);
		
		
		
		String formattedName = "<b>" + tweet.getUser().getName() + "</b>" + " <small><font color='#777777'>@" + 
		tweet.getUser().getScreenName() + "</font></small>";
		
		if (tweet.getTimeStamp() != null) {
			String timeStampOfTweet = (String) DateUtils.getRelativeDateTimeString(
			        getContext(), tweet.getTimeStamp().getTime(), // The time to display
			        DateUtils.SECOND_IN_MILLIS, // The resolution. This will display only minutes
			        DateUtils.WEEK_IN_MILLIS, // The maximum resolution at which the time will switch 
			        DateUtils.FORMAT_ABBREV_ALL );
			formattedName += " <small><font color='#777777'>"+timeStampOfTweet+"</font></small>";
		}
		
		nameView.setText(Html.fromHtml(formattedName));
		
		TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
		bodyView.setText(Html.fromHtml(tweet.getBodyText()));
		
		return view;
		
		
	}

}
