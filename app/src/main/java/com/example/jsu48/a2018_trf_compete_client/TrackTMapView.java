package com.example.jsu48.a2018_trf_compete_client;

import android.content.Context;
import android.graphics.PointF;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class TrackTMapView extends TMapView {
    public TrackTMapView(Context context) {
        super(context);
        setSKTMapApiKey("4296b5d5-5254-4cc1-89a0-e6dfbb467f30");
        this.setCompassMode(true);
        this.setOnClickListenerCallBack(new OnClickListenerCallback(){

            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                TrackTMapView.this.setCompassMode(true);
                return false;
            }
        });
    }
}
