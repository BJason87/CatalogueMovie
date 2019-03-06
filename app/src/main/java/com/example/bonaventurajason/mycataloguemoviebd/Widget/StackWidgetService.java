package com.example.bonaventurajason.mycataloguemoviebd.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class StackWidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}