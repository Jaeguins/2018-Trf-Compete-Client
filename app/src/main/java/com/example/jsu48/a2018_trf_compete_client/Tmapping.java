package com.example.jsu48.a2018_trf_compete_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Tmapping extends AppCompatActivity {
    Button cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmapping);
        cancelButton=findViewById(R.id.cancelButton2);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Tmapping.this,SubDrive.class);
                startActivity(i);
            }
        });
    }
}