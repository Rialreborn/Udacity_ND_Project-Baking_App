package com.example.zane.bakingapp.objects;

/**
 * Created by Zane on 03/06/2018.
 */

public class Ingredients {

    private int mQuantity;
    private String mMeasure;
    private String mIngredient;

    public Ingredients(int quantity, String measure, String ingredient){
        this.mQuantity = quantity;
        this.mMeasure = measure;
        this.mIngredient = ingredient;
    }

    public int getQuantity() {return mQuantity;}

    public String getMeasure() {return mMeasure;}

    public String getIngredient() {return mIngredient;}

}

