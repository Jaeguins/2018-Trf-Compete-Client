package com.example.jsu48.a2018_trf_compete_client;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class TrackTMapView extends TMapView {
    ArrayList<TMapPoint> field;
    Refresher r;
    Tracking cont;
    TMapGpsManager gpsM;

    boolean isDestinated=true;
    public void setField(TMapPolyLine line) {
        this.field = line.getLinePoint();
        for(int i=0;i<field.size()-1;i++){
            Vect tmp=Vect.getVect(new Vect(field.get(i)), new Vect(field.get(i+1)));
            Log.d("path optimize",i+" : "+tmp.getSize()+"");
            if(tmp.getSize()<=5){
                field.remove(i);
                i-=1;
            }
        }
        TMapPolyLine optLine=new TMapPolyLine();
        for(int i=0;i<field.size();i++){
            optLine.addLinePoint(field.get(i));
        }
        addTMapPolyLine("path",optLine);
        r = new Refresher();
        r.start();
    }

    public void setDestinated(boolean destinated) {
        isDestinated = destinated;
    }

    public TrackTMapView(Context context,TMapGpsManager gps) {
        super(context);
        gpsM=gps;
        cont = (Tracking) (TrackTMapView.this.getContext());
        this.setOnClickListenerCallBack(new OnClickListenerCallback() {

            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }
        });

    }
    class Refresher extends Thread {
        boolean stopper = false;
        TMapPoint closest;
        TMapPoint here;

        public void stopRun() {
            stopper = true;
        }

        @Override
        public void run() {

            for (int i = 0; i < field.size(); i++) {
                TMapMarkerItem t = new TMapMarkerItem();
                t.setCalloutTitle("" + i);
                t.setAutoCalloutVisible(true);
                t.setCanShowCallout(true);
                t.setTMapPoint(field.get(i));
                addMarkerItem("" + i, t);
            }
            int k=0;
            gpsM.setLocationCallback();
            while (!stopper) {
                setTrackingMode(true);
                double dist = 0.0;
                here =gpsM.getLocation();
                TMapMarkerItem z = new TMapMarkerItem();
                z.setCalloutTitle("here");
                z.setAutoCalloutVisible(true);
                z.setCanShowCallout(true);
                z.setTMapPoint(here);
                addMarkerItem("here"+k,z);
                k+=1;
                /*setCompassMode(true);
                setSightVisible(true);
                setZoomLevel(18);*/
                if(!isDestinated||(here.getLongitude()==0&&here.getLatitude()==0)){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("TrackMapView","freedriving, skip corner detection");
                    continue;
                }
                //현위치 갱신 및 현위치 지도 갱신
                double min = 999999999999.0;
                int minInd = 0;
                for (int i = 0; i < field.size(); i++) {
                    double t = Vect.getDist(here, field.get(i));
                    if (t < min) {
                        minInd = i;
                        min = t;
                    }
                }
                closest = field.get(minInd);
                //자신과 가장 가까운 노드 갱신 TODO 최근접 노드 이전 노드들 삭제여부 검토
                dist += Vect.getDist(here, closest);
                //자신과 가장 가까운 노드간 거리 더함
                int i = minInd,j=i+1,l=j+1, dir = -1;
                double angle=0.0;
                Vect in=new Vect(), out = new Vect();
                in = Vect.getVect(new Vect(field.get(i)), new Vect(field.get(j)));
                out=Vect.getVect(new Vect(field.get(j)),new Vect(field.get(l)));
                while(l<field.size()) {

                    dist += in.getSize();
                    angle = Vect.getAngle(in, out);
                    Log.d("nowRotation",i+"~"+j+" -> "+j+"~"+l+" angle : "+angle);
                    if (angle > Math.PI) {
                        angle = 2 * Math.PI - angle;
                    }
                    if (angle > Math.PI * 3 / 4 || angle < Math.PI * -3 / 4) {
                        dir = 0;
                        break;
                    } else if (angle > Math.PI / 4) {
                        dir = 1;
                        break;
                    } else if (angle < Math.PI / -4) {
                        dir = 2;
                        break;
                    } else {
                        in=out;
                        i+=1;
                        j+=1;
                        l+=1;
                        if(l>=field.size()){
                            dir=3;
                            break;
                        }else {
                            out = Vect.getVect(new Vect(field.get(j)), new Vect(field.get(l)));
                        }
                        continue;
                    }
                }
                Log.i("nowRotation",dir+"");
                final int rDir = dir;
                final double rDist = dist;
                cont.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (rDir) {
                            case 0:
                                Log.i("nowRotation","uturn");
                                cont.changeNow(R.drawable.uturn, rDist);
                                break;
                            case 1:
                                Log.i("nowRotation","left");
                                cont.changeNow(R.drawable.left, rDist);
                                break;
                            case 2:
                                Log.i("nowRotation","right");
                                cont.changeNow(R.drawable.right, rDist);
                                break;
                            case 3:
                                Log.i("nowRotation","arrival");
                                cont.changeNow(R.drawable.arrival,rDist);
                                break;
                        }
                    }
                });
                //이번 코너 탐지/거리 갱신
                i+=1;
                j+=1;
                l+=1;
                dist=0;
                in = Vect.getVect(new Vect(field.get(i)), new Vect(field.get(j)));
                out=Vect.getVect(new Vect(field.get(j)),new Vect(field.get(l)));
                while(l<field.size()) {
                    dist += in.getSize();
                    angle = Vect.getAngle(in, out);
                    Log.i("nextRotation",i+"~"+j+" -> "+j+"~"+l+" angle : "+angle);
                    if (angle > Math.PI) {
                        angle = 2 * Math.PI - angle;
                    }
                    if (angle > Math.PI * 3 / 4 || angle < Math.PI * -3 / 4) {
                        dir = 0;
                        break;
                    } else if (angle > Math.PI / 4) {
                        dir = 1;
                        break;
                    } else if (angle < Math.PI / -4) {
                        dir = 2;
                        break;
                    } else {
                        in=out;
                        i+=1;
                        j+=1;
                        l+=1;
                        if(l>=field.size()){
                            dir=3;
                            break;
                        }else {
                            out = Vect.getVect(new Vect(field.get(j)), new Vect(field.get(l)));
                        }
                        continue;
                    }
                }
                final int nDir = dir;
                final double nDist = dist;
                Log.i("nextRotation",dir+"");
                cont.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (nDir) {
                            case 0:
                                Log.i("nextRotation","uTurn");
                                cont.changeNext(R.drawable.uturn, nDist);
                                break;
                            case 1:
                                Log.i("nextRotation","left");
                                cont.changeNext(R.drawable.left, nDist);
                                break;
                            case 2:
                                Log.i("nextRotation","right");
                                cont.changeNext(R.drawable.right, nDist);
                                break;
                            case 3:
                                Log.i("nextRotation","arrival");
                                cont.changeNext(R.drawable.arrival,nDist);
                                break;
                        }
                    }
                });
                //다음 코너 탐지/거리 갱신
                try
                {
                    Thread.sleep(5000);
                } catch (
                        InterruptedException e)

                {
                    e.printStackTrace();
                }
            }
            gpsM.CloseGps();
        }
    }
}
