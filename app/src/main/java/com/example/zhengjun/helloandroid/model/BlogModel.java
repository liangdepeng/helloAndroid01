package com.example.zhengjun.helloandroid.model;

import java.util.List;

public class BlogModel {
	private String user_photo_imgpath;
	private String user_name;
	private String comment;
	private String publish_time;
	private String praise_info;
	private List<MarketModel> commentsImg;
	private String commentID;
	public String getCommentID() {
		return commentID;
	}
	public void setCommentID(String commentID) {
		this.commentID = commentID;
	}
	public String getUser_photo_imgpath() {
		return user_photo_imgpath;
	}
	public void setUser_photo_imgpath(String user_photo_imgpath) {
		this.user_photo_imgpath = user_photo_imgpath;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getPublish_time() {
		return publish_time;
	}
	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}
	public String getPraise_info() {
		return praise_info;
	}
	public void setPraise_info(String praise_info) {
		this.praise_info = praise_info;
	}
	public List<MarketModel> getCommentsImg() {
		return commentsImg;
	}
	public void setCommentsImg(List<MarketModel> commentsImg) {
		this.commentsImg = commentsImg;
	}
	
}
