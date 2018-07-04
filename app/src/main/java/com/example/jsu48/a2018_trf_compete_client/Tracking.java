package com.example.jsu48.a2018_trf_compete_client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Tracking extends AppCompatActivity {
    TrackTMapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        mapView=new TrackTMapView(this);
        mapView.addView(findViewById(R.id.mapView));

    }
}
