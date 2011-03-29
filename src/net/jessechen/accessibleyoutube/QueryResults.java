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
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class QueryResults extends Activity {
	
	private static NodeList nl;
	private ListView lv;
	private HashMap<String, SearchResult> h;
	private String query;
	private TextView tv;
	private Bundle b;
    private ArrayList<ListItem> results;
    private mAdapter m_adapter; 
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.queryresults);
         
        b = getIntent().getExtras();
        query = (String) b.getCharSequence("query");
        
        
        results = new ArrayList<ListItem>();
        m_adapter = new mAdapter(this, R.layout.list_item, results);
        lv = (ListView) findViewById(R.id.list);
        
        h = new HashMap<String, SearchResult>();
		
        tv = (TextView) findViewById(R.id.ResultsTitle);
     
	    getResults();
	    
        if (results.isEmpty()) {
        	TextView t = new TextView(this);
        	t.setText("No results for " + query);
        	t.setTextSize(20);
        	setContentView(t);
        }
        
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		SearchResult l = h.get(((TextView) view.findViewById(R.id.listitem)).getText());
        		Intent i = new Intent(QueryResults.this, Result.class);
        		i.putExtra("videotitle", l.getTitle());
        		i.putExtra("videourl", l.getVideoUrl());
        		i.putExtra("thumbnailurl", l.getThumbnailUrl());
        		i.putExtra("description", l.getDescription());
        		i.putExtra("username", l.getUsername());
        		startActivity(i);
        	}
        }); 
        lv.setAdapter(m_adapter); 
    }
    
	private void getResults() {
        try {
			URL url;
			String encodedQuery;
			if (query == null) { // means we are pulling a specific user's videos that has captions
				query = (String) b.getCharSequence("username");
				encodedQuery = URLEncoder.encode(query);
				url = new URL("http://gdata.youtube.com/feeds/api/videos?author=" + encodedQuery + "&caption&v=2");
		        tv.setText(query + "'s Channel");
			} else { // normal operation, search for youtube videos matching query && has captions
				encodedQuery = URLEncoder.encode(query);
				url = new URL("http://gdata.youtube.com/feeds/api/videos?q=" + encodedQuery + "&caption&v=2");
		        tv.setText("Results for \"" + query + "\"");
			}

        	URLConnection connection = url.openConnection();
        	
        	HttpURLConnection httpConnection = (HttpURLConnection) connection;
        	
        	int responseCode = httpConnection.getResponseCode();
        	
        	if (responseCode == HttpURLConnection.HTTP_OK); {
        		InputStream in = httpConnection.getInputStream();
        		
        		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        		DocumentBuilder db = dbf.newDocumentBuilder();
        		
        		Document dom = db.parse(in);
        		Element docEle = dom.getDocumentElement();
        		
        		nl = docEle.getElementsByTagName("entry");
        		if (nl != null && nl.getLength() > 0) {
        			for (int i = 0; i < nl.getLength(); i++) {
        				Element entry = (Element) nl.item(i);
        				Element title = (Element) entry.getElementsByTagName("title").item(0);
        				Element id = (Element) entry.getElementsByTagName("id").item(0);
        				Element media = (Element) entry.getElementsByTagName("media:group").item(0);
        				Element tn = (Element) media.getElementsByTagName("media:thumbnail").item(0);
        				Element dp = (Element) media.getElementsByTagName("media:description").item(0);
        				Element author = (Element) entry.getElementsByTagName("author").item(0);
        				Element name = (Element) author.getElementsByTagName("name").item(0);
        				
        				String channelName = name.getFirstChild().getNodeValue();
        				String description = dp.getFirstChild().getNodeValue();
        				String thumbnailUrl = tn.getAttribute("url");
        				String videoId = id.getFirstChild().getNodeValue().split(":")[3]; // grabs unique video ID from entry->id
        				String titleString = title.getFirstChild().getNodeValue();
        				SearchResult sr = new SearchResult(titleString, videoId, thumbnailUrl, description, channelName); // custom class to store YouTube links
        				h.put(titleString, sr); // store in a HashMap to look up later
        				results.add(new ListItem(titleString, sr.getVideoUrl(), thumbnailUrl)); // only add title to the ArrayList
        			}
        		}
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}	
	
	protected static Object fetch(String address) throws IOException, MalformedURLException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}
	   
    protected class mAdapter extends ArrayAdapter<ListItem> {
    	private ArrayList<ListItem> items;
    	
    	public mAdapter(Context context, int textViewResourceId, ArrayList<ListItem> items) {
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
    			ImageView img = (ImageView) v.findViewById(R.id.listpic);
    			
				t.setText(l.getTitle());
				InputStream is;
				try {
					is = (InputStream) fetch(l.getThumbnailURL());

					Drawable d = Drawable.createFromStream(is, "src");
					img.setImageDrawable(d);
					img.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(l.getVideoURL())));
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
    		return v;
    	}
    }
}
