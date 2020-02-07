
package com.example.topmovies.data.network;

import com.example.topmovies.Model.TrailerModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResponseBody {


    @SerializedName("results")
    private List<TrailerModel> mTrailerResponseBodies;

    public List<TrailerModel> getResults() {
        return mTrailerResponseBodies;
    }

    public void setResults(List<TrailerModel> trailerResponseBodies) {
        mTrailerResponseBodies = trailerResponseBodies;
    }

}
