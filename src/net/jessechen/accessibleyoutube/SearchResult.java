package net.jessechen.accessibleyoutube;

public class SearchResult {
	private String title, videoURL, thumbnailUrl, description, username;
	public SearchResult(String titleString, String id, String thumbnailUrl, String description, String username) {
		title = titleString;
		videoURL = "http://www.youtube.com/watch?v=" + id;
		this.thumbnailUrl = thumbnailUrl;
		this.description = description;
		this.username = username;
	}
	
	public SearchResult(String titleString, String thumbnailUrl2) {
		title = titleString;
		this.thumbnailUrl = thumbnailUrl2;
	}

	public String getTitle() {
		return title;
	}
	public String getVideoUrl() {
		return videoURL;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public String getDescription() {
		return description;
	}
	public String getUsername() {
		return username;
	}
}
