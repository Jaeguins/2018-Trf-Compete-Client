package com.example.jsu48.a2018_trf_compete_client;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PreDrive extends AppCompatActivity {
    ImageView img;
    TextView txt;
    int count = 0;
    Intent i;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predrive);
        i = getIntent();
        img = findViewById(R.id.imgView);
        txt = findViewById(R.id.textView);
        intent = new Intent(PreDrive.this, Tracking.class);
        findViewById(R.id.activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (count) {
                    case 0:
                        img.setImageResource(R.drawable.headlight);
                        txt.setText(R.string.headlight);
                        break;
                    case 1:
                        img.setImageResource(R.drawable.handbrake);
                        txt.setText(R.string.handbrake);
                        break;
                    case 2:
                        if ("map".equals(i.getAction())) {
                            intent.setAction("map");
                            intent.putExtra("longitude", i.getDoubleExtra("longitude", -1.0));
                            intent.putExtra("latitude", i.getDoubleExtra("latitude", -1.0));
                            startActivity(intent);
                            img.setImageResource(R.drawable.gear);
                            txt.setText(R.string.gearR);
                        } else {
                            intent.setAction("free");
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        img.setImageResource(R.drawable.handbrake);
                        txt.setText(R.string.handbrakeR);
                        break;
                    case 4:
                        img.setImageResource(R.drawable.headlight);
                        txt.setText(R.string.headlightR);
                        break;
                    case 5:
                        img.setImageResource(R.drawable.seatbelts);
                        txt.setText(R.string.seatbeltR);
                        break;
                    case 6:
                        PreDrive.this.finish();
                }
                count += 1;
            }
        });

    }
}