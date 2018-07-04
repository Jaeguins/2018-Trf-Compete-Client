package com.example.jsu48.a2018_trf_compete_client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class Tracking extends AppCompatActivity {
    TrackTMapView mapView;
    GPSManager gps;
    GPSThread k;
    TMapPolyLine path;
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
        Intent i=getIntent();
        final TMapPoint targetPoint=new TMapPoint(i.getDoubleExtra("latitude",1.0),i.getDoubleExtra("longitude",1.0));
        new Thread(new Runnable(){
            @Override
            public void run(){
                TMapPoint now=new TMapPoint(gps.getLatitude(),gps.getLongitude());
                try {
                    Tracking.this.path=new TMapData().findPathData(now,targetPoint);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                mapView.addTMapPolyLine("path",path);
            }
        }).start();
    }
    protected void onDestroy() {
        super.onDestroy();
        k.stopping=true;
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
}
