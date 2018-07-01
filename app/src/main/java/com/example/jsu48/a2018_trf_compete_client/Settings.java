package com.example.jsu48.a2018_trf_compete_client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class Settings extends AppCompatActivity {
    FileOutputStream saveOut;
    FileInputStream saveIn;
    PrintWriter p;
    BufferedReader r;
    static String INITIALSAVE="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        try {
            saveIn=openFileInput("save.txt");
        } catch (FileNotFoundException e) {
            try {
                saveOut=openFileOutput("save.txt",MODE_PRIVATE);
                p=new PrintWriter(saveOut);
                p.print(INITIALSAVE);
                p.close();
                saveOut.close();
                saveIn=openFileInput("save.txt");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        r=new BufferedReader(new InputStreamReader(saveIn));
        //TODO read all savings
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            saveOut=openFileOutput("save.txt",MODE_PRIVATE);
            //TODO write all savings
            saveOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
