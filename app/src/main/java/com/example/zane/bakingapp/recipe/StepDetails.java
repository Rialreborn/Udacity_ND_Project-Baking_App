package com.example.zane.bakingapp.recipe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
    @Nullable @BindView(R.id.player_view)
    SimpleExoPlayerView playerView;
    @BindView(R.id.step_details)
    TextView tvStepDetails;
    @BindView(R.id.iv_left_arrow)
    ImageButton ivPreviousStep;
    @BindView(R.id.iv_right_arrow)
    ImageButton ivNextStep;

    private int mPosition;
    private Uri mVideoUri;
    private Uri mThumbnailUri;
    private String mUserAgent;
    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private ArrayList<Step> mStepArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStepArray = getIntent().getParcelableArrayListExtra(Constants.INTENT_STEPS_ARRAY);
        mPosition = getIntent().getIntExtra(Constants.INTENT_STEP_POSITION, 0);
        mStep = mStepArray.get(mPosition);
        mVideoUri = mStep.getVideoUrl();
        mThumbnailUri = mStep.getThumbnailUri();
        Uri playerUri = null;


        if (mVideoUri != null && !mVideoUri.toString().equals("")) {
            Log.i(LOG_TAG, "Uri for videoUri: " + mVideoUri);

            setContentView(R.layout.step_overview);
            playerUri = mVideoUri;
        } else if (mThumbnailUri != null && !mThumbnailUri.toString().equals("")) {
            Log.i(LOG_TAG, "Uri for thumbnail Uri: " + mThumbnailUri);

            setContentView(R.layout.step_overview);
            playerUri = mThumbnailUri;
        } else {
            setContentView(R.layout.step_overview_no_video);
        }

        ButterKnife.bind(StepDetails.this);
        initializePlayer(playerUri);

        CustomiseWindow.customWindow(this);
        setViews();
        setToolbar();
    }

    private void setViews() {
        if (mPosition == 0) {
            ivPreviousStep.setVisibility(View.INVISIBLE);
        } else if (mPosition == mStepArray.size() - 1) {
            ivNextStep.setVisibility(View.INVISIBLE);
        }

        tvStepDetails.setText(mStep.getDescription());
    }


    public void loadNextOrPreviousStep(View view) {
        int id = view.getId();

        if (id == findViewById(R.id.iv_right_arrow).getId()) {
            mPosition += 1;
        } else if (id == findViewById(R.id.iv_left_arrow).getId()) {
            mPosition -= 1;
        }

        reloadActivity(id);
    }

    private void reloadActivity(int direction) {
        Intent intent = getIntent();
        intent.putExtra(Constants.INTENT_STEP_POSITION, mPosition);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        if (direction == findViewById(R.id.iv_left_arrow).getId()) {
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        } else {
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }

    }

    private void initializePlayer(Uri videoUri) {
        if (mExoPlayer == null && playerView != null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            playerView.setPlayer(mExoPlayer);

            mUserAgent = Util.getUserAgent(this, "BakingApp");

            MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(
                    this, mUserAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void setToolbar() {
        toolbar.setTitle(mStep.getShortDescription());
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary));
        toolbar.setTitleTextColor(getColor(R.color.white));
    }


}
