package com.vstar3D.VRPlayer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import java.io.IOException;
import com.google.vrtoolkit.cardboard.widgets.video.VrVideoView;
import com.google.vrtoolkit.cardboard.widgets.video.VrVideoEventListener;
public class VPlayerActivity extends Activity {
    private VrVideoView videoWidgetView;
    private boolean loadVideoSuccessful = false;
    private boolean isPaused = false;
    private static final String STATE_IS_PAUSED = "isPaused";
    private static final String STATE_PROGRESS_TIME = "progressTime";
    private static final String STATE_VIDEO_DURATION = "videoDuration";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vplayer);
        videoWidgetView = (VrVideoView) findViewById(R.id.video_view);
        videoWidgetView.isVrMode = true;
        videoWidgetView.isFullScreen = true;
        videoWidgetView.fullscreenBackButton.setVisibility(View.GONE);
        videoWidgetView.setEventListener(new ActivityEventListener());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get("fpath") != null) {
            String path = getIntent().getExtras().getString("fpath", null);
            try {
                videoWidgetView.loadVideo(Uri.parse(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Play Demo!", Toast.LENGTH_SHORT).show();
            try {
                videoWidgetView.loadVideoFromAsset(String.valueOf(R.raw.demo));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private class ActivityEventListener extends VrVideoEventListener {

        @Override
        public void onLoadSuccess() {
            loadVideoSuccessful = true;
        }

        @Override
        public void onLoadError(String errorMessage) {
            loadVideoSuccessful = false;
        }

        @Override
        public void onClick() {
            togglePause();
        }
        @Override
        public void onNewFrame() {
        }

        @Override
        public void onCompletion() {
            videoWidgetView.seekTo(0);
        }
    }
    @Override
    protected void onDestroy() {
        videoWidgetView.shutdown();
        super.onDestroy();
    }

    private void togglePause() {
        if (isPaused) {
            videoWidgetView.playVideo();
        } else {
            videoWidgetView.pauseVideo();
        }
        isPaused = !isPaused;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(STATE_PROGRESS_TIME, videoWidgetView.getCurrentPosition());
        savedInstanceState.putLong(STATE_VIDEO_DURATION, videoWidgetView.getDuration());
        savedInstanceState.putBoolean(STATE_IS_PAUSED, isPaused);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        long progressTime = savedInstanceState.getLong(STATE_PROGRESS_TIME);
        videoWidgetView.seekTo(progressTime);
        isPaused = savedInstanceState.getBoolean(STATE_IS_PAUSED);
        if (isPaused) {
            videoWidgetView.pauseVideo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoWidgetView.pauseRendering();
        isPaused = true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        videoWidgetView.resumeRendering();
    }
}
