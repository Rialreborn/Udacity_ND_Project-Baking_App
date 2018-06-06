package com.example.zane.bakingapp.objects;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Zane on 03/06/2018.
 */

public class Step implements Parcelable{

    private static final String LOG_TAG = Step.class.getSimpleName();

    private int mId;
    private String mShortDescription;
    private String mDescription;
    private String mVideoUrl;
    private String mImageUrl;

    public Step(int id, String shortDescription, String description, String videoUrl, String imageUrl){
        this.mId = id;
        this.mShortDescription = shortDescription;
        this.mDescription = description;
        this.mVideoUrl = videoUrl;
        this.mImageUrl = imageUrl;
    }

    protected Step(Parcel in) {
        mId = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoUrl = in.readString();
        mImageUrl = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public int getId() {return mId;}

    public String getShortDescription() {return mShortDescription;}

    public String getDescription() {return mDescription;}

    public Uri getVideoUrl() {
        return Uri.parse(mVideoUrl);
    }

    public Uri getImageUrl() {
        return Uri.parse(mImageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mShortDescription);
        dest.writeString(mDescription);
        dest.writeString(mVideoUrl);
        dest.writeString(mImageUrl);
    }
}
