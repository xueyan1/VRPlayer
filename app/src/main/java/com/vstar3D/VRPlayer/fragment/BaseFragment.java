package com.vstar3D.VRPlayer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2016/5/21.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract boolean OnkeyStaus();


}
