package com.example.zane.bakingapp.objects;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Zane on 03/06/2018.
 */

public class Recipe implements Parcelable{

    private static final String LOG_TAG = Recipe.class.getSimpleName();


    private int mId;
    private String mName;
    private ArrayList<Ingredients> mIngredients;
    private ArrayList<Steps> mSteps;
    private int mServings;
    private String mImageUrlString;

    public Recipe(int id, String name, ArrayList<Ingredients> ingredients, ArrayList<Steps> steps, int servings, String imageUrlString){
        this.mId = id;
        this.mName = name;
        this.mIngredients = ingredients;
        this.mSteps = steps;
        this.mServings = servings;
        this.mImageUrlString = imageUrlString;
    }

    protected Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mServings = in.readInt();
        mImageUrlString = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getId() {return mId;}

    public String getName() {return mName;}

    public ArrayList<Ingredients> getIngredients() {return mIngredients;}

    public ArrayList<Steps> getSteps() {return mSteps;}

    public int getServings() {return mServings;}

    public Uri getImageUri() {
        return Uri.parse(mImageUrlString);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeInt(mServings);
        dest.writeString(mImageUrlString);
    }
}
