package com.example.jsu48.a2018_trf_compete_client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapView;

public class Tracking extends AppCompatActivity {
    TrackTMapView mapView;
    GPSManager gps;
    GPSThread k;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        mapView = new TrackTMapView(this);
        LinearLayout viewer = findViewById(R.id.mapViewer);
        viewer.addView(mapView);
        gps = new GPSManager(this);
        mapView.setZoomLevel(18);
        k = new GPSThread(mapView, gps);
        k.start();
    }
    protected void onDestroy() {
        super.onDestroy();
        k.stopping=true;
    }
}
class GPSThread extends Thread{
    TMapView mapView;
    GPSManager gps;
    boolean stopping=false;
    public GPSThread(TMapView mapView,GPSManager gps){
        this.mapView=mapView;
        this.gps=gps;
    }
    @Override
    public void run() {
        while(true) {
            double lon=gps.getLongitude(),lat=gps.getLatitude();
            mapView.setCenterPoint(lon,lat);
            System.out.println("location update to : "+lon+", "+lat);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(stopping) break;
        }
    }
}