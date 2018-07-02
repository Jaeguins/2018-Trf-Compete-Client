package com.example.jsu48.a2018_trf_compete_client;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    CustTMapView tMapView;

    TextView searchResult;
    EditText input;
    int resultNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    0 );
        }

        searchShow = findViewById(R.id.searchShow);
        tmapLayView = findViewById(R.id.tMapLayout);
        tMapView = new CustTMapView(this,R.drawable.marker);
        tmapLayView.addView(tMapView);
        searchBtn = findViewById(R.id.searchButton);
        closeSearchResult = findViewById(R.id.closeSearchResult);
        adap = new SearchAdapter(tMapView);
        recyclerView = findViewById(R.id.searchResultView);
        layManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layManager);
        recyclerView.setAdapter(adap);

        input =findViewById(R.id.inputLoc);
        searchResult = findViewById(R.id.searchResultHint);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adap.deleteAll();
                EditText z = findViewById(R.id.inputLoc);
                String searchName = z.getText().toString();
                tMapView.searchLoc(searchName);
                searchShow.setVisibility(View.VISIBLE);
            }
        });
        closeSearchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSearch();
            }
        });


        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        searchBtn.performClick();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //  tMapView.setCenterPoint()
    }



    public void closeSearch() {
        searchShow.setVisibility(View.GONE);
        adap.deleteAll();
    }

    public void setSearchResultIndicator() {
        if (resultNum == 0) searchResult.setText(R.string.searchResultZero);
        else {
            searchResult.setText(R.string.searchResultHint);
            searchResult.setText(searchResult.getText().toString() + resultNum);
        }
    }
}
class CustTMapView extends TMapView{
    TMapData tmapdata;
    Bitmap mapMarker;
    TMapMarkerItem formerMarker=new TMapMarkerItem();
    CustLongClickCallback longCall;
    public CustTMapView(Context context,int src){
        super(context);
        mapMarker = BitmapFactory.decodeResource(this.getResources(), src);
        mapMarker=Bitmap.createScaledBitmap(mapMarker, 50, 100, false);
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
        longCall=new CustLongClickCallback(context);
        this.setOnLongClickListenerCallback(longCall);
    }
    class CustLongClickCallback implements TMapView.OnLongClickListenerCallback{
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
        public CustLongClickCallback(Context activity){
            this.activity=(MapViewer)activity;
        }
    }
    public void searchLoc(String keyWord) {

        tmapdata.findAllPOI(keyWord, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList poiItem) {
                final MapViewer cont=(MapViewer)CustTMapView.this.getContext();
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
