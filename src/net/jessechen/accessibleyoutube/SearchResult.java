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

public class SearchResult {
	private String title, videoURL, thumbnailUrl, description, username, summary, feedURL;

	public SearchResult(String titleString, String id, String thumbnailUrl,
			String description, String username) {
		title = titleString;
		videoURL = "http://www.youtube.com/watch?v=" + id;
		this.thumbnailUrl = thumbnailUrl;
		this.description = description;
		this.username = username;
	}

	public SearchResult(String titleString, String summ, String feed) {
		title = titleString;
		summary = summ;
		feedURL = feed;
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
	
	public String getSummary() {
		return summary;
	}
	
	public String getFeedUrl() {
		return feedURL;
	}
}
