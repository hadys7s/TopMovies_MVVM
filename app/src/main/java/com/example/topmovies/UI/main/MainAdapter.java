package com.example.topmovies.UI.main;

import android.content.Context;
import android.graphics.Color;
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
import com.example.topmovies.data.db.MoviesDb;
import com.example.topmovies.databinding.ListItemMoviesBinding;
import com.example.topmovies.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private OnItemClicked onItemClicked;
    MainActivityViewModel viewModel;

    public MainAdapter(Context context,MainActivityViewModel mainActivityViewModel,OnItemClicked onItemClicked) {

        this.context = context;
        this.onItemClicked = onItemClicked;
        viewModel=mainActivityViewModel;

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


        holder.binding.IbtnFav.setBackgroundColor(Color.TRANSPARENT);

        MoviesModel moviesModel = moviesList.get(position);
        holder.binding.tvAvgRate.setText(moviesModel.getVoteAverage()+"");

        if (viewModel.getFavValue(moviesModel.getId())==1)
        {
            holder.binding.IbtnFav.setImageResource(R.drawable.fav_inline);


        }else {
            holder.binding.IbtnFav.setImageResource(R.drawable.fav_outline);

        }


            try {


            if (!moviesModel.getTitle().isEmpty()) {
                holder.binding.tvTitle.setText(moviesModel.getTitle());

            }
        } catch (Exception e) {

         //   holder.binding.tvTitle.setText(moviesModel.getName());

        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        try {
            holder.binding.tvReleaseDate.setText(releaseYear(moviesModel.getReleaseDate()));
        } catch (Exception e) {
            holder.binding.tvReleaseDate.setText("Unknown");

        }
        Glide.with(context)
                .applyDefaultRequestOptions(requestOptions)
                .load(Constants.IMAGE_BASE_URL + moviesModel.getBackdropPath())
                .centerCrop()
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
            binding.IbtnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int check=viewModel.getFavValue(moviesList.get(getAdapterPosition()).getId());
                    if (check==0) {
                        viewModel.addfavmovie(moviesList.get(getAdapterPosition()));
                        binding.IbtnFav.setImageResource(R.drawable.fav_inline);
                        viewModel.updateFavValue(moviesList.get(getAdapterPosition()).getId(),1);
                    }else
                    {
                        //viewModel.removefavmovie(moviesList.get(getAdapterPosition()));
                        binding.IbtnFav.setImageResource(R.drawable.fav_outline);
                        viewModel.updateFavValue(moviesList.get(getAdapterPosition()).getId(),0);
                        addMoviesList(viewModel.getfavMoviesList());


                    }
                }


            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClicked.onListItemCLicked(moviesList.get(getAdapterPosition()));
                }
            });
        }

    }

    public void addMoviesList(List<MoviesModel> moviesList) {
        this.moviesList=moviesList;
        notifyDataSetChanged();
    }
    public void loadmore(List<MoviesModel> moviesList) {
        this.moviesList.addAll(moviesList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.moviesList.clear();
        notifyDataSetChanged();

    }


}
