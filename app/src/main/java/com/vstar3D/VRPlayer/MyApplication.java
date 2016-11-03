package com.vstar3D.VRPlayer;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		context = this.getApplicationContext();
	}

	public static Context context;
}
