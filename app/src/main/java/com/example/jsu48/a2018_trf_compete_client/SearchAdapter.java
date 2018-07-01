package com.example.jsu48.a2018_trf_compete_client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<ResultViewHolder> {
    public SearchAdapter(TMapView index){
        this.index=index;
    }
    TMapView index;
    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result,viewGroup,false);
        return new ResultViewHolder(v);
    }
    List<TMapPOIItem> items=new ArrayList<>();
    public void add(TMapPOIItem data){
        items.add(data);
    }
    public void deleteAll(){
        items.clear();
    }
    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder resultViewHolder, int i) {
        TMapPOIItem result=items.get(i);
        resultViewHolder.mName.setText(result.getPOIName().toString());
        resultViewHolder.mAddress.setText(result.getPOIAddress().toString().replace("null",""));
        TMapPoint p=result.getPOIPoint();
        resultViewHolder.setLatitude(p.getLatitude());
        resultViewHolder.setLongitude(p.getLongitude());
        resultViewHolder.setInd(index);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
