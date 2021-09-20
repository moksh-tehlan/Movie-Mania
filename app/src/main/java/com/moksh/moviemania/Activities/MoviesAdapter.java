package com.moksh.moviemania.Activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moksh.moviemania.PopularMovies.MovieList;
import com.moksh.moviemania.PopularMovies.Result;
import com.moksh.moviemania.R;

import java.util.List;
import java.util.zip.Inflater;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>{
    Context context;
    List<Result> resultList;

    public MoviesAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.movie_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.movieName.setText(resultList.get(position).getTitle());
            holder.Time.setText(resultList.get(position).getReleaseDate());
            holder.rating.setText(resultList.get(position).getVoteAverage().toString().substring(0,3));
            String imageUrl = "https://image.tmdb.org/t/p/w500/"+resultList.get(position).getPosterPath();
            Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .into(holder.movieImage);
            holder.movieCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,MovieDetails.class);
                    String movieId = resultList.get(holder.getAdapterPosition()).getId().toString();
                    intent.putExtra("movie_id",movieId);
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
        TextView movieName,Time,rating;
        ImageView movieImage;
        LinearLayout movieCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rating  = itemView.findViewById(R.id.rating);
            movieCard = itemView.findViewById(R.id.movie_card);
            movieImage = itemView.findViewById(R.id.movie_image);
            movieName = itemView.findViewById(R.id.movie_name);
            Time = itemView.findViewById(R.id.time);
        }
    }
}
