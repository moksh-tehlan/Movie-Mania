package com.moksh.moviemania.Misclenious;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moksh.moviemania.R;
import com.moksh.moviemania.TvDetails.Genre;

import java.util.List;

public class GenreAdapterTv extends RecyclerView.Adapter<GenreAdapterTv.ViewHolder> {
    Context context;
    List<Genre> genreList;

    public GenreAdapterTv(Context context, List<Genre> genreList) {
        this.context = context;
        this.genreList = genreList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.generes,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.genreText.setText(genreList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView genreText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            genreText = itemView.findViewById(R.id.genreText);
        }
    }

}
