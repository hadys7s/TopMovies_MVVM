
package com.example.topmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;@SuppressWarnings("unused")

public class MoviesModel implements Parcelable {

   @SerializedName("name")
    private  String mName;
    @SerializedName("first_air_date")
    private String mFirstairDate;
    @SerializedName("adult")
    private Boolean mAdult;
    @SerializedName("backdrop_path")
    private String mBackdropPath;
    @SerializedName("genre_ids")
    private List<Long> mGenreIds;
    @SerializedName("id")
    private Long mId;
    @SerializedName("media_type")
    private String mMediaType;
    @SerializedName("original_language")
    private String mOriginalLanguage;
    @SerializedName("original_title")
    private String mOriginalTitle;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("popularity")
    private Double mPopularity;
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("video")
    private Boolean mVideo;
    @SerializedName("vote_average")
    private Float mVoteAverage;
    @SerializedName("vote_count")
    private long mVoteCount;

    protected MoviesModel(Parcel in) {
        mName = in.readString();
        mFirstairDate = in.readString();
        byte tmpMAdult = in.readByte();
        mAdult = tmpMAdult == 0 ? null : tmpMAdult == 1;
        mBackdropPath = in.readString();
        if (in.readByte() == 0) {
            mId = null;
        } else {
            mId = in.readLong();
        }
        mMediaType = in.readString();
        mOriginalLanguage = in.readString();
        mOriginalTitle = in.readString();
        mOverview = in.readString();
        if (in.readByte() == 0) {
            mPopularity = null;
        } else {
            mPopularity = in.readDouble();
        }
        mPosterPath = in.readString();
        mReleaseDate = in.readString();
        mTitle = in.readString();
        byte tmpMVideo = in.readByte();
        mVideo = tmpMVideo == 0 ? null : tmpMVideo == 1;
        if (in.readByte() == 0) {
            mVoteAverage = null;
        } else {
            mVoteAverage = in.readFloat();
        }
        mVoteCount = in.readLong();
    }

    public static final Creator<MoviesModel> CREATOR = new Creator<MoviesModel>() {
        @Override
        public MoviesModel createFromParcel(Parcel in) {
            return new MoviesModel(in);
        }

        @Override
        public MoviesModel[] newArray(int size) {
            return new MoviesModel[size];
        }
    };

    public Boolean getAdult() {
        return mAdult;
    }

    public void setAdult(Boolean adult) {
        mAdult = adult;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }

    public List<Long> getGenreIds() {
        return mGenreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        mGenreIds = genreIds;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getMediaType() {
        return mMediaType;
    }

    public void setMediaType(String mediaType) {
        mMediaType = mediaType;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        mOriginalLanguage = originalLanguage;
    }

    public String getFirstairDate() {
        return mFirstairDate;
    }

    public String getName() {
        return mName;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public Double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(Double popularity) {
        mPopularity = popularity;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Boolean getVideo() {
        return mVideo;
    }

    public void setVideo(Boolean video) {
        mVideo = video;
    }

    public Float getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        mVoteAverage = voteAverage;
    }

    public long getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(Long voteCount) {
        mVoteCount = voteCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mFirstairDate);
        dest.writeByte((byte) (mAdult == null ? 0 : mAdult ? 1 : 2));
        dest.writeString(mBackdropPath);
        if (mId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mId);
        }
        dest.writeString(mMediaType);
        dest.writeString(mOriginalLanguage);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOverview);
        if (mPopularity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mPopularity);
        }
        dest.writeString(mPosterPath);
        dest.writeString(mReleaseDate);
        dest.writeString(mTitle);
        dest.writeByte((byte) (mVideo == null ? 0 : mVideo ? 1 : 2));
        if (mVoteAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(mVoteAverage);
        }
        dest.writeLong(mVoteCount);
    }
}
