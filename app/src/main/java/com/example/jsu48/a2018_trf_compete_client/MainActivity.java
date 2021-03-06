package com.example.jsu48.a2018_trf_compete_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.skt.Tmap.TMapTapi;

public class MainActivity extends AppCompatActivity {
    Button mapBtn,freeBtn,statBtn,setBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new TMapTapi(this).setSKTMapAuthentication("4296b5d5-5254-4cc1-89a0-e6dfbb467f30");
        setContentView(R.layout.activity_initial);
        mapBtn=findViewById(R.id.mapButton);
        mapBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,MapViewer.class);
                i.putExtra("map",10);
                startActivity(i);
            }
        });
        freeBtn=findViewById(R.id.freeDriveButton);
        freeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,PreDrive.class);
                i.setAction("free");
                startActivity(i);
            }
        });
        statBtn=findViewById(R.id.statistics);
        statBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,Statistic.class);
                i.putExtra("stat",10);
                startActivity(i);
            }
        });
        setBtn=findViewById(R.id.settingButton);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,Settings.class);
                i.putExtra("set",10);
                startActivity(i);
            }
        });
    };
}
