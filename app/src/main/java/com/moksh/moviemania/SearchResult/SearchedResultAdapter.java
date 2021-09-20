package com.moksh.moviemania.SearchResult;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moksh.moviemania.Activities.MovieDetails;
import com.moksh.moviemania.Activities.TvShowsDetails;
import com.moksh.moviemania.R;

import java.util.List;

public class SearchedResultAdapter extends RecyclerView.Adapter<SearchedResultAdapter.ViewHolder>
{
    Context context;
    List<Result> resultList;

    public SearchedResultAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.searched_result_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(resultList.get(position).getPosterPath() != null){
            try {
                if (resultList.get(position).getMediaType().equals("tv")) {
                    holder.name.setText(resultList.get(position).getName());
                    holder.time.setText(resultList.get(position).getFirstAirDate().substring(0, 4));
                } else if(resultList.get(position).getMediaType().equals("movie")){
                    holder.name.setText(resultList.get(position).getTitle());
                    holder.time.setText(resultList.get(position).getReleaseDate().substring(0, 4));
                }
                holder.type.setText(resultList.get(position).getMediaType());
                String imageUrl = "https://image.tmdb.org/t/p/w500/" + resultList.get(position).getPosterPath();
                Glide.with(context)
                        .load(imageUrl)
                        .centerCrop()
                        .into(holder.image);
                holder.searchedResultClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (resultList.get(holder.getAdapterPosition()).getMediaType().equals("tv")){
                            Intent intent  = new Intent(context, TvShowsDetails.class);
                            intent.putExtra("movie_id",resultList.get(holder.getAdapterPosition()).getId().toString());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        else{
                            Intent intent  = new Intent(context, MovieDetails.class);
                            intent.putExtra("movie_id",resultList.get(holder.getAdapterPosition()).getId().toString());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                });
            } catch (Exception e) {
                Log.i("error at adapter", e.toString());
            }
        }
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
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
