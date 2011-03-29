package net.jessechen.accessibleyoutube;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class Main extends Activity {
	private EditText QueryText, ChannelText;
	private Intent intent;
	private Bundle b;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        QueryText = (EditText) findViewById(R.id.QueryText);
        
        QueryText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                  // Perform action on key press
                	intent = new Intent(Main.this, QueryResults.class);
                	b = new Bundle();
                	b.putCharSequence("query", QueryText.getText().toString());
                	intent.putExtras(b);
            		startActivity(intent);
            		return true;
                }
                return false;
            }
        });	   
        
        ChannelText = (EditText) findViewById(R.id.ChannelText);
        
        ChannelText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                  // Perform action on key press
                	intent = new Intent(Main.this, ChannelResults.class);
                	intent.putExtra("query", ChannelText.getText().toString());
            		startActivity(intent);
            		return true;
                }
                return false;
            }
        });	
    }
}