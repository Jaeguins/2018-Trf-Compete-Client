package com.example.jsu48.a2018_trf_compete_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Driving extends AppCompatActivity {
    Button cancelButton;
    TextView drivingTitle,drivingCaution;
    ImageView img;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);
        cancelButton=findViewById(R.id.cancelButton);
        img=findViewById(R.id.img);
        drivingTitle=findViewById(R.id.drivingTitle);
        drivingCaution=findViewById(R.id.drivingCaution);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Driving.this.finish();
            }
        });
        i=getIntent();
        String state=i.getAction();
        if(state.equals("map")){
            drivingTitle.setText(R.string.map);
            drivingCaution.setText(R.string.TmapCaution);
            img.setImageResource(R.drawable.tmap);
            //TODO add tmap calling recording
        }else{
            drivingTitle.setText(R.string.freeDrive);
            drivingCaution.setText(R.string.cautionFreedrive);
            img.setImageResource(R.drawable.freedrive);
            //TODO add recording
        }
    }
}
