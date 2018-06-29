package com.example.jsu48.a2018_trf_compete_client;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class MapViewer extends AppCompatActivity {
    Button searchBtn;
    SearchAdapter adap;
    RecyclerView recyclerView;
    LinearLayoutManager layManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        searchBtn=findViewById(R.id.searchButton);
        adap=new SearchAdapter();
        recyclerView=findViewById(R.id.searchResultView);
        layManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layManager);
        recyclerView.setAdapter(adap);
        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TextInputEditText k=(TextInputEditText)view.findViewById(R.id.textInputLayout2);

                for(int i=0;i<50;i++){
                    SearchResult t=new SearchResult();
                    t.setAdress("주소"+i);
                    t.setName("지명"+i);
                    adap.add(t);
                }

            }
        });
    }
}
