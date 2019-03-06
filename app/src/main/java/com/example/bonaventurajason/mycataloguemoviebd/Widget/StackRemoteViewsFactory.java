package com.example.bonaventurajason.mycataloguemoviebd.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.bonaventurajason.mycataloguemoviebd.Movie;
import com.example.bonaventurajason.mycataloguemoviebd.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<Movie> widgetItems = new ArrayList<>();
    private Context context;
    private int appWidgetId;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    public void getFavoriteMovie() {
        widgetItems.clear();
        Uri uri = CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                do {
                    widgetItems.add(new Movie(cursor));
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }
            cursor.close();
        }
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        getFavoriteMovie();
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }
    @Override
    public int getCount() {
        return widgetItems.size();
    }
    @Override
    public int getViewTypeCount() {
        return 1;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .load(widgetItems.get(position).getUrl())
                    .asBitmap()
                    .error(new ColorDrawable(context.getResources().getColor(R.color.colorPrimaryDark)))
                    .into(SIZE_ORIGINAL, SIZE_ORIGINAL).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        remoteViews.setImageViewBitmap(R.id.widget_image, bitmap);
        remoteViews.setTextViewText(R.id.widget_title, widgetItems.get(position).getTitle());
        Bundle extras = new Bundle();
        extras.putInt(MovieWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.widget_image, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }
}
