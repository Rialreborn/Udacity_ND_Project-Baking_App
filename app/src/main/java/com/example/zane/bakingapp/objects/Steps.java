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

public class Steps implements Parcelable{

    private static final String LOG_TAG = Steps.class.getSimpleName();

    private int mId;
    private String mShortDescription;
    private String mDescription;
    private String mVideoUrl;
    private String mImageUrl;

    public Steps(int id, String shortDescription, String description, String videoUrl, String imageUrl){
        this.mId = id;
        this.mShortDescription = shortDescription;
        this.mDescription = description;
        this.mVideoUrl = videoUrl;
        this.mImageUrl = imageUrl;
    }

    protected Steps(Parcel in) {
        mId = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoUrl = in.readString();
        mImageUrl = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    public int getId() {return mId;}

    public String getShortDescription() {return mShortDescription;}

    public String getDescription() {return mDescription;}

    public URL getVideoUrl() {
        return convertStringToUrl(mVideoUrl);
    }

    public URL getImageUrl() {
        return convertStringToUrl(mImageUrl);
    }

    private URL convertStringToUrl(String urlString) {

        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Malformed URL: " + urlString);
        }

        return url;
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
