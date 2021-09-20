package com.moksh.moviemania.Activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moksh.moviemania.PopularMovies.Result;
import com.moksh.moviemania.R;

import java.util.List;

public class upComingAdapter extends  RecyclerView.Adapter<upComingAdapter.ViewHolder>{
    Context context;

    public upComingAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    List<Result> resultList;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.searched_result_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(resultList.get(position).getTitle());
        holder.type.setVisibility(View.INVISIBLE);
        holder.time.setText(resultList.get(position).getReleaseDate());
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/" + resultList.get(position).getPosterPath())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name,time,type;
        LinearLayout searchedResultClick;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            searchedResultClick = itemView.findViewById(R.id.searchedResultClick);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.searched_name);
            time = itemView.findViewById(R.id.searched_date);
            type = itemView.findViewById(R.id.searched_type);
        }
    }

}
