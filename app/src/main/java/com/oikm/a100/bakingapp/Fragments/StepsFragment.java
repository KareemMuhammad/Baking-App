package com.oikm.a100.bakingapp.Fragments;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.oikm.a100.bakingapp.R;
import com.oikm.a100.bakingapp.StepsActivity;

public class StepsFragment extends Fragment implements ExoPlayer.EventListener {
    private static final String TAG = StepsActivity.class.getSimpleName();
    private static final String KEY_DESC = "desc";
    private static final String KEY_URL = "url";
    public TextView description;
    public TextView noVideo;
    private String desc;
    private String url;
    public SimpleExoPlayerView mPlayerView;
    public MediaSessionCompat mediaSession;
    public SimpleExoPlayer mExoPlayer;
    public PlaybackStateCompat.Builder playbackStateBuilder;
    private static final String DESC = "desc";
    private static final String VIDEO = "video";
    private long exoPlayerPosition;
    private boolean exoPlayerPlayWhenReady = true;
    private static final String POSITION = "position";
    private static final String STATE = "state";
    public StepsFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.steps_fragment_list,container,false);
        if (savedInstanceState != null){
           url = savedInstanceState.getString(KEY_URL);
           desc = savedInstanceState.getString(KEY_DESC);
           exoPlayerPosition = savedInstanceState.getLong(POSITION);
           exoPlayerPlayWhenReady = savedInstanceState.getBoolean(STATE);
           Log.d(TAG,"posPlayer "+exoPlayerPosition);
        }
            initializeViews(rootView);
            mediaSessionInitialize();
            setDesc();
            setMedia();
            return rootView;

    }

    private void setMedia() {
        Bundle b = this.getArguments();
        if (b != null) {
             url = b.getString(VIDEO);
            if (url != null && !url.isEmpty()) {
                Log.d(TAG, "video not null");
                initializePlayer(Uri.parse(url));
            } else {
                hidePlayer();
            }
        }
    }
    private void setDesc(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             desc = bundle.getString(DESC);
            description.setText(desc);
        }
    }

    private void initializeViews(View rootView) {
        mPlayerView = rootView.findViewById(R.id.playerView);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),R.drawable.nutella));
        description = rootView.findViewById(R.id.descriptionText);
        noVideo = rootView.findViewById(R.id.noVideoText);
    }
    private void hidePlayer() {
        noVideo.setVisibility(View.VISIBLE);
        mPlayerView.setVisibility(View.GONE);
    }
    private void displayPlayer() {
        noVideo.setVisibility(View.GONE);
        mPlayerView.setVisibility(View.VISIBLE);
    }

    private class MyMediaCallBack extends MediaSessionCompat.Callback{
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
    public void mediaSessionInitialize(){
            mediaSession = new MediaSessionCompat(requireContext(), TAG);
            mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mediaSession.setMediaButtonReceiver(null);
            playbackStateBuilder = new PlaybackStateCompat.Builder().setActions(
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS | PlaybackStateCompat.ACTION_PAUSE
                            | PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE
            );
            mediaSession.setPlaybackState(playbackStateBuilder.build());
            mediaSession.setCallback(new MyMediaCallBack());
            mediaSession.setActive(true);
    }
    private void initializePlayer(Uri mediaUri) {
        displayPlayer();
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(requireContext(), getString(R.string.app_name));
                DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(requireContext(), userAgent);
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, dataSourceFactory, new DefaultExtractorsFactory(), null, null);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.seekTo(exoPlayerPosition);
                mExoPlayer.setPlayWhenReady(exoPlayerPlayWhenReady);
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,mExoPlayer.getCurrentPosition(),1f);
            mediaSession.setPlaybackState(playbackStateBuilder.build());
        } else if((playbackState == ExoPlayer.STATE_READY)){
            playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,mExoPlayer.getCurrentPosition(),1f);
            mediaSession.setPlaybackState(playbackStateBuilder.build());
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        mediaSession.setActive(false);
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_DESC,desc);
        outState.putString(KEY_URL,url);
        outState.putLong(POSITION,exoPlayerPosition);
        outState.putBoolean(STATE,exoPlayerPlayWhenReady);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            exoPlayerPlayWhenReady = mExoPlayer.getPlayWhenReady();
            exoPlayerPosition = mExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(Uri.parse(url));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer(Uri.parse(url));
        }
    }
}
