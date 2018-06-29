package com.example.jsu48.a2018_trf_compete_client;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<ResultViewHolder> {
    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result,viewGroup,false);
        return new ResultViewHolder(v);
    }
    List<SearchResult> items=new ArrayList<>();
    public void add(SearchResult data){
        items.add(data);
        notifyDataSetChanged();
    }
    public void deleteAll(){
        items.clear();
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder resultViewHolder, int i) {
        SearchResult result=items.get(i);
        resultViewHolder.mName.setText(result.getName());
        resultViewHolder.mAdress.setText(result.getAdress());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
