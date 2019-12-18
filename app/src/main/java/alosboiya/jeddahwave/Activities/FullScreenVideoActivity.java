package alosboiya.jeddahwave.Activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import alosboiya.jeddahwave.R;

public class FullScreenVideoActivity extends AppCompatActivity {

    VideoView videoView;

    String url;

    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen);

        videoView = findViewById(R.id.videoview);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            url = extras.getString("url");
        }

        MediaController mediaController = new MediaController(this);
        videoView.setVideoURI(Uri.parse(url));
        videoView.requestFocus();
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.seekTo(100);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
}
