package com.example.bean;

public class PositionBean extends BaseBean{
	private CenterBean center;
	private EyeLeftBean eye_left;
	private EyeRightBean eye_right;
	private float height;
	private MouthLeftBean mouth_left;
	private MouthRightBean mouth_right;
	private NoseBean nose;
	private float width;
	
	public CenterBean getCenter() {
		return center;
	}
	public void setCenter(CenterBean center) {
		this.center = center;
	}
	public EyeLeftBean getEye_left() {
		return eye_left;
	}
	public void setEye_left(EyeLeftBean eye_left) {
		this.eye_left = eye_left;
	}
	public EyeRightBean getEye_right() {
		return eye_right;
	}
	public void setEye_right(EyeRightBean eye_right) {
		this.eye_right = eye_right;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public MouthLeftBean getMouth_left() {
		return mouth_left;
	}
	public void setMouth_left(MouthLeftBean mouth_left) {
		this.mouth_left = mouth_left;
	}
	public MouthRightBean getMouth_right() {
		return mouth_right;
	}
	public void setMouth_right(MouthRightBean mouth_right) {
		this.mouth_right = mouth_right;
	}
	public NoseBean getNose() {
		return nose;
	}
	public void setNose(NoseBean nose) {
		this.nose = nose;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
}
