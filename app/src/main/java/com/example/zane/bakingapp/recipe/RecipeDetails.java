package com.example.zane.bakingapp.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.zane.bakingapp.R;
import com.example.zane.bakingapp.objects.Ingredients;
import com.example.zane.bakingapp.objects.Recipe;
import com.example.zane.bakingapp.objects.Step;
import com.example.zane.bakingapp.utils.Constants;
import com.example.zane.bakingapp.utils.CustomiseWindow;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zane on 04/06/2018.
 */

public class RecipeDetails extends AppCompatActivity {

    private static final String LOG_TAG = RecipeDetails.class.getSimpleName();

    @BindView(R.id.recipe_description_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_details_ingredients)
    TextView mTvIngredients;
    @BindView(R.id.rv_steps)
    RecyclerView mRvSteps;

    Recipe mRecipe;
    ArrayList<Ingredients> mIngredientsList;
    ArrayList<Step> mStepsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recipe_overview);

        ButterKnife.bind(RecipeDetails.this);

        mStepsList = getIntent().getParcelableArrayListExtra(Constants.INTENT_STEPS_ARRAY);
        mIngredientsList = getIntent().getParcelableArrayListExtra(Constants.INTENT_INGREDIENTS_ARRAY);
        mRecipe = getIntent().getParcelableExtra(Constants.INTENT_RECIPE_OBJECT);
        if (mRecipe == null) {
            finish();
            Toast.makeText(this, R.string.detail_intent_error, Toast.LENGTH_SHORT).show();
        }

        CustomiseWindow.customWindow(this);
        setViews();
        setToolbar();
    }

    private void setViews() {
        Log.i(LOG_TAG, "Recipe Name: " + mRecipe.getName());
        Log.i(LOG_TAG, "Ingredient ArraySize: " + mIngredientsList.size());
        mTvIngredients.setText(createIngredientList());

        StepsAdapter stepsAdapter = new StepsAdapter(mStepsList, this, new StepsAdapter.OnStepClickListener() {
            @Override
            public void onStepClicked(int position) {


                Intent intent = new Intent(RecipeDetails.this, StepDetails.class);
                intent.putExtra(Constants.INTENT_STEPS_ARRAY, mStepsList);
                intent.putExtra(Constants.INTENT_STEP_POSITION, position);
                startActivity(intent);
            }
        });
        mRvSteps.setHasFixedSize(true);
        mRvSteps.setAdapter(stepsAdapter);
    }

    private String createIngredientList(){
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
            if (measure.contains("CUP")) {
                measure += "S";
            }
            builder.append("\u2022 " + quantity + " " + measure + " " + singleIngredient + "\n");
        }

        return builder.toString();
    }

    private void setToolbar() {
        mToolbar.setTitle(mRecipe.getName());
        String servings = getString(R.string.servings) + " " + mRecipe.getServings();
        mToolbar.setSubtitle(servings);
        mToolbar.setSubtitleTextColor(getColor(R.color.colorAccentLight));
        mToolbar.setBackgroundColor(getColor(R.color.colorPrimary));
        mToolbar.setTitleTextColor(getColor(R.color.white));
    }

}
