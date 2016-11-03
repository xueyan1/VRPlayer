package com.vstar3D.VRPlayer.bitmaputils;

import android.view.View;

public interface IImageViewerListener
{
    public void onImageSelectedChanged(View v, int position, MediaInfo media);
    public void onSearchCompleted(int nCount);
}
