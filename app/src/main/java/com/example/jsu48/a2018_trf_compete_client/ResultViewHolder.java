package com.example.jsu48.a2018_trf_compete_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skt.Tmap.TMapView;

class ResultViewHolder extends RecyclerView.ViewHolder{
    public TextView mAddress,mName;
    public Button dirBtn;
    public TMapView ind;
    double longitude,latitude;
    public ResultViewHolder(View itemView){
        super(itemView);
        mAddress=(TextView)itemView.findViewById(R.id.resultAdress);
        mName=(TextView)itemView.findViewById(R.id.resultTitle);
        dirBtn=(Button)itemView.findViewById(R.id.directionButton);
        dirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),PreDrive.class);
                intent.setAction("map");
                intent.putExtra("longitude",longitude);
                intent.putExtra("latitude",latitude);
                view.getContext().startActivity(intent);
                ((AppCompatActivity)view.getContext()).finish();
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ind.setCenterPoint(longitude,latitude,true);
            }
        });
    }
    public void setInd(TMapView ind){
        this.ind=ind;
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
