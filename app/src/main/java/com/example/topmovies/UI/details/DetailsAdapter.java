package com.example.topmovies.UI.details;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.topmovies.Model.CastModel;
import com.example.topmovies.R;
import com.example.topmovies.databinding.ListItemCastsBinding;

import java.util.ArrayList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    private Context context;


    public DetailsAdapter(Context context) {

        this.context = context;

    }

    List<CastModel> castList = new ArrayList<>();


    @NonNull
    @Override
    public DetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemCastsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_casts, parent, false);
        return new ViewHolder(binding);


    }


    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.ViewHolder holder, int position) {
        CastModel castModel = castList.get(position);
        holder.binding.castProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));

        holder.binding.tvActressName.setText(castModel.getName().trim());
        holder.binding.tvCharacterName.setText(castModel.getCharacter());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CircleCrop());
          Glide.with(context)
                  .applyDefaultRequestOptions(requestOptions)
                  .load("https://image.tmdb.org/t/p/original" + castModel.getProfilePath())
                  .listener(new RequestListener<Drawable>() {
                      @Override
                      public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                          holder.binding.castProgressBar.setVisibility(View.GONE);
                          return false;
                      }

                      @Override
                      public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                          holder.binding.castProgressBar.setVisibility(View.GONE);
                          return false;
                      }
                  })
                  .error(R.drawable.placeholder)
                  .into(holder.binding.ivActress);

    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ListItemCastsBinding binding;

        public ViewHolder(@NonNull ListItemCastsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }

    public void addCastList(List<CastModel> castList) {
        this.castList.addAll(castList); //1234567
        notifyDataSetChanged();
    }


}
