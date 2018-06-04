package com.example.zane.bakingapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zane on 03/06/2018.
 */

public class Ingredients implements Parcelable{

    private int mQuantity;
    private String mMeasure;
    private String mIngredient;

    public Ingredients(int quantity, String measure, String ingredient){
        this.mQuantity = quantity;
        this.mMeasure = measure;
        this.mIngredient = ingredient;
    }

    protected Ingredients(Parcel in) {
        mQuantity = in.readInt();
        mMeasure = in.readString();
        mIngredient = in.readString();
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

    public int getQuantity() {return mQuantity;}

    public String getMeasure() {return mMeasure;}

    public String getIngredient() {return mIngredient;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mQuantity);
        dest.writeString(mMeasure);
        dest.writeString(mIngredient);
    }
}

