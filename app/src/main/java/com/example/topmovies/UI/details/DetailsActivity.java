package com.example.topmovies.UI.details;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.topmovies.Model.CastModel;
import com.example.topmovies.Model.MoviesModel;
import com.example.topmovies.Model.TrailerModel;
import com.example.topmovies.R;
import com.example.topmovies.databinding.MovieDetailsBinding;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieDetailsBinding movieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.movie_details);
        YouTubePlayerSupportFragmentX frag =
                (YouTubePlayerSupportFragmentX) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);


        DetailsAdapter detailsAdapter = new DetailsAdapter(this);
        RequestOptions castCircleCrop = new RequestOptions();
        castCircleCrop = castCircleCrop.transforms(new CenterCrop(), new RoundedCorners(120));


        movieDetailsBinding.rvCast.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        movieDetailsBinding.rvCast.setAdapter(detailsAdapter);
        MoviesModel moviesObject = getIntent().getParcelableExtra("movieItem");
        if (!moviesObject.getTitle().isEmpty())
            movieDetailsBinding.tvTitle.setText(moviesObject.getTitle());
        else {
            movieDetailsBinding.tvTitle.setText(moviesObject.getName());
        }


        movieDetailsBinding.tvDescription.setText(textHead("Description : " + moviesObject.getOverview(), 13));


        if (!moviesObject.getReleaseDate().isEmpty() | moviesObject.getReleaseDate() != "null") {
            movieDetailsBinding.tvReleaseDate.setText(textHead("Release Date : " + moviesObject.getReleaseDate(), 14));
        } else {
            movieDetailsBinding.tvReleaseDate.setText("Unkown");

        }
        movieDetailsBinding.tvAvgRate.setText(stringToFloat(moviesObject.getVoteAverage()) + "/10");
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/original/" + moviesObject.getPosterPath())
                .transform(new MultiTransformation(new BlurTransformation()))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        movieDetailsBinding.clDetailsBackground.setBackground(resource);
                    }
                });

        Glide.with(this)
                .applyDefaultRequestOptions(castCircleCrop)
                .load("https://image.tmdb.org/t/p/original/" + moviesObject.getBackdropPath())
                .into(movieDetailsBinding.ivMovieImage);


        DetailsActivityViewModel detailsActivityViewModel = ViewModelProviders.of(this).get(DetailsActivityViewModel.class);
        detailsActivityViewModel.getcastlist(moviesObject.getId());
        detailsActivityViewModel.getTrailerUrl(moviesObject.getId());


        detailsActivityViewModel.videolist.observe(this, new Observer<TrailerModel>() {
            @Override
            public void onChanged(TrailerModel trailerModel) {
                try {


                    if (trailerModel.getName() != null && !trailerModel.getName().isEmpty()) {


                        movieDetailsBinding.trailerName.setText(trailerModel.getName());

                    }
                } catch (NullPointerException e) {

                    movieDetailsBinding.trailerName.setVisibility(View.GONE);

                    movieDetailsBinding.tvTrailerhead.setVisibility(View.GONE);

                }
                try {


                    if (trailerModel.getKey() != null && !trailerModel.getKey().isEmpty()) {
                        if (frag != null) {
                            frag.initialize(trailerModel.getKey(), new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                                    youTubePlayer.cueVideo(trailerModel.getKey());

                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                                    movieDetailsBinding.llHideVideo.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                } catch (NullPointerException e) {


                    movieDetailsBinding.llHideVideo.setVisibility(View.GONE);


                }


            }
        });
        detailsActivityViewModel.CastList.observe(this, new Observer<List<CastModel>>() {
            @Override
            public void onChanged(List<CastModel> castModels) {
                detailsAdapter.addCastList(castModels);


            }
        });


    }



    private Spannable textHead(String text, int end) {
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(
                new ForegroundColorSpan(getResources().getColor(R.color.pink)), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(
                new RelativeSizeSpan(1.40f), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;


    }

    private String stringToFloat(float text) {

        return Float.toString(text);

    }

    public void playVideo(final String videoId, YouTubePlayerSupportFragmentX youTubePlayerView) {
        youTubePlayerView.initialize("AIzaSyAsZTzRJC5O7C_aKjVpFVEWnwH3FC8_fdM",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(videoId);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }


}
