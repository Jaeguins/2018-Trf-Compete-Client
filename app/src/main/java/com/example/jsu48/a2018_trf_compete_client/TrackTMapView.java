package com.example.jsu48.a2018_trf_compete_client;

import android.content.Context;
import android.graphics.PointF;

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

    public void setField(TMapPolyLine line, GPSManager gps) {
        this.field = line.getLinePoint();
        r = new Refresher(gps);
        r.start();
    }

    public TrackTMapView(Context context) {
        super(context);
        setSKTMapApiKey("4296b5d5-5254-4cc1-89a0-e6dfbb467f30");
        //this.setCompassMode(true);
        cont = (Tracking) (TrackTMapView.this.getContext());
        this.setOnClickListenerCallBack(new OnClickListenerCallback() {

            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //TrackTMapView.this.setCompassMode(true);
                return false;
            }
        });

    }

    class Refresher extends Thread {
        boolean stopper = false;
        GPSManager gps;
        TMapPoint closest;
        TMapPoint here;


        public Refresher(GPSManager gps) {
            this.gps = gps;
        }

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
            while (!stopper) {
                double dist = 0.0;
                gps.getLocation();
                here = new TMapPoint(gps.getLatitude(), gps.getLongitude());
                setCenterPoint(gps.getLongitude(), gps.getLatitude());
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
                int i = minInd, dir = -1;
                double angle=0.0;
                Vect in=new Vect(), out = new Vect();
                for(;i<field.size();i++){
                    in = Vect.getVect(new Vect(field.get(minInd)), new Vect(field.get(i)));
                    if (in.x != 0 || in.y != 0) break;
                }
                int j=i;
                while(j<field.size()) {
                    for (; j< field.size() ; j++) {
                        out = Vect.getVect(new Vect(field.get(i)), new Vect(field.get(j)));
                        if (out.x != 0 || out.y != 0) break;
                    }
                    dist += in.getSize();
                    angle = Vect.getAngle(in, out);

                    if (angle > Math.PI) {
                        angle = 2 * Math.PI - angle;
                    }
                    if (angle > Math.PI * 3 / 4 || angle < Math.PI * -3 / 4) {
                        dir = 0;
                        System.out.println("turn around after " + dist + "m from " + minInd + " to " + i);
                        break;
                    } else if (angle > Math.PI / 4) {
                        dir = 1;
                        System.out.println("turn left after " + (int)dist + "m from " + minInd + " to " + i);
                        break;
                    } else if (angle < Math.PI / -4) {
                        dir = 2;
                        System.out.println("turn right after " + dist + "m from " + minInd + " to " + i);
                        break;
                    } else {
                        System.out.println("go next node : "+minInd+" to "+i);
                        in=out;
                        minInd=i;
                        i=j;
                        continue;
                    }
                }
                final int rDir = dir;
                final double rDist = dist;
                cont.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (rDir) {
                            case 0:
                                cont.changeNow(R.drawable.uturn, rDist);
                                break;
                            case 1:
                                cont.changeNow(R.drawable.left, rDist);
                                break;
                            case 2:
                                cont.changeNow(R.drawable.right, rDist);
                                break;
                        }
                    }
                });
                //이번 코너 탐지/거리 갱신

                out = new Vect();
                while(minInd+i<field.size()) {
                    for(;minInd+i<field.size()-1;i++){
                        in = Vect.getVect(new Vect(field.get(minInd + i)), new Vect(field.get(minInd + i + 1)));
                        if (in.x != 0 || in.y != 0) break;
                    }
                    for (; minInd + i < field.size() - 1; i++) {
                        out = Vect.getVect(new Vect(field.get(minInd + i)), new Vect(field.get(minInd + 1 + i)));
                        if (out.x != 0 || out.y != 0) break;
                    }
                    dist += in.getSize();
                    angle = Vect.getAngle(in, out);

                    if (angle > Math.PI) {
                        angle = 2 * Math.PI - angle;
                    }
                    if (angle > Math.PI * 7 / 8 || angle < Math.PI * -7 / 8) {
                        dir = 0;
                        System.out.println("turn around after " + dist + "m from " + minInd + " to " + (minInd + i));
                        break;
                    } else if (angle > Math.PI / 8) {
                        dir = 1;
                        System.out.println("turn left after " + dist + "m from " + minInd + " to " + (minInd + i));
                        break;
                    } else if (angle < Math.PI / -8) {
                        dir = 2;
                        System.out.println("turn right after " + dist + "m from " + minInd + " to " + (minInd + i));
                        break;
                    } else {
                        i+=1;
                        //System.out.println("go next node : "+minInd+" to "+(minInd+i));
                        continue;
                    }
                }
                final int nDir = dir, nDist = (int) dist;
                cont.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       switch (nDir) {
                           case 0:
                               cont.changeNow(R.drawable.uturn, nDist);
                               break;
                           case 1:
                               cont.changeNow(R.drawable.left, nDist);
                               break;
                           case 2:
                               cont.changeNow(R.drawable.right, nDist);
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
        }
    }
}
