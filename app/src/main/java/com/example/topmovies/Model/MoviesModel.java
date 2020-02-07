
package com.example.topmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
// we merge model(api) and table(database)
@Entity(tableName = "movies")
public class MoviesModel implements Parcelable {
    public MoviesModel() {
    }

    private int favorite = 0;

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    @Ignore
    @SerializedName("adult")
    private Boolean mAdult;
    @SerializedName("backdrop_path")
    private String mBackdropPath;
    @PrimaryKey
    @SerializedName("id")
    private Long mId;
    @Ignore
    @SerializedName("media_type")
    private String mMediaType;
    @Ignore
    @SerializedName("original_language")
    private String mOriginalLanguage;
    @Ignore
    @SerializedName("original_title")
    private String mOriginalTitle;
    @SerializedName("overview")
    private String mOverview;
    @Ignore
    @SerializedName("popularity")
    private Double mPopularity;
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("title")
    private String mTitle;
    @Ignore
    @SerializedName("video")
    private Boolean mVideo;
    @SerializedName("vote_average")
    private Double mVoteAverage;
    @Ignore
    @SerializedName("vote_count")
    private long mVoteCount;

    protected MoviesModel(Parcel in) {
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
            mVoteAverage = in.readDouble();
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

    public Double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
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
            dest.writeDouble(mVoteAverage);
        }
        dest.writeLong(mVoteCount);
    }
}
