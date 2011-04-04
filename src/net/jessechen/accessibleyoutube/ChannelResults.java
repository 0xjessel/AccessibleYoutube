package net.jessechen.accessibleyoutube;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChannelResults extends Activity {
	private NodeList nl;
	private String query;
	private ArrayList<ListItem> results;
	private ListView lv;
	private HashMap<String, SearchResult> h;
	private TextView tv;
	private mAdapter m_adapter;
	private Runnable searchResults;
	private ProgressDialog m_ProgressDialog = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.channelresults);

		query = (String) getIntent().getCharSequenceExtra("query");

		results = new ArrayList<ListItem>();
		m_adapter = new mAdapter(ChannelResults.this, R.layout.list_item,
				results);
		lv = (ListView) findViewById(R.id.channellist);
		lv.setAdapter(m_adapter);

		h = new HashMap<String, SearchResult>();

		tv = (TextView) findViewById(R.id.channelresultstitle);

		searchResults = new Runnable() {

			@Override
			public void run() {
				getResults();
			}
		};
		Thread thread = new Thread(null, searchResults, "MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(ChannelResults.this,
				"Please wait...", "Retrieving data...", true);
	}

	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (results != null && results.size() > 0) {
				m_adapter.notifyDataSetChanged();
			}
			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
		}
	};

	private void getResults() {
		try {
			URL url = new URL("http://gdata.youtube.com/feeds/api/channels?q="
					+ query + "&v=2");
			String encodedQuery = URLEncoder.encode(query);
			tv.setText("Channel Results for \"" + encodedQuery + "\"");

			URLConnection connection = url.openConnection();

			HttpURLConnection httpConnection = (HttpURLConnection) connection;

			int responseCode = httpConnection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream in = httpConnection.getInputStream();

				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();

				Document dom = db.parse(in);
				Element docEle = dom.getDocumentElement();

				nl = docEle.getElementsByTagName("entry");
				if (nl != null && nl.getLength() > 0) {
					for (int i = 0; i < nl.getLength(); i++) {
						Element entry = (Element) nl.item(i);
						Element title = (Element) entry.getElementsByTagName(
								"title").item(0);
						Element summary = (Element) entry.getElementsByTagName("summary").item(0);
						Element feedLink = (Element) entry.getElementsByTagName("gd:feedlink").item(0);
						
						String summaryString = summary.getFirstChild().getNodeValue();
						String feedUrl = feedLink.getAttribute("href");
						String titleString = title.getFirstChild()
								.getNodeValue();
						SearchResult sr = new SearchResult(titleString, summaryString, feedUrl);
						h.put(titleString, sr); // store for lookup
						results.add(new ListItem(titleString, null, null));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SearchResult l = h.get(((TextView) view
						.findViewById(R.id.listitem)).getText());
				Intent i = new Intent(ChannelResults.this, Result.class);
				i.putExtra("channeltitle", l.getTitle());
				i.putExtra("channelsummary", l.getSummary());
				i.putExtra("feedurl", l.getFeedUrl());
				startActivity(i);
			}
		});
		runOnUiThread(returnRes);
	}

	protected class mAdapter extends ArrayAdapter<ListItem> {
		private ArrayList<ListItem> items;

		public mAdapter(Context context, int textViewResourceId,
				ArrayList<ListItem> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_item, null);
			}
			final ListItem l = items.get(position);
			if (l != null) {
				TextView t = (TextView) v.findViewById(R.id.listitem);

				t.setText(l.getTitle());
			}
			return v;
		}
	}

	protected static Object fetch(String address) throws IOException,
			MalformedURLException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}
}
