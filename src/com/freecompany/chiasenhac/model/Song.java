package com.freecompany.chiasenhac.model;

public class Song {
	String title;
	String singer;
	// String views;
	String time;
	String type;
	String url;

	public Song(String title, String singer, String time, String type,
			String url) {
		super();
		this.title = title;
		this.singer = singer;
		// this.views = views;
		this.time = time;
		this.type = type;
		this.url = url;
	}

	public Song() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
