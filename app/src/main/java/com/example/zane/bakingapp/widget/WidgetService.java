package com.example.zane.bakingapp.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by Zane on 24/06/2018.
 */

public class WidgetService extends RemoteViewsService {
    private static final String LOG_TAG = WidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i(LOG_TAG, "MSG! onGetViewFactory()");

        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

