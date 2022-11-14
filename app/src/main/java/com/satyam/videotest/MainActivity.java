package com.satyam.videotest;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class MainActivity extends AppCompatActivity {

    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btFullScreen;
    SimpleExoPlayer simpleExoPlayer;

    boolean flag = false;
    ImageView btQuality;
    boolean isFullScreen = false;
    ImageView imgSpeed;
    TextView speedTXT;

    String[] speed = {"0.25x","0.5x","Normal","1.5x","2x"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.player_view);
        progressBar = findViewById(R.id.progress_bar);
        btFullScreen = playerView.findViewById(R.id.bt_fullscreen);
        btQuality = playerView.findViewById(R.id.bt_quality);
        imgSpeed=playerView.findViewById(R.id.imgSpped);
        speedTXT=playerView.findViewById(R.id.speed);

        //   Uri videoUrl= Uri.parse("https://www.radiantmediaplayer.com/media/big-buck-bunny-360p.mp4");
        //  Uri videoUrl= Uri.parse("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8");
        //   Uri videoUrl= Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4pall.m3u8");

        //Uri videoUrl = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"); //todo running url 1
        Uri videoUrl = Uri.parse("https://jsoncompare.org/LearningContainer/SampleFiles/Video/MP4/sample-mp4-file.mp4"); //todo running url 2



        LoadControl loadControl = new DefaultLoadControl();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(MainActivity.this, trackSelector, loadControl);


        DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoplayer_video");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(videoUrl, factory, extractorsFactory, null, null);



        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        playerView.setLayoutParams(params);

//                    Toast.makeText(Details.this, "We are going to FullScreen Mode.", Toast.LENGTH_SHORT).show();
        isFullScreen = true;

        playerView.setPlayer(simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        simpleExoPlayer.setPlayWhenReady(true);


//
//        btQuality.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popup = new PopupMenu(MainActivity.this, view);
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override public boolean onMenuItemClick(MenuItem item) {
//                        simpleExoPlayer.setSelectedTrack(0, (item.getItemId() - 1));
//                        return false;
//                    }
//                });
//                ArrayList<Integer> formats = new ArrayList<>();
//                Menu menu = popup.getMenu();
//                menu.add(Menu.NONE, 0, 0, "Bitrate");
//                for (int i = 0; i < simpleExoPlayer.getTrackCount(0); i++) {
//                    MediaFormat format = simpleExoPlayer.getTrackFormat(0, i);
//                    if (MimeTypes.isVideo(format.mimeType)) {
//                        Log.e("dsa", format.bitrate + "");
//                        if (format.adaptive) {
//                            menu.add(1, (i + 1), (i + 1), "Auto");
//                        } else {
//                            if (!formats.contains(format.bitrate)) {
//                                menu.add(1, (i + 1), (i + 1), (format.bitrate) / 1000 + " kbps");
//                                formats.add(format.bitrate);
//                            }
//                        }
//                    }
//                }
//
//
//                menu.setGroupCheckable(1, true, true);
//                menu.findItem((simpleExoPlayer.getSelectedTrack(0) + 1)).setChecked(true);
//                popup.show();
//            }
//
//        });


//

        imgSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetSpeed();
            }
        });


        btQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(MainActivity.this, view);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        Toast.makeText(getApplicationContext(), "Please wait!", Toast.LENGTH_LONG).show();

                        return false;
                    }
                });

                Menu menu = popup.getMenu();
                menu.add(Menu.NONE, 0, 0, "Video quality");
                popup.show();

            }
        });


        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                if (playbackState == Player.STATE_BUFFERING) {

                    progressBar.setVisibility(View.VISIBLE);

                } else if (playbackState == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);


                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

        btFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

            }
        });

    }

    private void SetSpeed() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Set Speed");
        builder.setCancelable(false);
        builder.setItems(speed, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                if (which==0){

                    speedTXT.setVisibility(View.VISIBLE);
                    speedTXT.setText("0.25X");
                    PlaybackParameters param = new PlaybackParameters(0.5f);
                    simpleExoPlayer.setPlaybackParameters(param);


                }  if (which==1){

                    speedTXT.setVisibility(View.VISIBLE);
                    speedTXT.setText("0.5X");
                    PlaybackParameters param = new PlaybackParameters(0.5f);
                    simpleExoPlayer.setPlaybackParameters(param);


                }
                if (which==2){

                    speedTXT.setVisibility(View.GONE);
                    PlaybackParameters param = new PlaybackParameters(1f);
                    simpleExoPlayer.setPlaybackParameters(param);


                }
                if (which==3){
                    speedTXT.setVisibility(View.VISIBLE);
                    speedTXT.setText("1.5X");
                    PlaybackParameters param = new PlaybackParameters(1.5f);
                    simpleExoPlayer.setPlaybackParameters(param);

                }
                if (which==4){

                    speedTXT.setVisibility(View.VISIBLE);
                    speedTXT.setText("2X");
                    PlaybackParameters param = new PlaybackParameters(2f);
                    simpleExoPlayer.setPlaybackParameters(param);

                }
            }
        });
        builder.show();

    }

    @Override
    protected void onPause() {
        super.onPause();

        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.getPlaybackState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}