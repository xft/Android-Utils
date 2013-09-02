package com.exercise.utils;

import android.content.Context;
import android.support.v4.app.Fragment;

public class FuncGroup {
	public String title;
	public int icon;
	public String summary;
	public Fragment fragment;
	
	public FuncGroup(String title, String summary, int icon, Fragment fragment) {
		this.title = title;
		this.summary = summary;
		this.icon = icon;
		this.fragment = fragment;
	}
	
	public FuncGroup(Context context, int titleResId, int summaryResId, int icon, Fragment fragment) {
		this.title = context.getString(titleResId);
		this.summary = context.getString(summaryResId);
		this.icon = icon;
		this.fragment = fragment;
	}
}
