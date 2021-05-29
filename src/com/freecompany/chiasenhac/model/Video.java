package com.freecompany.chiasenhac.model;

public class Video {
	String title;
	String singer;
	// String views;
	String type;
	String url;
	String imgurl;

	public Video(String title, String singer, String type, String url,
			String imgurl) {
		super();
		this.title = title;
		this.singer = singer;
		this.type = type;
		this.url = url;
		this.imgurl = imgurl;
	}

	public Video() {
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

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

}
