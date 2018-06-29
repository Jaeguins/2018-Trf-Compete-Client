package com.example.jsu48.a2018_trf_compete_client;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

class ResultViewHolder extends RecyclerView.ViewHolder{
    public TextView mAdress,mName;
    public Button dirBtn;
    public ResultViewHolder(View itemView){
        super(itemView);
        mAdress=(TextView)itemView.findViewById(R.id.resultAdress);
        mName=(TextView)itemView.findViewById(R.id.resultTitle);
        dirBtn=(Button)itemView.findViewById(R.id.directionButton);
    }
}
