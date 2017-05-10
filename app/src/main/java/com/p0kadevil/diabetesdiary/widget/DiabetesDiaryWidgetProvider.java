package com.p0kadevil.diabetesdiary.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import com.p0kadevil.diabetesdiary.R;
import com.p0kadevil.diabetesdiary.db.DiabetesDbContract;


public class DiabetesDiaryWidgetProvider extends AppWidgetProvider {

    private static final String PROVIDER_NAME = "com.p0kadevil.diabetesdiary.DiabetesDiaryContentProvider.entries";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/entries");

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);

        if(cursor == null ||
                cursor.getCount() == 0)
        {
            remoteViews.setTextViewText(R.id.tv_avg, String.valueOf(0));
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

            return;
        }

        float sum = 0;
        int count = cursor.getCount();

        while(cursor.moveToNext())
        {
            sum += cursor.getFloat(cursor.getColumnIndex(DiabetesDbContract.DiaryEntry.COLUMN_NAME_BLOOD));
        }

        float avg = sum / count;

        remoteViews.setTextViewText(R.id.tv_avg, String.valueOf(avg));
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds)
        {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}
