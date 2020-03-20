package com.oikm.a100.bakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.oikm.a100.bakingapp.Model.Bakery;
import com.oikm.a100.bakingapp.Model.Ingredients;
import com.oikm.a100.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecipesWidget extends AppWidgetProvider {
    private static String text;
    static List<Ingredients> ingredients = new ArrayList<>();

  private static RemoteViews remoteViews(Context context, AppWidgetManager appWidgetManager,int widgetID){

   RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_baking);

    views.setTextViewText(R.id.tv_recipe_title_widget, text);

    Intent intent = new Intent(context, RecipesService.class);
    views.setRemoteAdapter(R.id.lv_ingredients_widget, intent);

    appWidgetManager.updateAppWidget(widgetID, views);
    return views;
}

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.hasExtra("recipe")){
          Bakery bakery = (Bakery) intent.getParcelableExtra("bakery");
          text = bakery.getName();
          ingredients = bakery.getIngredients();
      }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), RecipesWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_ingredients_widget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            remoteViews(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }
}
