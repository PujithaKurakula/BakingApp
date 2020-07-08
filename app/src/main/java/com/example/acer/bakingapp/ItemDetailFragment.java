package com.example.acer.bakingapp;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.acer.bakingapp.dummy.DummyContent;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
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
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String FRG_Item_id = "getItem_id";
    public static final String ARG_ITEM_ID = "item_id";
    String des, shortdes, videourl;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer simpleExoPlayer;
    long currentposition;

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            des = getArguments().getString("description");
            shortdes = getArguments().getString("shortdesc");
            videourl = getArguments().getString("videourl");


            Activity activity = this.getActivity();
          /*  CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        TextView descriptiontextv = rootView.findViewById(R.id.desc);
        simpleExoPlayerView = rootView.findViewById(R.id.exoplayer);
        descriptiontextv.setText(des);
        startPlayer();


        return rootView;
    }


    public void startPlayer() {
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(new
                        DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        Uri playuri;
        if (!videourl.isEmpty()) {
            playuri = Uri.parse(videourl);
        }  else {
            playuri = Uri.parse("");
            simpleExoPlayerView.setVisibility(View.GONE);
        }

        RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        String useragent = Util.getUserAgent(getContext(), "NewBakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(playuri, new DefaultDataSourceFactory
                (getContext(), useragent), new DefaultExtractorsFactory(), null, null);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
        simpleExoPlayer.prepare(mediaSource);
        if (currentposition != 0) {
            simpleExoPlayer.seekTo(currentposition);
            simpleExoPlayer.setPlayWhenReady(true);
        }
        simpleExoPlayerView.setPlayer(simpleExoPlayer);
    }

    public void stopPlayer() {

        if (simpleExoPlayer != null) {

            currentposition = simpleExoPlayer.getCurrentPosition();
            Log.i("pausestate", "video is  stop in  stopplater");
            simpleExoPlayer.release();
            simpleExoPlayer.stop();
            //simpleExoPlayer = null;
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        Log.i("pausestate", "video is stop");
        simpleExoPlayer.release();
        if (Util.SDK_INT <= 23) {

            stopPlayer();
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            Log.i("pausestate", "video is stop in stop");
            stopPlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayer();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || simpleExoPlayer == null) {
            startPlayer();
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("currentpos", simpleExoPlayer.getCurrentPosition());
        Log.i("savepos", String.valueOf(simpleExoPlayer.getCurrentPosition()));
        outState.putBoolean("playwhenready", simpleExoPlayer.getPlayWhenReady());
    }
}