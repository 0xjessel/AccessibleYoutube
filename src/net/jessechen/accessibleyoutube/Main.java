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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Main extends Activity {
	private EditText queryText;
	private Intent intent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		queryText = (EditText) findViewById(R.id.searchtext);

		Button queryButton = (Button) findViewById(R.id.querybutton);
		queryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(Main.this, QueryResults.class);
				intent.putExtra("query", queryText.getText().toString());
				startActivity(intent);
			}
		});

		Button channelButton = (Button) findViewById(R.id.channelbutton);
		channelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(Main.this, ChannelResults.class);
				intent.putExtra("query", queryText.getText().toString());
				startActivity(intent);
			}
		});
	}
}
