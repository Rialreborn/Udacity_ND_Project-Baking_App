package com.example.zane.bakingapp.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.zane.bakingapp.objects.Ingredients;
import com.example.zane.bakingapp.objects.Recipe;
import com.example.zane.bakingapp.objects.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Zane on 03/06/2018.
 */

public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    public static ArrayList<Recipe> getRecipeArrayList() {
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();

        try {
            String jsonResponse = getJsonResponse();
            JSONArray jsonArray = new JSONArray(jsonResponse);

            Log.i(LOG_TAG,
                    "Name of Recipe Array(0): " + jsonArray.getJSONObject(0).optString(Constants.JSON_RECIPE_NAME) +
                    ". \nName in Recipe Array(1): " + jsonArray.getJSONObject(1).optString(Constants.JSON_RECIPE_NAME));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipe = jsonArray.getJSONObject(i);

                int id = recipe.optInt(Constants.JSON_RECIPE_ID);
                String name = recipe.optString(Constants.JSON_RECIPE_NAME);
                ArrayList<Ingredients> ingredientsArrayList = getIngredients(recipe);
                ArrayList<Steps> stepsArrayList = getSteps(recipe);
                int servings = recipe.optInt(Constants.JSON_RECIPE_SERVINGS);
                String imageUrl = recipe.optString(Constants.JSON_RECIPE_IMAGE);

                recipeArrayList.add(
                        new Recipe(id, name, ingredientsArrayList, stepsArrayList, servings, imageUrl));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeArrayList;
    }

    private static ArrayList<Ingredients> getIngredients(JSONObject recipe){

        ArrayList<Ingredients> ingredientsArrayList = new ArrayList<>();

        try {
            JSONArray jsonIngredientArray = recipe.getJSONArray(Constants.JSON_RECIPE_INGREDIENTS_ARRAY);

            for (int i = 0; i < jsonIngredientArray.length(); i++) {
                JSONObject jsonObject = jsonIngredientArray.getJSONObject(i);
                int quantity = jsonObject.optInt(Constants.JSON_INGREDIENTS_QUANTITY);
                String measurement = jsonObject.optString(Constants.JSON_INGREDIENTS_MEASURE);
                String ingredient = jsonObject.optString(Constants.JSON_INGREDIENTS_INGREDIENT);

                ingredientsArrayList.add(new Ingredients(quantity, measurement, ingredient));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ingredientsArrayList;
    }

    private static ArrayList<Steps> getSteps(JSONObject steps){

        ArrayList<Steps> stepsArrayList = new ArrayList<>();

        try {
            JSONArray jsonStepsArray = steps.getJSONArray(Constants.JSON_RECIPE_STEPS_ARRAY);

            for (int i = 0; i < jsonStepsArray.length(); i++) {
                JSONObject jsonObject = jsonStepsArray.getJSONObject(i);
                int id = jsonObject.optInt(Constants.JSON_STEPS_ID);
                String shortDescription = jsonObject.optString(Constants.JSON_STEPS_SHORT_DESCRIPTION);
                String longDescription = jsonObject.optString(Constants.JSON_STEPS_LONG_DESCRIPTION);
                String videoURL = jsonObject.optString(Constants.JSON_STEPS_VIDEO_URL);
                String thumbnailURL = jsonObject.optString(Constants.JSON_STEPS_THUMBNAIL_URL);

                stepsArrayList.add(new Steps(id, shortDescription, longDescription, videoURL, thumbnailURL));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stepsArrayList;
    }



    private static String getJsonResponse() throws IOException {

        URL url = null;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }

        }  finally {
            urlConnection.disconnect();
        }
    }
}
