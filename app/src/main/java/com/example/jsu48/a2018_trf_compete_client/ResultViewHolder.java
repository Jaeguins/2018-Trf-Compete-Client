package com.example.jsu48.a2018_trf_compete_client;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

class ResultViewHolder extends RecyclerView.ViewHolder{
    public TextView mAdress,mName;
    public Button dirBtn;
    double longitude,latitude;
    public ResultViewHolder(View itemView){
        super(itemView);
        mAdress=(TextView)itemView.findViewById(R.id.resultAdress);
        mName=(TextView)itemView.findViewById(R.id.resultTitle);
        dirBtn=(Button)itemView.findViewById(R.id.directionButton);
        dirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent("map");
                i.putExtra("longitude",longitude);
                i.putExtra("latitude",latitude);
            }
        });
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
