package com.example.topmovies.UI.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topmovies.Model.CastModel;
import com.example.topmovies.Model.MoviesModel;
import com.example.topmovies.R;
import com.example.topmovies.UI.Adapters.OnItemClicked;
import com.example.topmovies.UI.details.DetailsActivity;
import com.example.topmovies.databinding.ActivityMainBinding;
import com.example.topmovies.utils.Constants;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    MainActivityViewModel viewModel;
    MainAdapter mainAdapter;
    ActivityMainBinding activityMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.rvPopularmovies.setLayoutManager(new GridLayoutManager(this, 3));
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainAdapter = new MainAdapter(this, viewModel, new OnItemClicked() {
            @Override
            public void onListItemCLicked(MoviesModel moviesModel) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(Constants.MOVIE_INTENT_KEY, moviesModel);
                startActivity(intent);

            }

            @Override
            public void onlistitemclickedcast(CastModel moviesModel) {

            }
        });
        activityMainBinding.rvPopularmovies.setAdapter(mainAdapter);
        activityMainBinding.btnOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMainBinding.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        int id = activityMainBinding.searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = activityMainBinding.searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        activityMainBinding.searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMainBinding.tvHeadName.setVisibility(View.INVISIBLE);
                activityMainBinding.btnOpenDrawer.setVisibility(View.INVISIBLE);
            }
        });

        //search
        SearchView.OnQueryTextListener queryTextListener
                = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mainAdapter.clear();
                viewModel.getSearchResults(activityMainBinding.searchView.getQuery().toString());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    mainAdapter.clear();
                    viewModel.getCashedMoviesList();
                    viewModel.getMoviesList();
                    viewModel.gettopMoviesList();

                } else {

                    mainAdapter.clear();
                    viewModel.getSearchResults(activityMainBinding.searchView.getQuery().toString());
                }
                return true;
            }
        };
        activityMainBinding.searchView.setOnQueryTextListener(queryTextListener);

        activityMainBinding.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                activityMainBinding.searchView.onActionViewCollapsed();
                activityMainBinding.tvHeadName.setVisibility(View.VISIBLE);
                activityMainBinding.btnOpenDrawer.setVisibility(View.VISIBLE);
                activityMainBinding.searchView.setEnabled(false);

                return true;
            }
        });

        //databaselist
        viewModel.getCashedMoviesList();
        viewModel.moviesCashedList.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                mainAdapter.addMoviesList(moviesModels);
            }
        });

        //popularmovies
        viewModel.getMoviesList();
        viewModel.moviesList.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                viewModel.deleteCashedMoviesList();
                mainAdapter.addMoviesList(moviesModels);
                viewModel.cashMoviesList(moviesModels);
            }
        });

        //searchresults
        viewModel.moviesSearchedList.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                mainAdapter.addMoviesList(moviesModels);
            }
        });

        //top movies load more list
        viewModel.loadmore.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                mainAdapter.loadmore(moviesModels);
                viewModel.cashMoviesList(moviesModels);
            }
        });
        //error
        viewModel.error.observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable throwable) {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        });
        //load more by top movies
        activityMainBinding.rvPopularmovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean check = false;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!check) {
                    viewModel.gettopMoviesList();
                    check = true;
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        //observe internet connection
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                // network
                viewModel.getMoviesList();
                viewModel.gettopMoviesList();

            }

            @Override
            public void onLost(Network network) {
                // network unavailable
            }
        };

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }

        activityMainBinding.designNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Home:
                        viewModel.getMoviesList();
                        viewModel.gettopMoviesList();
                        activityMainBinding.drawerLayout.closeDrawer(Gravity.LEFT);
                        activityMainBinding.tvHeadName.setText("Home");
                        break;

                    case R.id.favourites:
                        mainAdapter.addMoviesList(viewModel.getfavMoviesList());
                        activityMainBinding.drawerLayout.closeDrawer(Gravity.LEFT);
                        activityMainBinding.tvHeadName.setText("Favourites");
                        break;
                    case R.id.comedy:
                        catogeryData(Constants.Comedy, "Comedy");
                        break;

                    case R.id.action:
                        catogeryData(Constants.ACTION, "Action");
                        break;
                    case R.id.romance:
                        catogeryData(Constants.Romance, "Romance");
                        break;
                    case R.id.science_fiction:
                        catogeryData(Constants.Science_Fiction, "ScienceFiction");
                        break;
                    case R.id.crime:
                        catogeryData(Constants.Crime, "Crime");
                        break;
                    case R.id.drama:
                        catogeryData(Constants.Drama, "Drama");
                        break;
                    case R.id.horror:
                        catogeryData(Constants.Horror, "Horror");
                        break;


                }
                return true;
            }
        });
        activityMainBinding.designNavigationView.bringToFront();


    }

    public void catogeryData(String catogeryValue, String catogeryName) {
        viewModel.getCatogriesList(catogeryValue);
        activityMainBinding.drawerLayout.closeDrawer(Gravity.LEFT);
        activityMainBinding.tvHeadName.setText(catogeryName);

    }

}


