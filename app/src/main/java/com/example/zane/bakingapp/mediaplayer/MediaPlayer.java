package com.example.zane.bakingapp.mediaplayer;

import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

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

/**
 * Created by Zane on 06/06/2018.
 */

public class MediaPlayer {
//
//    private SimpleExoPlayer mExoPlayer;
//    private SimpleExoPlayerView mPlayerView;
//    private static MediaSessionCompat mMediaSession;
//    private PlaybackStateCompat.Builder mStateBuilder;
//}
//
//    private void initializePlayer(Uri mediaUri) {
//        if (mExoPlayer == null) {
//            // Create an instance of the ExoPlayer.
//            TrackSelector trackSelector = new DefaultTrackSelector();
//            LoadControl loadControl = new DefaultLoadControl();
//            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
//            mPlayerView.setPlayer(mExoPlayer);
//
//            // Set the ExoPlayer.EventListener to this activity.
//            mExoPlayer.addListener(this);
//
//            // Prepare the MediaSource.
//            String userAgent = Util.getUserAgent(this, "ClassicalMusicQuiz");
//            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
//                    this, userAgent), new DefaultExtractorsFactory(), null, null);
//            mExoPlayer.prepare(mediaSource);
//            mExoPlayer.setPlayWhenReady(true);
//        }
}
