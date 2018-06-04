package com.example.zane.bakingapp.recipe;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.zane.bakingapp.R;
import com.example.zane.bakingapp.objects.Ingredients;
import com.example.zane.bakingapp.objects.Recipe;
import com.example.zane.bakingapp.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zane on 04/06/2018.
 */

public class RecipeDetails extends AppCompatActivity {

    @BindView(R.id.recipe_description_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_ingredients)
    TextView mIngredients;

    Recipe mRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recipe_overview);

        ButterKnife.bind(RecipeDetails.this);

        mRecipe = getIntent().getParcelableExtra(Constants.INTENT_RECIPE_OBJECT);
        if (mRecipe == null) {
            finish();
            Toast.makeText(this, R.string.detail_intent_error, Toast.LENGTH_SHORT).show();
        }

        mToolbar.setTitle(mRecipe.getName());

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

    }

//    private String createIngredientList(){
//        StringBuilder builder;
//        ArrayList<Ingredients> ingredientsArrayList = mRecipe.getIngredients();
//        Recipe recipe;
//        int quantity;
//        String measure;
//        String ingredient;
//
//
//        for (int i = 0; i < ingredientsArrayList.size(); i++) {
//            quantity =
//        }
//
//
//
//
//        return builder.toString();
//    }
}
