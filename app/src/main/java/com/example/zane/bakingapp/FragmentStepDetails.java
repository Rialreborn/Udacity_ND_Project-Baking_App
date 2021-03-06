package com.example.zane.bakingapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zane.bakingapp.objects.Recipe;
import com.example.zane.bakingapp.objects.Step;
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

public class FragmentStepDetails extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = FragmentStepDetails.class.getSimpleName();
    private static final String EXOPLAYER_POSITION = "exoplayer_position";
    private static final String EXOPLAYER_STATE = "exoplayer_state";

    @Nullable
    @BindView(R.id.player_view)
    SimpleExoPlayerView playerView;
    @Nullable
    @BindView(R.id.tv_step_details)
    TextView tvStepDetails;
    @Nullable
    @BindView(R.id.iv_left_arrow)
    ImageButton ivPreviousStep;
    @Nullable
    @BindView(R.id.iv_right_arrow)
    ImageButton ivNextStep;
    @Nullable
    @BindView(R.id.step_title)
    TextView tvTitle;


    private int mRecipePosition;
    private int mStepPosition;
    private SimpleExoPlayer mExoPlayer;
    private ArrayList<Step> mStepArray;
    private long mPlayerPosition;
    private boolean mPlayerPlayWhenReady;
    ArrayList<Recipe> mRecipeArrayList;
    Step mStep;
    Uri mVideoUri;
    Uri mThumbnailUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i(LOG_TAG, "MSG! OnCreateView started");

        View view = inflater.inflate(R.layout.fragment_step, container, false);

        ButterKnife.bind(FragmentStepDetails.this, view);

        mRecipeArrayList = MainActivity.recipeArray;

        if (savedInstanceState != null) {
            mPlayerPosition = savedInstanceState.getLong(EXOPLAYER_POSITION);
            mPlayerPlayWhenReady = savedInstanceState.getBoolean(EXOPLAYER_STATE);
        } else {
            mPlayerPlayWhenReady = true;
        }

        if (mRecipeArrayList != null && mRecipeArrayList.size() > 0) {
            mRecipePosition = MainActivity.recipePosition;
            mStepPosition = MainActivity.stepPosition;
            mStepArray = mRecipeArrayList.get(mRecipePosition).getSteps();
            mStep = mStepArray.get(mStepPosition);

            setupNextPreviousButtonListener();
            updateViews();
        }

        return view;
    }

    private void updateViews() {
        Log.i(LOG_TAG, "MSG! updateViews()");

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if (toolbar != null && !MainActivity.tabletUsed) {
            toolbar.setTitle(mStep.getShortDescription());
        }
        if (tvTitle != null) {
            tvTitle.setText(mStep.getShortDescription());
        }
        if (tvStepDetails != null) {
            tvStepDetails.setText(mStep.getDescription());
        }

        mVideoUri = mStep.getVideoUrl();
        mThumbnailUri = mStep.getThumbnailUri();
        Uri playerUri;
        if (mVideoUri != null && !mVideoUri.toString().equals("")) {
            playerUri = mVideoUri;
        } else if (mThumbnailUri != null && !mThumbnailUri.toString().equals("")) {
            playerUri = mThumbnailUri;
        } else {
            playerUri = null;
        }
        initializePlayer(playerUri);
        setNextAndPreviousButtonVisibility();
    }

    private void setupNextPreviousButtonListener() {
        Log.i(LOG_TAG, "MSG! setupNextPreviousButtonListener()");
        if (ivPreviousStep != null && ivNextStep != null) {
            ivPreviousStep.setOnClickListener(this);
            ivNextStep.setOnClickListener(this);
        }
    }

    private void setNextAndPreviousButtonVisibility() {
        Log.i(LOG_TAG, "MSG! setNextAndPreviousButtonVisibility()");
        if (mStepPosition == 0) {
            ivPreviousStep.setVisibility(View.INVISIBLE);
        } else if (mStepPosition == mStepArray.size() - 1) {
            ivNextStep.setVisibility(View.INVISIBLE);
        } else {
            ivNextStep.setVisibility(View.VISIBLE);
            ivPreviousStep.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Log.i(LOG_TAG, "MSG! onClick()");

        if (v == ivPreviousStep) {
            mStepPosition -= 1;
            Log.i(LOG_TAG, "MSG! Step Position decreased to: " + mStepPosition);
        } else if (v == ivNextStep) {
            mStepPosition += 1;
            Log.i(LOG_TAG, "MSG! Step Position increased to: " + mStepPosition);
        }
        mStep = mStepArray.get(mStepPosition);
        MainActivity.stepPosition = mStepPosition;
        releasePlayer();
        updateViews();
    }

    private void initializePlayer(Uri videoUri) {
        Log.i(LOG_TAG, "MSG! initializePlayer(); \nVideoUri: " + videoUri);

        if (videoUri != null && !videoUri.toString().equals("")) {
            if (mExoPlayer == null && playerView != null) {

                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();

                mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                        getActivity().getApplicationContext(), trackSelector, loadControl);
                playerView.setPlayer(mExoPlayer);

                startPlayer(videoUri);
            }
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.baking_backdrop);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            playerView.setPlayer(null);
            playerView.setDefaultArtwork(bitmap);
        }
    }

    private void startPlayer(Uri videoUri) {
        Log.i(LOG_TAG, "MSG! startPlayer(); \nPlayer position: " + mPlayerPosition);
        String mUserAgent = Util.getUserAgent(getActivity().getApplicationContext(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(
                getActivity().getApplicationContext(), mUserAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);


        mExoPlayer.seekTo(mPlayerPosition);
        mExoPlayer.setPlayWhenReady(mPlayerPlayWhenReady);
    }

    private void releasePlayer() {
        Log.i(LOG_TAG, "MSG! releasePlayer()");
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        Log.i(LOG_TAG, "MSG! onStop()");
        super.onStop();

        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, "MSG! onSaveInstanceState(); \nPlayer Position: " + mExoPlayer.getCurrentPosition());
        Log.i(LOG_TAG, "MSG! onSaveInstanceState(); \nPlayer State: " + mExoPlayer.getPlaybackState());
        super.onSaveInstanceState(outState);

        outState.putLong(EXOPLAYER_POSITION, mExoPlayer.getCurrentPosition());
        outState.putBoolean(EXOPLAYER_STATE, mExoPlayer.getPlayWhenReady());
    }

    public FragmentStepDetails() {
    }

}
