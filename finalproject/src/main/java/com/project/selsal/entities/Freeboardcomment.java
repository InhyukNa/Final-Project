package com.project.selsal.entities;

import java.util.Date;

import org.springframework.stereotype.Component;


@Component
public class Freeboardcomment {
	private int comment_num;
	private int f_seq;
	private String comment_name;
	private String comment_date;
	private int comment_parent;
	private String comment_content;
	private String comment_email;
	private Date date;
	
	public int getComment_num() {
		return comment_num;
	}
	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
	}
	public int getF_seq() {
		return f_seq;
	}
	public void setF_seq(int f_seq) {
		this.f_seq = f_seq;
	}
	public String getComment_name() {
		return comment_name;
	}
	public void setComment_name(String comment_name) {
		this.comment_name = comment_name;
	}
	public String getComment_date() {
		return comment_date;
	}
	public void setComment_date(String comment_date) {
		this.comment_date = comment_date;
	}
	public int getComment_parent() {
		return comment_parent;
	}
	public void setComment_parent(int comment_parent) {
		this.comment_parent = comment_parent;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public String getComment_email() {
		return comment_email;
	}
	public void setComment_email(String comment_email) {
		this.comment_email = comment_email;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}