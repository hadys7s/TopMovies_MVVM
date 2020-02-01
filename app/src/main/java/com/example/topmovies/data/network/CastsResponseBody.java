
package com.example.topmovies.data.network;

import java.util.List;

import com.example.topmovies.Model.CastModel;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CastsResponseBody {

    @SerializedName("cast")
    private List<CastModel> mCastModel;

    public List<CastModel> getCast() {
        return mCastModel;
    }

    public void setCast(List<CastModel> castModel) {
        mCastModel = castModel;
    }



}
