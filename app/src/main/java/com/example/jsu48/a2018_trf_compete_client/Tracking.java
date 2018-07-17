package com.example.jsu48.a2018_trf_compete_client;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class Tracking extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{
    TrackTMapView mapView;
    TMapPolyLine path;
    TextView nowDist,nextDist;
    ImageView nowTurn,nextTurn;
    TMapGpsManager gpsM;
    int pathCounter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        gpsM=new TMapGpsManager(this);
        nowDist=findViewById(R.id.nowDist);
        nextDist=findViewById(R.id.nextDist);
        nowTurn=findViewById(R.id.nowImage);
        nextTurn=findViewById(R.id.nextImage);
        mapView = new TrackTMapView(this,gpsM);
        LinearLayout viewer = findViewById(R.id.mapViewer);
        viewer.addView(mapView);
        gpsM.setMinTime(1000);
        gpsM.setMinDistance(5);
        gpsM.setProvider(gpsM.NETWORK_PROVIDER);
        gpsM.OpenGps();
        Intent i=getIntent();
        if(i.getAction().equals("free")){
            mapView.setDestinated(false);
        }
        final TMapPoint startPoint=new TMapPoint(i.getDoubleExtra("slatitude",1.0),i.getDoubleExtra("slongitude",1.0));
        final TMapPoint targetPoint=new TMapPoint(i.getDoubleExtra("latitude",1.0),i.getDoubleExtra("longitude",1.0));
        Thread p=new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    Tracking.this.path=new TMapData().findPathData(startPoint,targetPoint);
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
                mapView.setField(path);
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

    @Override
    public void onLocationChange(Location location) {
        mapView.setLocationPoint(location.getLongitude(),location.getLatitude());
    }
}
