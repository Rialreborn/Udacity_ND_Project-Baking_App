package com.example.zane.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.zane.bakingapp.MainActivity;
import com.example.zane.bakingapp.R;
import com.example.zane.bakingapp.objects.Ingredients;

import java.util.ArrayList;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = WidgetRemoteViewsFactory.class.getSimpleName();

    Context mContext;
    ArrayList<Ingredients> mIngredientsList;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        Log.i(LOG_TAG, "MSG! WidgetRemoteViewsFactory()");
        this.mContext = context;

    }


    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "MSG! onCreate()");
    }

    @Override
    public void onDataSetChanged() {
        Log.i(LOG_TAG, "MSG! onDataSetChanged()");
        this.mIngredientsList = MainActivity.ingredientArray;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredientsList == null) {
            Log.i(LOG_TAG, "MSG! IngredientsList == null");
            return 0;
        } else {
            Log.i(LOG_TAG, "MSG! IngredientsList size: " + mIngredientsList.size());
            return mIngredientsList.size();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.i(LOG_TAG, "MSG! getViewAt()");
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        StringBuilder builder = new StringBuilder();
        Ingredients ingredients = mIngredientsList.get(position);
        int quantity = ingredients.getQuantity();
        String measure = ingredients.getMeasure();
        String singleIngredient = ingredients.getIngredient();
        builder.append("\u2022 " + quantity + " " + measure + " " + singleIngredient + "\n");

        rv.setTextViewText(R.id.widget_list_item_tv, builder.toString());


        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
