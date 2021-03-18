package com.project.selsal.entities;

import org.springframework.stereotype.Component;


@Component
public class Freeboard {
	private int f_seq;
	private String f_ref;
	private String f_step;
	private String f_email;
	private String f_name;
	private String f_title;
	private String f_content;
	private int f_hit;
	private String f_attach;
	private int f_like;
	private String f_inputtime;
	private String f_sessionemail;
	
	public int getF_seq() {
		return f_seq;
	}
	public void setF_seq(int f_seq) {
		this.f_seq = f_seq;
	}
	public String getF_ref() {
		return f_ref;
	}
	public void setF_ref(String f_ref) {
		this.f_ref = f_ref;
	}
	public String getF_step() {
		return f_step;
	}
	public void setF_step(String f_step) {
		this.f_step = f_step;
	}
	public String getF_email() {
		return f_email;
	}
	public void setF_email(String f_email) {
		this.f_email = f_email;
	}
	public String getF_name() {
		return f_name;
	}
	public void setF_name(String f_name) {
		this.f_name = f_name;
	}
	public String getF_title() {
		return f_title;
	}
	public void setF_title(String f_title) {
		this.f_title = f_title;
	}
	public String getF_content() {
		return f_content;
	}
	public void setF_content(String f_content) {
		this.f_content = f_content;
	}
	public int getF_hit() {
		return f_hit;
	}
	public void setF_hit(int f_hit) {
		this.f_hit = f_hit;
	}
	public String getF_attach() {
		return f_attach;
	}
	public void setF_attach(String f_attach) {
		this.f_attach = f_attach;
	}
	public int getF_like() {
		return f_like;
	}
	public void setF_like(int f_like) {
		this.f_like = f_like;
	}
	public String getF_inputtime() {
		return f_inputtime;
	}
	public void setF_inputtime(String f_inputtime) {
		this.f_inputtime = f_inputtime;
	}
	public String getF_sessionemail() {
		return f_sessionemail;
	}
	public void setF_sessionemail(String f_sessionemail) {
		this.f_sessionemail = f_sessionemail;
	}
	
	
}