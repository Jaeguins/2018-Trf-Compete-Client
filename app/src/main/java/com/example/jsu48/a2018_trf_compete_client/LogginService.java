package com.example.jsu48.a2018_trf_compete_client;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;

import com.skt.Tmap.TMapTapi;

import java.util.Locale;

public class DrivingService extends Service {
    boolean isTmap = true;
    double longitude, latitude;//GPS
    double tLong,tLat;//tMap
    private TextToSpeech tts;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != -1) tts.setLanguage(Locale.KOREAN);
                tts.setPitch(1.0f);
                tts.setSpeechRate(1.0f);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String action=intent.getAction();
        if(action.equals("tMap")){
            tLong=intent.getDoubleExtra("longitude",1.0);
            tLat=intent.getDoubleExtra("latitude",1.0);
            TMapTapi t;
        }
        return startId;
    }

    @Override
    public void onDestroy() {

    }
    public void speak(String msg){
        tts.speak(msg,TextToSpeech.QUEUE_ADD,null,null);
    }
}
