package com.example.zane.bakingapp;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zane.bakingapp.objects.Ingredients;
import com.example.zane.bakingapp.objects.Recipe;
import com.example.zane.bakingapp.utils.LoadRecipes;
import com.example.zane.bakingapp.utils.SimpleIdlingResource;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoadRecipes.AfterTaskComplete {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String INSTANT_STATE_RECIPE_ARRAY = "recipe_array";
    private final String INSTANT_STATE_STEP_POSITION = "step_position";
    private final String INSTANT_STATE_RECIPE_POSITION = "recipe_position";
    public static final String FRAGMENT_LIST_ID = "fragment_list";
    public static final String FRAGMENT_RECIPE_DETAILS_ID = "fragment_details";
    public static final String FRAGMENT_STEP_DETAILS_ID = "fragment_steps";

    @BindView(R.id.fragment_main)
    FrameLayout fragmentMain;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_error_message)
    TextView tvErrorMessage;

    @Nullable
    private SimpleIdlingResource mIdlingResource;
    public static boolean tabletUsed;
    public static boolean screenLand;
    public static ArrayList<Recipe> recipeArray;
    public static ArrayList<Ingredients> ingredientArray;
    public static int recipePosition;
    public static int stepPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "MSG! onCreate started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);

        if (networkConnection()) {
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                screenLand = true;
            } else {
                screenLand = false;
            }

            if (savedInstanceState == null) {
                recipePosition = 0;
                stepPosition = 0;

                fragmentMain.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                new LoadRecipes(this).execute();
            } else {
                recipeArray = savedInstanceState.getParcelableArrayList(INSTANT_STATE_RECIPE_ARRAY);
                recipePosition = savedInstanceState.getInt(INSTANT_STATE_RECIPE_POSITION);
                stepPosition = savedInstanceState.getInt(INSTANT_STATE_STEP_POSITION);

                startFragmentization(savedInstanceState);
                if (findViewById(R.id.fragment_recipe_step_details) != null) {
                    tabletUsed = true;
                } else {
                    tabletUsed = false;
                }
            }
        } else {
            noNetworkFound();
        }
        setupViews();
    }

    private void setupViews() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_title));
        }
    }

    @Override
    public void afterRecipesLoaded(final ArrayList<Recipe> recipeArrayList) {
        Log.i(LOG_TAG, "MSG! After Recipes Loaded Size: " + recipeArrayList.size());

        recipeArray = recipeArrayList;

        progressBar.setVisibility(View.GONE);
        fragmentMain.setVisibility(View.VISIBLE);

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }

        startFragmentization(null);
    }



    private void startFragmentization(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "MSG! startFragmentization!");
        FragmentManager fm = getFragmentManager();

        FragmentRecipeList fragmentRecipeList = new FragmentRecipeList();
        FragmentStepDetails fragmentStepDetails = (FragmentStepDetails) getFragmentManager().findFragmentByTag(FRAGMENT_STEP_DETAILS_ID);
        FragmentRecipeDetails fragmentRecipeDetails = (FragmentRecipeDetails) getFragmentManager().findFragmentByTag(FRAGMENT_RECIPE_DETAILS_ID);
        if (savedInstanceState != null) {
            if (!tabletUsed) {
                if (screenLand) fm.beginTransaction()
                        .replace(R.id.fragment_recipe_list, fragmentRecipeList, FRAGMENT_LIST_ID)
                        .commit();

                if (fragmentStepDetails != null) return;

                if (fragmentRecipeDetails == null) {
                    fragmentRecipeDetails = new FragmentRecipeDetails();
                    fm.beginTransaction()
                            .replace(R.id.fragment_main, fragmentRecipeDetails, FRAGMENT_RECIPE_DETAILS_ID)
                            .commit();
                }
            }
        } else {
            fragmentStepDetails = new FragmentStepDetails();
            fragmentRecipeDetails = new FragmentRecipeDetails();
            if (tabletUsed) {
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_recipe_list, fragmentRecipeList, FRAGMENT_LIST_ID)
                        .add(R.id.fragment_main, fragmentRecipeDetails, FRAGMENT_RECIPE_DETAILS_ID)
                        .add(R.id.fragment_recipe_step_details, fragmentStepDetails, FRAGMENT_STEP_DETAILS_ID)
                        .commit();
            } else if (screenLand) {
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_main, fragmentRecipeDetails, FRAGMENT_RECIPE_DETAILS_ID)
                        .add(R.id.fragment_recipe_list, fragmentRecipeList, FRAGMENT_LIST_ID)
                        .commit();
            } else {
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_main, fragmentRecipeList, FRAGMENT_LIST_ID)
                        .commit();
            }
        }


    }

    private boolean networkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo info = null;
        if (connectivityManager != null) {
            info = connectivityManager.getActiveNetworkInfo();
        }

        return info != null && info.isConnectedOrConnecting();
    }

    private void noNetworkFound() {
        fragmentMain.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(INSTANT_STATE_RECIPE_ARRAY, recipeArray);
        outState.putInt(INSTANT_STATE_RECIPE_POSITION, recipePosition);
        outState.putInt(INSTANT_STATE_STEP_POSITION, stepPosition);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
