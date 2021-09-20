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
import com.moksh.moviemania.R;
import com.moksh.moviemania.TvShows.Result;

import java.util.List;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.ViewHolder> {
    Context context;
    List<Result> resultList;

    public TvShowsAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.TvShowName.setText(resultList.get(position).getName());
        holder.Time.setText(resultList.get(position).getFirstAirDate());
        holder.rating.setText(resultList.get(position).getVoteAverage().toString().substring(0,3));
        String imageUrl = "https://image.tmdb.org/t/p/w500/"+resultList.get(position).getPosterPath();
        Glide.with(context)
                .load(imageUrl)
                .centerCrop()
                .into(holder.TvShowImage);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,TvShowsDetails.class);
                intent.putExtra("movie_id",resultList.get(holder.getAdapterPosition()).getId().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout linearLayout;
        TextView TvShowName,Time,rating;
        ImageView TvShowImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.movie_card);
            rating = itemView.findViewById(R.id.rating);
            TvShowImage = itemView.findViewById(R.id.movie_image);
            TvShowName = itemView.findViewById(R.id.movie_name);
            Time = itemView.findViewById(R.id.time);
        }
    }
}
