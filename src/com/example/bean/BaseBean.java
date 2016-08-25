package com.example.bean;

import java.io.Serializable;

import com.google.gson.Gson;

public class BaseBean implements Serializable {
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
