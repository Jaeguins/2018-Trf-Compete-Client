package com.example.jsu48.a2018_trf_compete_client;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skt.Tmap.TMapGpsManager;

public class MapViewer extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{
    Button searchBtn, closeSearchResult;
    SearchAdapter adap;
    RecyclerView recyclerView;
    LinearLayoutManager layManager;
    LinearLayout tmapLayView;
    ConstraintLayout searchShow;
    DestTMapView tMapView;
    TextView searchResult;
    EditText input;
    int resultNum = 0;
    boolean initialLocSetter=true;
    TMapGpsManager gpsM;
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
        gpsM=new TMapGpsManager(this);
        tMapView = new DestTMapView(this,R.drawable.marker,gpsM);
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        gpsM.CloseGps();
    }
    public void setSearchResultIndicator() {
        if (resultNum == 0) searchResult.setText(R.string.searchResultZero);
        else {
            searchResult.setText(R.string.searchResultHint);
            searchResult.setText(searchResult.getText().toString() + resultNum);
        }
    }
    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(),location.getLatitude());
        if(initialLocSetter){
            initialLocSetter=false;
            tMapView.setCenterPoint(location.getLongitude(),location.getLatitude());
        }
        Log.d("MapViewer",location.toString());
    }

}

