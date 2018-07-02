package com.example.zane.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.zane.bakingapp.MainActivity;
import com.example.zane.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {
    private static final String LOG_TAG = WidgetProvider.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "MSG! onReceive()");
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, WidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(
                    appWidgetManager.getAppWidgetIds(componentName), R.id.widget_listview);
        }

        super.onReceive(context, intent);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        Log.i(LOG_TAG, "MSG! updateWidget()");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_collection);

        Intent serviceIntent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_listview, serviceIntent);

        Intent launchIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingintent = PendingIntent.getActivity(context, 0, launchIntent, 0);
        views.setOnClickPendingIntent(R.id.widget, pendingintent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(LOG_TAG, "MSG! onUpdate()");

        for (int appWidgetId : appWidgetIds){
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

