package com.example.topmovies.UI.main;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.topmovies.Model.MoviesModel;
import com.example.topmovies.R;
import com.example.topmovies.UI.details.DetailsActivity;
import com.example.topmovies.databinding.ActivityMainBinding;
import com.example.topmovies.utils.Constants;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    MainActivityViewModel viewModel;
    MainAdapter mainAdapter;
    ActivityMainBinding activityMainBinding;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dataBinding
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //recyclerView Manger

        activityMainBinding.rvPopularmovies.setLayoutManager(new GridLayoutManager(this, 3));
        //viewModelInstance
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        //adapter instance and on item Click Listener to move to details activity
        mainAdapter = new MainAdapter(this, viewModel,activityMainBinding, new OnItemClicked() {
            @Override
            public void onListItemCLicked(MoviesModel moviesModel) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(Constants.MOVIE_INTENT_KEY, moviesModel);
                startActivity(intent);
            }
        });
        activityMainBinding.rvPopularmovies.setAdapter(mainAdapter);
        //btn for open navigationDrawer and set background to be transparent
        activityMainBinding.btnOpenDrawer.setBackgroundColor(Color.TRANSPARENT);
        activityMainBinding.btnOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMainBinding.dlMain.openDrawer(Gravity.LEFT);
            }
        });
        //set the head for navigation drawer and
        View hView =  activityMainBinding.nvMain.getHeaderView(0);
        ImageView nav_header = hView.findViewById(R.id.iv_header);
        Glide.with(this)
                .load(R.drawable.headerpopcorn)
                .centerCrop()
                .into(nav_header);

        //refresh

       activityMainBinding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                returnToRightCatogery(activityMainBinding.tvHeadName.getText().toString());
                activityMainBinding.refresh.setRefreshing(false);
            }
        });



        //set the search texxt color to be black
        int id = activityMainBinding.searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = activityMainBinding.searchView.findViewById(id);
        textView.setTextColor(Color.BLACK);

        //searchlistiner to detect user typing and search
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
                    returnToRightCatogery(activityMainBinding.tvHeadName.getText().toString());

                } else {
                    mainAdapter.clear();
                    activityMainBinding.rvPopularmovies.setVisibility(View.VISIBLE);
                    activityMainBinding.emptyView.setVisibility(View.INVISIBLE);
                    viewModel.getSearchResults(activityMainBinding.searchView.getQuery().toString());
                }
                return true;
            }
        };
        activityMainBinding.searchView.setOnQueryTextListener(queryTextListener);

        // onSearch Icon click to hide Head name and drawer btn
        activityMainBinding.searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityMainBinding.tvHeadName.setVisibility(View.INVISIBLE);
                activityMainBinding.btnOpenDrawer.setVisibility(View.INVISIBLE);
                activityMainBinding.searchView.setIconifiedByDefault(false);
                activityMainBinding.btnBack.setVisibility(View.VISIBLE);
                activityMainBinding.btnBack.setBackgroundColor(Color.TRANSPARENT);
                activityMainBinding.searchView.setBackground(
                        getResources().getDrawable(R.drawable.rounded_search_bar));

            }
        });

        //back btn to close search view and back to the right catogery
        activityMainBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainAdapter.clear();
                returnToRightCatogery(activityMainBinding.tvHeadName.getText().toString());
                activityMainBinding.btnBack.setVisibility(View.GONE);
                activityMainBinding.searchView.setBackgroundColor(getResources().getColor(R.color.off_black));
                activityMainBinding.searchView.onActionViewCollapsed();
                activityMainBinding.tvHeadName.setVisibility(View.VISIBLE);
                activityMainBinding.btnOpenDrawer.setVisibility(View.VISIBLE);
                activityMainBinding.searchView.setEnabled(false);

            }

        });

        //get cached movies
        viewModel.getCashedMoviesList();
        viewModel.moviesCashedList.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                mainAdapter.addMoviesList(moviesModels);
            }
        });

        //get popular movies from the api
        viewModel.moviesList.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                mainAdapter.addMoviesList(moviesModels);
            }
        });


        // get searchresults from api
        viewModel.moviesSearchedList.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                mainAdapter.addMoviesList(moviesModels);
            }
        });
        viewModel.moviesList.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                moviesModels.remove(moviesModels.size()-1);
                moviesModels.remove(moviesModels.size()-2);
                mainAdapter.loadMore(moviesModels);
            }
        });

        //top movies load more list
        viewModel.getTopMoviesList(1);
        viewModel.loadmore.observe(this, new Observer<List<MoviesModel>>() {
            boolean check = false;
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                if (!check) {
                    viewModel.deleteCashedMoviesList();
                    mainAdapter.clear();
                    viewModel.cashMoviesList(moviesModels);
                    check=true;
                }
                mainAdapter.loadMore(moviesModels);
            }
        });

        // observe error
        viewModel.error.observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable throwable) {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        });
        //load more when scroll in home  with top movies
       activityMainBinding.rvPopularmovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean check = false;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
              if (!check) {
                  viewModel.getTopMoviesList(2);
                  viewModel.getTopMoviesList(3);
                  viewModel.getTopMoviesList(4);
                  viewModel.getTopMoviesList(5);
                  viewModel.getTopMoviesList(6);
                  viewModel.getTopMoviesList(7);
                  viewModel.getTopMoviesList(8);
                  viewModel.getTopMoviesList(9);
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

        //  set navigation categories   right data
        activityMainBinding.nvMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Home:
                        mainAdapter.clear();
                        activityMainBinding.rvPopularmovies.setVisibility(View.VISIBLE);
                        activityMainBinding.emptyView.setVisibility(View.INVISIBLE);
                        home();
                        activityMainBinding.dlMain.closeDrawer(GravityCompat.START);
                        activityMainBinding.tvHeadName.setText( getResources().getString(R.string.home));
                        break;

                    case R.id.favourites:
                        mainAdapter.clear();
                        if (!viewModel.getFavouritesMoviesList().isEmpty()) {
                            mainAdapter.addMoviesList(viewModel.getFavouritesMoviesList());
                            activityMainBinding.rvPopularmovies.setVisibility(View.VISIBLE);
                            activityMainBinding.emptyView.setVisibility(View.INVISIBLE);
                        }else
                        {
                            activityMainBinding.rvPopularmovies.setVisibility(View.INVISIBLE);
                            activityMainBinding.emptyView.setVisibility(View.VISIBLE);
                        }
                        activityMainBinding.dlMain.closeDrawer(GravityCompat.START);
                        activityMainBinding.tvHeadName.setText("Favourites");
                        break;
                    case R.id.comedy:
                        catogeryData(Constants.Comedy,  getResources().getString(R.string.comedy));
                        break;

                    case R.id.action:
                        catogeryData(Constants.ACTION,  getResources().getString(R.string.action));
                        break;
                    case R.id.romance:
                        catogeryData(Constants.Romance,  getResources().getString(R.string.romance));
                        break;
                    case R.id.science_fiction:
                        catogeryData(Constants.Science_Fiction,  getResources().getString(R.string.science_fiction));
                        break;
                    case R.id.crime:
                        catogeryData(Constants.Crime,  getResources().getString(R.string.crime));
                        break;
                    case R.id.drama:
                        catogeryData(Constants.Drama,  getResources().getString(R.string.drama));
                        break;
                    case R.id.horror:
                        catogeryData(Constants.Horror,  getResources().getString(R.string.horror));
                        break;


                }
                return true;
            }
        });
        activityMainBinding.nvMain.bringToFront();


    }


    public void catogeryData(String catogeryValue, String catogeryName) {
        mainAdapter.clear();
        activityMainBinding.rvPopularmovies.setVisibility(View.VISIBLE);
        activityMainBinding.emptyView.setVisibility(View.INVISIBLE);
        viewModel.getCategoriesList(catogeryValue,1);
        viewModel.getCategoriesList(catogeryValue,2);
        activityMainBinding.dlMain.closeDrawer(Gravity.LEFT);
        activityMainBinding.tvHeadName.setText(catogeryName);

    }


    // make sure from user that he wanna close app and if he wasn't home back to home
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        if (activityMainBinding.tvHeadName.getText().toString()!="Home")
        {
            mainAdapter.clear();
            home();
            activityMainBinding.tvHeadName.setText("Home");
        }
        else {
            builder.setTitle("Please confirm");
            builder.setMessage("Are you want to exit the app?");
            builder.setCancelable(true);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Do something when user want to exit the app
                    // Let allow the system to handle the event, such as exit the app
                    MainActivity.super.onBackPressed();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Do something when want to stay in the app
                    //Toast.makeText(getApplicationContext(), "thank you", Toast.LENGTH_LONG).show();
                }
            });

            // Create the alert dialog using alert dialog builder
            AlertDialog dialog = builder.create();

            // Finally, display the dialog when user press back button
            dialog.show();
        }
    }
    // when search closed take user to the right category
    public void returnToRightCatogery(String catogery)
    {
        switch (catogery) {
            case "Home":
                activityMainBinding.rvPopularmovies.setVisibility(View.VISIBLE);
                activityMainBinding.emptyView.setVisibility(View.INVISIBLE);
                mainAdapter.clear();
                home();
                activityMainBinding.dlMain.closeDrawer(GravityCompat.START);
                activityMainBinding.tvHeadName.setText( getResources().getString(R.string.home));
                break;

            case "Favourites":
                if (!viewModel.getFavouritesMoviesList().isEmpty()) {
                    mainAdapter.addMoviesList(viewModel.getFavouritesMoviesList());
                    activityMainBinding.rvPopularmovies.setVisibility(View.VISIBLE);
                    activityMainBinding.emptyView.setVisibility(View.INVISIBLE);
                }else
                {
                    activityMainBinding.rvPopularmovies.setVisibility(View.INVISIBLE);
                    activityMainBinding.emptyView.setVisibility(View.VISIBLE);
                }
                activityMainBinding.dlMain.closeDrawer(GravityCompat.START);
                activityMainBinding.tvHeadName.setText(getResources().getString(R.string.favourites));
                break;
            case "comedy":
                catogeryData(Constants.Comedy,  getResources().getString(R.string.comedy));
                break;

            case "Action":
                catogeryData(Constants.ACTION,  getResources().getString(R.string.action));
                break;
            case "Romance":
                catogeryData(Constants.Romance,  getResources().getString(R.string.romance));
                break;
            case "ScienceFiction":
                catogeryData(Constants.Science_Fiction, getResources().getString(R.string.science_fiction));
                break;
            case"Crime":
                catogeryData(Constants.Crime,  getResources().getString(R.string.crime));
                break;
            case "Drama":
                catogeryData(Constants.Drama,  getResources().getString(R.string.drama));
                break;
            case "Horror":
                catogeryData(Constants.Horror,  getResources().getString(R.string.horror));
                break;


        }




    }
    void home()
    {
        viewModel.getTopMoviesList(1);
        viewModel.getTopMoviesList(2);
        viewModel.getTopMoviesList(3);
        viewModel.getTopMoviesList(4);
        viewModel.getTopMoviesList(5);
        viewModel.getTopMoviesList(6);
        viewModel.getTopMoviesList(7);
        viewModel.getTopMoviesList(8);
        viewModel.getTopMoviesList(9);

    }

}


