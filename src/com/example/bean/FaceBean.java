package com.example.bean;

public class FaceBean extends BaseBean{
	private AttributeBean attribute;
	private String face_id;
	private PositionBean position;
	private String tag;
	
	public AttributeBean getAttribute() {
		return attribute;
	}
	public void setAttribute(AttributeBean attribute) {
		this.attribute = attribute;
	}
	public String getFace_id() {
		return face_id;
	}
	public void setFace_id(String face_id) {
		this.face_id = face_id;
	}
	public PositionBean getPosition() {
		return position;
	}
	public void setPosition(PositionBean position) {
		this.position = position;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
}
