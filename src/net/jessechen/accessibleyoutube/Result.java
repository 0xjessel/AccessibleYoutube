/*******************************************************************************
 * Authors:
 *     Jesse Chen
 * 
 * Copyright (c) 2011 Jesse Chen.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package net.jessechen.accessibleyoutube;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Result extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.result);

		try {
			String thumbnailUrl = getIntent().getExtras().getString(
					"thumbnailurl");
			final String username = getIntent().getExtras().getString(
					"username");
			String titleString = getIntent().getExtras()
					.getString("videotitle");
			final String videoURL = getIntent().getExtras().getString(
					"videourl");
			String description = getIntent().getExtras().getString(
					"description");

			TextView title = (TextView) findViewById(R.id.resulttitle);
			title.setText(titleString);

			ImageView image = (ImageView) findViewById(R.id.thumbnail);
			InputStream is = (InputStream) fetch(thumbnailUrl);
			Drawable d = Drawable.createFromStream(is, "src");
			image.setImageDrawable(d);
			image.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse(videoURL)));
				}
			});

			Button channelButton = (Button) findViewById(R.id.userchannel);
			channelButton.setText("By " + username);
			channelButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(Result.this, QueryResults.class);
					i.putExtra("username", username);
					startActivity(i);
				}
			});

			TextView descripText = (TextView) findViewById(R.id.description);
			descripText.setText(description);

			final RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingbar);
			ratingbar
					.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
						public void onRatingChanged(RatingBar ratingBar,
								float rating, boolean fromUser) {
							Toast.makeText(Result.this,
									"New Rating: " + rating, Toast.LENGTH_SHORT)
									.show();
						}
					});

			Button shareButton = (Button) findViewById(R.id.share);
			shareButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent sharingIntent = new Intent(Intent.ACTION_SEND);
					sharingIntent.setType("text/plain");
					sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
							"Sharing YouTube URL");
					sharingIntent.putExtra(Intent.EXTRA_TEXT, videoURL);
					startActivity(Intent.createChooser(sharingIntent,
							"Share YouTube URL using"));
				}
			});
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private static Object fetch(String address) throws IOException,
			MalformedURLException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

}
