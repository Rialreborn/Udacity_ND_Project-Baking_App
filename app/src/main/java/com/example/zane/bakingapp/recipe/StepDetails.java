package com.example.zane.bakingapp.recipe;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.zane.bakingapp.R;
import com.example.zane.bakingapp.objects.Step;
import com.example.zane.bakingapp.utils.Constants;
import com.example.zane.bakingapp.utils.CustomiseWindow;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zane on 05/06/2018.
 */

public class StepDetails extends AppCompatActivity {

    private static final String LOG_TAG = StepDetails.class.getSimpleName();

    @BindView(R.id.step_overview_toolbar)
    Toolbar toolbar;
    @BindView(R.id.player_view)
    SimpleExoPlayerView playerView;
    @BindView(R.id.step_details)
    TextView tvStepDetails;

    ArrayList<Step> mStepArray;
    Uri mVideoUri;
    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.step_overview);

        ButterKnife.bind(StepDetails.this);

        mStepArray = getIntent().getParcelableArrayListExtra(Constants.INTENT_STEPS_ARRAY);

        int position = getIntent().getIntExtra(Constants.INTENT_STEP_POSITION, 0);
        mVideoUri = mStepArray.get(position).getVideoUrl();
        Step step = mStepArray.get(position);

        Log.i(LOG_TAG, "Uri for videoUri: " + mVideoUri);
        if(!mVideoUri.toString().equals("")) {
            initializePlayer(mVideoUri);
        }

        CustomiseWindow.customWindow(this);


        toolbar.setTitle(step.getShortDescription());
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary));
        toolbar.setTitleTextColor(getColor(R.color.white));


        tvStepDetails.setText(step.getDescription());
    }

    private void initializePlayer(Uri videoUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            playerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

}
