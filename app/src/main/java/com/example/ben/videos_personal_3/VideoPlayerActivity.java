package com.example.ben.videos_personal_3;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.widget.VideoView;

public class VideoPlayerActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_player);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        String videoPath = getIntent().getStringExtra("str1");
        final VideoView videoView = (VideoView) findViewById(R.id.videoView1);
        videoView.setVideoPath(videoPath);

        android.widget.MediaController mediaController = new android.widget.MediaController(this, true);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setKeepScreenOn(true);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setKeepScreenOn(false);
            }
        });
        videoView.start();
    }
}
