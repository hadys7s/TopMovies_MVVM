package com.example.topmovies.UI.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.topmovies.Model.CastModel;
import com.example.topmovies.Model.MoviesModel;
import com.example.topmovies.R;
import com.example.topmovies.UI.Adapters.OnItemClicked;
import com.example.topmovies.UI.details.DetailsActivity;
import com.example.topmovies.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.rvPopularmovies.setLayoutManager(new GridLayoutManager(this, 3));
        MainAdapter mainAdapter = new MainAdapter(this, new OnItemClicked() {
            @Override
            public void onListItemCLicked(MoviesModel moviesModel) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("movieItem", moviesModel);
                startActivity(intent);
            }

            @Override
            public void onListItemCLickedcast(CastModel moviesModel) {

            }
        });
        activityMainBinding.rvPopularmovies.setAdapter(mainAdapter);

        MainActivityViewModel viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewModel.getMoviesList();
        int id = activityMainBinding.searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) activityMainBinding.searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        activityMainBinding.searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMainBinding.tvHeadName.setVisibility(View.INVISIBLE);
            }
        });


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
                    viewModel.getMoviesList();
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
                return true;
            }
        });


        viewModel.moviesList.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                mainAdapter.addMoviesList(moviesModels);
            }
        });


    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_details, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchViewmovie =findViewById(R.id.searchView);

        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                searchViewmovie.setVisibility(View.VISIBLE);
                return true;
            }
        });
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }
*/

}


