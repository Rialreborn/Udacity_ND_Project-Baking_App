package com.example.zane.bakingapp;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.zane.bakingapp.objects.Ingredients;
import com.example.zane.bakingapp.objects.Recipe;
import com.example.zane.bakingapp.objects.Step;
import com.example.zane.bakingapp.recipe.StepsAdapter;
import com.example.zane.bakingapp.utils.Constants;
import com.example.zane.bakingapp.utils.LoadRecipes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentRecipeDetails extends Fragment {

    private static final String LOG_TAG = FragmentRecipeDetails.class.getSimpleName();


    @BindView(R.id.tv_details_ingredients)
    TextView mTvIngredients;
    @BindView(R.id.rv_steps)
    RecyclerView mRvSteps;
    @Nullable
    @BindView(R.id.tv_detail_title)
    TextView tvTitle;

    private int mPosition;
    Recipe mRecipe;
    private ArrayList<Recipe> mRecipeArrayList;
    ArrayList<Ingredients> mIngredientsList;
    ArrayList<Step> mStepsList;
    Context mContext;
    Uri mImageUri;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i(LOG_TAG, "MSG! onCreate() started");

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);

        mRecipeArrayList = MainActivity.recipeArray;
        mPosition = MainActivity.recipePosition;

        if(mRecipeArrayList != null && mRecipeArrayList.size() > 0) {
            mRecipe = mRecipeArrayList.get(mPosition);
            mIngredientsList = mRecipe.getIngredients();
            mStepsList = mRecipe.getSteps();
            mImageUri = mRecipe.getImageUri();

            setupAdapter();
            setupViews();
        }



        return view;
    }

    private void setupViews() {
        Log.i(LOG_TAG, "MSG! setupViews() started");
        mTvIngredients.setText(createIngredientList());

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            if(!MainActivity.tabletUsed) {
                toolbar.setTitle(mRecipe.getName());
            }
        }
        if (tvTitle != null){
            tvTitle.setText(mRecipe.getName());
        }
    }

    private void setupAdapter() {
        Log.i(LOG_TAG, "MSG! Recipe Name: " + mRecipe.getName());

        StepsAdapter stepsAdapter = new StepsAdapter(mStepsList, mContext, new StepsAdapter.OnStepClickListener() {
            @Override
            public void onStepClicked(int position) {
                FragmentStepDetails fragmentStepDetails = new FragmentStepDetails();
                MainActivity.stepPosition = position;

                if (MainActivity.tabletUsed) {
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(getActivity().findViewById(R.id.fragment_recipe_step_details).getId(), fragmentStepDetails, MainActivity.FRAGMENT_STEP_DETAILS_ID)
                            .commit();
                } else {
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(getActivity().findViewById(R.id.fragment_main).getId(), fragmentStepDetails, MainActivity.FRAGMENT_STEP_DETAILS_ID)
                            .commit();
                }
            }
        });

        mRvSteps.setHasFixedSize(true);
        mRvSteps.setAdapter(stepsAdapter);
    }

    private String createIngredientList() {
        StringBuilder builder = new StringBuilder();
        Ingredients ingredients;
        int quantity;
        String measure;
        String singleIngredient;

        for (int i = 0; i < mIngredientsList.size(); i++) {
            ingredients = mIngredientsList.get(i);
            quantity = ingredients.getQuantity();
            measure = ingredients.getMeasure();
            singleIngredient = ingredients.getIngredient();
            builder.append("\u2022 " + quantity + " " + measure + " " + singleIngredient + "\n");
        }

        String recipeList = builder.toString();
        updateWidgetInfo(recipeList);

        return recipeList;
    }

    private void updateWidgetInfo(String list) {
        Context context = getActivity().getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient);
        ComponentName thisWidget = new ComponentName(context, WidgetIngredient.class);
        remoteViews.setTextViewText(R.id.tv_widget_ingredients, list);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    public FragmentRecipeDetails() {
    }


}
