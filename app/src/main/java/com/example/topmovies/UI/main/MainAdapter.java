package com.example.topmovies.UI.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.topmovies.Model.MoviesModel;
import com.example.topmovies.R;
import com.example.topmovies.UI.Adapters.OnItemClicked;
import com.example.topmovies.databinding.ListItemMoviesBinding;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private OnItemClicked onItemClicked;

    public MainAdapter(Context context, OnItemClicked onItemClicked) {

        this.context = context;
        this.onItemClicked = onItemClicked;

    }

    List<MoviesModel> moviesList = new ArrayList<>();


    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemMoviesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_movies, parent, false);
        return new ViewHolder(binding);

    }


    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        MoviesModel moviesModel = moviesList.get(position);
        try {


            if (!moviesModel.getTitle().isEmpty()) {
                holder.binding.tvTitle.setText(moviesModel.getTitle());

            }
        } catch (Exception e)
        {

            holder.binding.tvTitle.setText(moviesModel.getName());

        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        try {
            holder.binding.tvReleaseDate.setText(releaseYear(moviesModel.getReleaseDate()));
        }catch (Exception e){
            holder.binding.tvReleaseDate.setText("Unknown");

        }
        Glide.with(context)
                .applyDefaultRequestOptions(requestOptions)
                .load("https://image.tmdb.org/t/p/original/" + moviesModel.getBackdropPath())
                .into(holder.binding.ivPhoto);
    }

    private String releaseYear(String date) {
        return date.substring(0, 4);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ListItemMoviesBinding binding;

        public ViewHolder(@NonNull ListItemMoviesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClicked.onListItemCLicked(moviesList.get(getAdapterPosition()));

                }
            });
        }

    }

    public void addMoviesList(List<MoviesModel> moviesList) {
        this.moviesList.addAll(moviesList); //1234567
        notifyDataSetChanged();
    }

    public void  clear ()
    {
        this.moviesList.clear();
        notifyDataSetChanged();

    }


}
