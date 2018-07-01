package com.example.jsu48.a2018_trf_compete_client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class MapViewer extends AppCompatActivity {
    Button searchBtn, closeSearchResult;
    SearchAdapter adap;
    RecyclerView recyclerView;
    LinearLayoutManager layManager;
    LinearLayout tmapLayView;
    ConstraintLayout searchShow;
    TMapView tMapView;
    TMapData tmapdata;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.tmap);
        bitmap = Bitmap.createScaledBitmap(bitmap, 50, 100, false);
        searchShow = findViewById(R.id.searchShow);
        tmapLayView = findViewById(R.id.tMapLayout);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("4296b5d5-5254-4cc1-89a0-e6dfbb467f30");
        tmapLayView.addView(tMapView);
        searchBtn = findViewById(R.id.searchButton);
        closeSearchResult = findViewById(R.id.closeSearchResult);
        adap = new SearchAdapter(tMapView);
        recyclerView = findViewById(R.id.searchResultView);
        layManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layManager);
        recyclerView.setAdapter(adap);
        tmapdata = new TMapData();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adap.deleteAll();
                EditText z = findViewById(R.id.inputLoc);
                String searchName = z.getText().toString();
                searchLoc(searchName);
                searchShow.setVisibility(View.VISIBLE);
            }
        });
        closeSearchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchShow.setVisibility(View.GONE);
                adap.deleteAll();
            }
        });
        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint) {
                try {
                    tmapdata.convertGpsToAddress(tMapPoint.getLatitude(), tMapPoint.getLongitude(),
                            new TMapData.ConvertGPSToAddressListenerCallback() {
                                @Override
                                public void onConvertToGPSToAddress(String strAddress) {
                                    EditText z = findViewById(R.id.inputLoc);
                                    z.setText(strAddress);
                                    searchLoc(strAddress);
                                }
                            });
                } catch (Exception e) {

                }
            }
        });
    }
    public void searchLoc(String keyWord){
        tmapdata.findAllPOI(keyWord, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList poiItem) {
                if(poiItem.size()==0)return;
                tMapView.removeAllMarkerItem();
                for (int i = 0; i < poiItem.size(); i++) {
                    TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                    TMapMarkerItem markerItem1 = new TMapMarkerItem();
                    markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                    markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    markerItem1.setTMapPoint(item.getPOIPoint()); // 마커의 좌표 지정
                    markerItem1.setName(item.getPOIName()); // 마커의 타이틀 지정
                    tMapView.addMarkerItem("marker" + i, markerItem1);
                    adap.add(item);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adap.notifyDataSetChanged();
                    }
                });
                TMapPOIItem tCent = (TMapPOIItem) poiItem.get(0);
                tMapView.setCenterPoint(tCent.getPOIPoint().getLongitude(), tCent.getPOIPoint().getLatitude(), true);
            }
        });
    }
}
