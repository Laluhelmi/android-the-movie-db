package com.hazard.movielist.ui.review.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hazard.ReviewModellist.ui.review.adapter.ReviewRVAdapter;

public class JavaTest<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context contex;
    public RecyclerView.ViewHolder giveHolder(RecyclerView.ViewHolder holder){
        return holder;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return giveHolder(new ReviewRVAdapter
                .DataViewHolder(LayoutInflater.from(contex)
                .inflate(android.R.layout.activity_list_item,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JavaTest<ReviewRVAdapter.DataViewHolder> holderJavaTest = new JavaTest<>();
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
