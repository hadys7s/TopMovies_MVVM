package com.example.topmovies.UI.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.topmovies.Model.CastModel;
import com.example.topmovies.R;
import com.example.topmovies.UI.Adapters.OnItemClicked;
import com.example.topmovies.databinding.ListItemCastsBinding;

import java.util.ArrayList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    private Context context;
    private OnItemClicked onItemClicked;


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

        holder.binding.tvActressName.setText(castModel.getName().trim());
        holder.binding.tvCharacterName.setText(castModel.getCharacter());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CircleCrop());
          Glide.with(context)
                  .applyDefaultRequestOptions(requestOptions)
                  .load("https://image.tmdb.org/t/p/original" + castModel.getProfilePath())
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClicked.onlistitemclickedcast(castList.get(getAdapterPosition()));

                }
            });
        }

    }

    public void addCastList(List<CastModel> castList) {
        this.castList.addAll(castList); //1234567
        notifyDataSetChanged();
    }


}
