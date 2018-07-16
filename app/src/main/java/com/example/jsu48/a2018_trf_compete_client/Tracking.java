package com.example.jsu48.a2018_trf_compete_client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class Tracking extends AppCompatActivity {
    TrackTMapView mapView;
    GPSManager gps;
    TMapPolyLine path;
    TextView nowDist,nextDist;
    ImageView nowTurn,nextTurn;
    int pathCounter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        nowDist=findViewById(R.id.nowDist);
        nextDist=findViewById(R.id.nextDist);
        nowTurn=findViewById(R.id.nowImage);
        nextTurn=findViewById(R.id.nextImage);
        gps = new GPSManager(this);
        mapView = new TrackTMapView(this);
        LinearLayout viewer = findViewById(R.id.mapViewer);
        viewer.addView(mapView);
        Intent i=getIntent();
        if(i.getAction().equals("free")){
            mapView.setDestinated(false);
        }
        final TMapPoint targetPoint=new TMapPoint(i.getDoubleExtra("latitude",1.0),i.getDoubleExtra("longitude",1.0));
        Thread p=new Thread(new Runnable(){
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
                path.setLineColor(Color.RED);
                path.setLineWidth(50.0f);
                path.setLineAlpha(255);
                mapView.addTMapPolyLine("path",path);
                mapView.setField(path,gps);
            }
        });
        p.start();
        try {
            p.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        mapView.r.stopRun();
    }
    public void changeNow(int imgRes,double distance){
        findViewById(R.id.turnInfo).setVisibility(View.VISIBLE);
        nowTurn.setImageResource(imgRes);
        nowDist.setText((int)distance+"m");
    }
    public void changeNext(int imgRes,double distance){
        nextTurn.setImageResource(imgRes);
        nextDist.setText((int)distance+"m");
    }
}
