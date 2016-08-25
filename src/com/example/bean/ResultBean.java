package com.example.bean;

import java.util.List;

public class ResultBean extends BaseBean{
	private List<FaceBean> face;//脸部信息
	private String img_height;
	private String img_id;
	private String img_width;
	private String session_id;
	private String url;
	private String response_code;
	
	public List<FaceBean> getFace() {
		return face;
	}
	public void setFace(List<FaceBean> face) {
		this.face = face;
	}
	public String getImg_height() {
		return img_height;
	}
	public void setImg_height(String img_height) {
		this.img_height = img_height;
	}
	public String getImg_id() {
		return img_id;
	}
	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}
	public String getImg_width() {
		return img_width;
	}
	public void setImg_width(String img_width) {
		this.img_width = img_width;
	}
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getResponse_code() {
		return response_code;
	}
	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}
}
