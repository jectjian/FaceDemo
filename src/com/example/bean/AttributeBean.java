package com.example.bean;

public class AttributeBean extends BaseBean{
	private AgeBean age;
	private GenderBean gender;
	private RaceBean race;
	private SmilingBean smiling;
	
	public AgeBean getAge() {
		return age;
	}
	public void setAge(AgeBean age) {
		this.age = age;
	}
	public GenderBean getGender() {
		return gender;
	}
	public void setGender(GenderBean gender) {
		this.gender = gender;
	}
	public RaceBean getRace() {
		return race;
	}
	public void setRace(RaceBean race) {
		this.race = race;
	}
	public SmilingBean getSmiling() {
		return smiling;
	}
	public void setSmiling(SmilingBean smiling) {
		this.smiling = smiling;
	}
}
