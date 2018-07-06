package com.example.jsu48.a2018_trf_compete_client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
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
    GPSThread k;
    TMapPolyLine path;
    int pathCounter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        mapView = new TrackTMapView(this);
        LinearLayout viewer = findViewById(R.id.mapViewer);
        viewer.addView(mapView);
        gps = new GPSManager(this);
        //mapView.setZoomLevel(18);
        Intent i=getIntent();
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
                path.setLineWidth(100.0f);
                path.setLineAlpha(255);
                mapView.addTMapPolyLine("path",path);
            }
        });
        p.start();
        try {
            p.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        k = new GPSThread(path.getLinePoint());
        k.start();
    }
    protected void onDestroy() {
        super.onDestroy();
        k.stopping=true;
    }
    class GPSThread extends Thread{
        boolean stopping=false;
        ArrayList<TMapPoint> field;
        public GPSThread(ArrayList<TMapPoint> field){
            this.field=field;
        }
        @Override
        public void run() {
            for(int i=0;i<field.size();i++){
                TMapMarkerItem t=new TMapMarkerItem();
                t.setCalloutTitle(""+i);
                t.setAutoCalloutVisible(true);
                t.setCanShowCallout(true);
                t.setTMapPoint(field.get(i));
                mapView.addMarkerItem(""+i,t);
            }
            double lon=gps.getLongitude(),lat=gps.getLatitude();
            mapView.setCenterPoint(lon,lat);
            while(true) {
                //mapView.setCenterPoint(lon,lat);
                System.out.println("location update to : "+lon+", "+lat);
                TMapPoint here=new TMapPoint(gps.getLatitude(),gps.getLongitude());
                TMapPoint tmpPoint=field.get(pathCounter+1);
                if(Vect.getDist(new Vect(tmpPoint),new Vect(here))<10){
                    pathCounter+=1;
                }
                Vect in;
                Vect out;
                boolean isCurved=false;
                int i=0;
                double dist=0;
                final String msg;
                final int imgSet;
                final int fDist;
                String tMsg="";
                int img=-1,tDist=-1;
                dist+=Vect.getDist(new Vect(here),new Vect(tmpPoint));  
                do{
                    in=Vect.getVect(new Vect(field.get(pathCounter+i)),new Vect(field.get(pathCounter+i+1)));
                    while(true) {
                        i+=1;
                        out = Vect.getVect(new Vect(field.get(pathCounter + i)), new Vect(field.get(pathCounter + 1 + i)));
                        if(out.x!=0||out.y!=0) break;
                    }
                    dist += Vect.getDist(in,out);
                    double angle=Vect.getAngle(in,out);

                    if(angle>Math.PI){
                        angle=2*Math.PI-angle;
                    }
                    if(angle>Math.PI/3){
                        tMsg="turn left after "+dist+"m from "+pathCounter+" to "+(pathCounter+i);
                        img=R.drawable.left;
                        isCurved=true;
                    }else if(angle<Math.PI/-3){
                        tMsg="turn right after "+dist+"m from "+pathCounter+" to "+(pathCounter+i);
                        img=R.drawable.right;
                        isCurved=true;
                    }
                    TextView v=findViewById(R.id.nextDist);
                }while(!isCurved);
                msg=tMsg;
                imgSet=img;
                fDist=(int)dist;
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        ImageView f=findViewById(R.id.nowImage);
                        TextView t=findViewById(R.id.nowDist);
                        f.setImageResource(imgSet);
                        t.setText(fDist+"m");
                        Toast.makeText(getBaseContext(),msg,Toast.LENGTH_SHORT).show();
                    }
                });
                if(stopping) break;

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
