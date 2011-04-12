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
