package com.example.jsu48.a2018_trf_compete_client;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapView;

public class MapViewer extends AppCompatActivity {
    Button searchBtn,closeSearchResult;
    SearchAdapter adap;
    RecyclerView recyclerView;
    LinearLayoutManager layManager;
    LinearLayout tmapLayView;
    ConstraintLayout searchShow;
    TMapView tMapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        searchShow=findViewById(R.id.searchShow);
        tmapLayView=findViewById(R.id.tMapLayout);
        tMapView=new TMapView(this);
        tMapView.setSKTMapApiKey("4296b5d5-5254-4cc1-89a0-e6dfbb467f30");
        tmapLayView.addView(tMapView);
        searchBtn=findViewById(R.id.searchButton);
        closeSearchResult=findViewById(R.id.closeSearchResult);
        adap=new SearchAdapter();
        recyclerView=findViewById(R.id.searchResultView);
        layManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layManager);
        recyclerView.setAdapter(adap);
        //tMapView.setCenterPoint(35.134888, 129.103321);
        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                adap.deleteAll();
                searchShow.setVisibility(View.VISIBLE);
                recyclerView.bringToFront();
                //TODO get text from inputTextLayout
                for(int i=0;i<50;i++){
                    SearchResult t=new SearchResult();
                    t.setAdress("주소".toString()+i);
                    t.setName("지명"+i);
                    adap.add(t);
                }
            }
        });
        closeSearchResult.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                searchShow.setVisibility(View.GONE);
                adap.deleteAll();
            }
        });
    }
}
