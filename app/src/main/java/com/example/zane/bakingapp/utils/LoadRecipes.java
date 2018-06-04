package com.example.zane.bakingapp.utils;

import android.os.AsyncTask;

import com.example.zane.bakingapp.objects.Ingredients;
import com.example.zane.bakingapp.objects.Recipe;
import com.example.zane.bakingapp.objects.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

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
