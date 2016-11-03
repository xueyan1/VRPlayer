package com.vstar3D.VRPlayer.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Videos implements Serializable {
	public String vName;
	public String vPath;
	public Bitmap bitmap;

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getvName() {
		return vName;
	}

	public void setvName(String vName) {
		this.vName = vName;
	}

	public String getvPath() {
		return vPath;
	}

	public void setvPath(String vPath) {
		this.vPath = vPath;
	}

	public Videos(String vName, String vPath) {
		super();
		this.vName = vName;
		this.vPath = vPath;
	}

	public Videos() {
	}

}
