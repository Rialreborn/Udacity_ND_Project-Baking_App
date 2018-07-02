package com.example.zane.bakingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zane.bakingapp.objects.Recipe;
import com.example.zane.bakingapp.utils.LoadRecipes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentRecipeList extends Fragment {

    private static final String LOG_TAG = FragmentRecipeList.class.getSimpleName();

    @BindView(R.id.rv_recipes)
    RecyclerView rvRecipes;

    private ArrayList<Recipe> mRecipeArray;
    int mPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i(LOG_TAG, "MSG! onCreateView()");

        View view = inflater.inflate(R.layout.fragment_master_list, container, false);
        ButterKnife.bind(this, view);

        mRecipeArray = MainActivity.recipeArray;
        mPosition = MainActivity.recipePosition;

        if (mRecipeArray != null && mRecipeArray.size() > 0) {
            setRvRecipeList();
        } else {
            Log.i(LOG_TAG, "MSG! Recipe Array List empty!");
        }

        updateToolbar();

        return view;
    }

    private void updateToolbar() {
        Log.i(LOG_TAG, "MSG! updateViews()");

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if (toolbar != null && !MainActivity.tabletUsed) {
            toolbar.setTitle(R.string.app_title);
        }
    }

    private void setRvRecipeList() {
        Log.i(LOG_TAG, "MSG! setRvRecipeList started.");
        Log.i(LOG_TAG, "MSG! Recipe List size: " + mRecipeArray.size());

        AdapterRecipeList adapter = new AdapterRecipeList(mRecipeArray, new AdapterRecipeList.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                FragmentRecipeDetails fragmentRecipeDetails = new FragmentRecipeDetails();
                MainActivity.recipePosition = position;

                if (MainActivity.tabletUsed) {
                    MainActivity.stepPosition = 0;
                    FragmentStepDetails fragmentStepDetails = new FragmentStepDetails();
                    getFragmentManager().beginTransaction()
                            .replace(getActivity().findViewById(R.id.fragment_main).getId(),
                                    fragmentRecipeDetails, MainActivity.FRAGMENT_RECIPE_DETAILS_ID)
                            .replace(getActivity().findViewById(R.id.fragment_recipe_step_details).getId(),
                                    fragmentStepDetails, MainActivity.FRAGMENT_STEP_DETAILS_ID)
                            .commit();
                } else {
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(getActivity().findViewById(R.id.fragment_main).getId(), fragmentRecipeDetails, MainActivity.FRAGMENT_RECIPE_DETAILS_ID)
                            .commit();
                }
            }
        });
        rvRecipes.setHasFixedSize(true);
        rvRecipes.setAdapter(adapter);
    }

    public FragmentRecipeList() {}


}
