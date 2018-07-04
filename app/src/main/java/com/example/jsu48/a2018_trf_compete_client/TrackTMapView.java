package com.example.jsu48.a2018_trf_compete_client;

import android.content.Context;

import com.skt.Tmap.TMapView;

public class TrackTMapView extends TMapView {
    public TrackTMapView(Context context) {
        super(context);
        this.setTrackingMode(true);
        this.setCompassMode(true);
        
    }
}
