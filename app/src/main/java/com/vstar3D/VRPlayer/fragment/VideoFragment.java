package com.vstar3D.VRPlayer.fragment;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vstar3D.VRPlayer.R;
import com.vstar3D.VRPlayer.adapter.VideoFragmentAdapter;
import com.vstar3D.VRPlayer.bitmaputils.TosImageViewer;
import com.vstar3D.VRPlayer.model.Videos;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@SuppressLint("NewApi")
public class VideoFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private TosImageViewer mGridView;
    private TextView mTextView,textview;
    private ProgressBar mProgressBar;
    private VideoFragmentAdapter mAdapter;
    private List<Videos> mDate = new ArrayList<Videos>();
    private Videos video;
    private int fileNum;
    private final String local_path = Environment.getExternalStorageDirectory() + "";

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mGridView.setVisibility(View.GONE);
                mTextView.setVisibility(View.VISIBLE);}
                if (msg.what==2){
                    mAdapter.addView((Videos) msg.obj);
                    mGridView.setAdapter(mAdapter);
                    mGridView.setVisibility(View.VISIBLE);
                    mTextView.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                    textview.setVisibility(View.GONE);
                }
                super.handleMessage(msg);
        }
    };

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_video, container, false);
        mGridView = (TosImageViewer) mView.findViewById(R.id.fragment_video_gridview);

        mTextView = (TextView) mView.findViewById(R.id.fragment_video_textview);
        textview = (TextView) mView.findViewById(R.id.textview);

        mProgressBar = (ProgressBar) mView.findViewById(R.id.progressBar);
        mTextView.setOnClickListener(this);
        mAdapter = new VideoFragmentAdapter(getContext(), mDate);
        getDate();
        return mView;
    }
    private List<Videos> getVideoFile(File file) {
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String name = file.getName();
                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp4")
                            || name.equalsIgnoreCase(".3gp")
                            || name.equalsIgnoreCase(".3dv")
                            || name.equalsIgnoreCase(".avi")
                            || name.equalsIgnoreCase(".rmvb")
                            || name.equalsIgnoreCase(".flv")
                            || name.equalsIgnoreCase(".wmv")
                            || name.equalsIgnoreCase(".rm")
                            || name.equalsIgnoreCase(".mkv")
                            || name.equalsIgnoreCase(".swf")
                            || name.equalsIgnoreCase(".mov")
                            || name.equalsIgnoreCase(".vob")
                            ) {
                        Videos video = new Videos();
                        video.vName = file.getName();
                        video.vPath = file.getAbsolutePath();
                        video.bitmap = getVideoThumbnail(String.valueOf(file), 400, 300, MediaStore.Images.Thumbnails.MICRO_KIND);
                        Message msg = mHandler.obtainMessage();
                        msg.what = 2;
                        msg.obj=video;
                        msg.sendToTarget();
                        return true;
                    }
                } else if (file.isDirectory()) {
                    getVideoFile(file);
                }
                return false;
            }

        });
        return mDate;
    }

    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


    @Override
    public void onClick(View v) {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                mDate = getVideoFile(new File(local_path));
                fileNum = mDate.size();

                if (mDate.size() == 0) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    msg.sendToTarget();
                } else {
                    Message msg = mHandler.obtainMessage();
                    msg.what = 2;
                    msg.sendToTarget();
                }
            }
        });
        thread.start();
    }
public  void  getDate(){
    mProgressBar.setVisibility(View.VISIBLE);
    textview.setVisibility(View.VISIBLE);
    mTextView.setVisibility(View.GONE);
    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            mDate = getVideoFile(new File(local_path));
            fileNum = mDate.size();
            mAdapter = new VideoFragmentAdapter(getContext(), mDate);
            if (mDate.size() == 0) {
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.sendToTarget();
            } else {
                Message msg = mHandler.obtainMessage();
                msg.what = 2;
                msg.sendToTarget();
            }
        }
    });
    thread.start();
}
    long  time2;
    @Override
    public boolean OnkeyStaus() {
            if((System.currentTimeMillis()-time2) > 2000){
                Toast.makeText(getContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                time2 = System.currentTimeMillis();
            } else {
                getActivity().finish();
                System.exit(0);
            }
        return true;
    }
}
