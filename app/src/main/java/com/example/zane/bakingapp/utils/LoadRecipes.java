package com.example.zane.bakingapp.utils;

import android.os.AsyncTask;

import com.example.zane.bakingapp.objects.Recipe;

import java.util.ArrayList;

/**
 * Created by Zane on 03/06/2018.
 */

public class LoadRecipes extends AsyncTask<Void, Void, ArrayList<Recipe>> {

    public interface AfterTaskComplete {
        void afterRecipesLoaded(ArrayList<Recipe> recipeArrayList);
    }

     private AfterTaskComplete mAfterTaskComplete;

    public LoadRecipes(AfterTaskComplete afterTaskComplete) {
        this.mAfterTaskComplete = afterTaskComplete;
    }

    @Override
    protected ArrayList<Recipe> doInBackground(Void... voids) {
        return JsonUtils.getRecipeArrayList();
    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> recipeArrayList) {
        super.onPostExecute(recipeArrayList);

        mAfterTaskComplete.afterRecipesLoaded(recipeArrayList);
    }


}
