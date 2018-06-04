package com.example.zane.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.zane.bakingapp.objects.Ingredients;
import com.example.zane.bakingapp.objects.Recipe;
import com.example.zane.bakingapp.objects.Steps;
import com.example.zane.bakingapp.recipe.RecipeDetails;
import com.example.zane.bakingapp.utils.Constants;
import com.example.zane.bakingapp.utils.LoadRecipes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoadRecipes.AfterTaskComplete{

    @BindView(R.id.rv_recipes)
    RecyclerView mRecyclerView;
    @BindView(R.id.recipe_overview_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.baking_backdrop)
    ImageView mBakingBackdrop;


    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);

        Picasso.with(this)
                .load(R.drawable.baking_backdrop)
                .into(mBakingBackdrop);

        mToolbar.setTitle(R.string.app_title);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        new LoadRecipes(this).execute();
    }

    @Override
    public void afterRecipesLoaded(final ArrayList<Recipe> recipeArrayList) {
        Log.i(LOG_TAG, "After Recipes Loaded Size: " + recipeArrayList.size());
        Log.i(LOG_TAG, "Ingredient Array Size: " + recipeArrayList.get(0).getIngredients().size());

        RecipeAdapter adapter = new RecipeAdapter(recipeArrayList, new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Recipe recipe = recipeArrayList.get(position);
                ArrayList<Ingredients> ingredientsArrayList = recipe.getIngredients();
                ArrayList<Steps> stepsArrayList = recipe.getSteps();

                Intent intent = new Intent(MainActivity.this, RecipeDetails.class);
                intent.putExtra(Constants.INTENT_RECIPE_OBJECT, recipe);
                intent.putExtra(Constants.INTENT_INGREDIENTS_ARRAY, ingredientsArrayList);
                intent.putExtra(Constants.INTENT_STEPS_ARRAY, stepsArrayList);
                startActivity(intent);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }
}
