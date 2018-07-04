package com.example.jsu48.a2018_trf_compete_client;

import android.content.Context;
import android.graphics.*;

import com.skt.Tmap.*;

import java.util.ArrayList;

public class DestTMapView extends TMapView {
    TMapData tmapdata;
    Bitmap mapMarker;
    TMapMarkerItem formerMarker = new TMapMarkerItem();
    CustLongClickCallback longCall;

    public DestTMapView(Context context, int src) {
        super(context);
        mapMarker = BitmapFactory.decodeResource(this.getResources(), src);
        mapMarker = Bitmap.createScaledBitmap(mapMarker, 50, 100, false);
        this.tmapdata = new TMapData();
        this.setSKTMapApiKey("4296b5d5-5254-4cc1-89a0-e6dfbb467f30");
        this.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                if (arrayList.size() > 0) {
                    formerMarker.setCanShowCallout(false);
                    formerMarker = arrayList.get(0);
                    formerMarker.setCanShowCallout(true);
                }
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }
        });
        longCall = new CustLongClickCallback(context);
        this.setOnLongClickListenerCallback(longCall);
    }

    class CustLongClickCallback implements TMapView.OnLongClickListenerCallback {
        MapViewer activity;

        @Override
        public void onLongPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint) {
            activity.closeSearch();
            try {
                tmapdata.convertGpsToAddress(tMapPoint.getLatitude(), tMapPoint.getLongitude(),
                        new TMapData.ConvertGPSToAddressListenerCallback() {
                            @Override
                            public void onConvertToGPSToAddress(String strAddress) {
                                activity.input.setText(strAddress);
                                activity.tMapView.searchLoc(strAddress);
                            }
                        });
            } catch (Exception e) {}
        }
        public CustLongClickCallback(Context activity) {
            this.activity = (MapViewer) activity;
        }
    }
    public void searchLoc(String keyWord) {

        tmapdata.findAllPOI(keyWord, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList poiItem) {
                final MapViewer cont = (MapViewer) DestTMapView.this.getContext();
                cont.resultNum = poiItem.size();
                removeAllMarkerItem();
                if (poiItem.size() > 0) {
                    for (int i = 0; i < poiItem.size(); i++) {
                        TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                        TMapMarkerItem markerItem1 = new TMapMarkerItem();
                        markerItem1.setIcon(mapMarker); // 마커 아이콘 지정
                        markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                        markerItem1.setTMapPoint(item.getPOIPoint()); // 마커의 좌표 지정
                        markerItem1.setName(item.getPOIName()); // 마커의 타이틀 지정
                        markerItem1.setCalloutTitle(item.getPOIName());
                        addMarkerItem("marker" + i, markerItem1);
                        cont.adap.add(item);
                    }
                }
                cont.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cont.adap.notifyDataSetChanged();
                        cont.setSearchResultIndicator();
                    }
                });
                if (poiItem.size() > 0) {
                    TMapPOIItem tCent = (TMapPOIItem) poiItem.get(0);
                    setCenterPoint(tCent.getPOIPoint().getLongitude(), tCent.getPOIPoint().getLatitude(), true);
                }
            }
        });

    }
}