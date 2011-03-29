package net.jessechen.accessibleyoutube;

public class ListItem {

	private String title, videoURL, thumbnailUrl;
	
	public ListItem(String t, String url, String thumbUrl) {
		title = t;
		videoURL = url;
		thumbnailUrl = thumbUrl;
	}
	
	public String getTitle() {
		return title;
	}

	public String getVideoURL() {
		return videoURL;
	}
	
	public String getThumbnailURL() {
		return thumbnailUrl;
	}
}
