package com.example.zane.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zane.bakingapp.objects.Recipe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Zane on 03/06/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder>{

    private static final String LOG_TAG = RecipeAdapter.class.getSimpleName();
    private ArrayList<Recipe> mRecipeList;
    private OnItemClickListener mOnItemClickListener;

    public RecipeAdapter(ArrayList<Recipe> recipeArrayList, OnItemClickListener onItemClickListener){
        this.mRecipeList = recipeArrayList;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_rv_item, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    /**
     * Recipe Holder
     */
    class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mRecipeTitle;
        TextView mServings;
        ImageView mBackground;
        Context mContext;

        public RecipeHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mRecipeTitle = itemView.findViewById(R.id.tvRecipeCardTitle);
            mBackground = itemView.findViewById(R.id.recipe_image);
            mServings = itemView.findViewById(R.id.tvRecipeCardServings);
            itemView.setOnClickListener(this);
        }

        void bind(int position){
            String name = mRecipeList.get(position).getName();
            mRecipeTitle.setText(name);
            String servings = mContext.getString(R.string.servings) + " " + mRecipeList.get(position).getServings();
            mServings.setText(servings);

            Uri uri = mRecipeList.get(position).getImageUri();;

            int placeholderImg;
            name = name.toLowerCase();
            if (name.contains("cake")){
                placeholderImg = R.drawable.cake;
            } else if (name.contains("pie")) {
                placeholderImg = R.drawable.pie;
            } else {
                placeholderImg = R.drawable.cupcake;
            }
            if (uri.toString().equals("")) {
                Log.i(LOG_TAG, "Background Image uri is empty or null for: " + mRecipeList.get(position).getName());
                Picasso.with(mContext)
                        .load(placeholderImg)
                        .placeholder(placeholderImg)
                        .into(mBackground);
            } else {
                Log.i(LOG_TAG, "Background Image uri: " + uri.toString());
                Picasso.with(mContext)
                        .load(uri)
                        .placeholder(placeholderImg)
                        .error(placeholderImg)
                        .into(mBackground);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnItemClickListener.onItemClick(position);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
