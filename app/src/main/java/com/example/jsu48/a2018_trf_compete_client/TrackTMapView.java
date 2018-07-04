package com.example.jsu48.a2018_trf_compete_client;

import android.content.Context;

import com.skt.Tmap.TMapView;

public class TrackTMapView extends TMapView {
    public TrackTMapView(Context context) {
        super(context);
        setSKTMapApiKey("4296b5d5-5254-4cc1-89a0-e6dfbb467f30");
        this.setCompassMode(true);

    }
}
