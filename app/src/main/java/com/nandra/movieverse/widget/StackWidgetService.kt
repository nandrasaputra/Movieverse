package com.nandra.movieverse.widget

import android.content.Intent
import android.widget.RemoteViewsService

class StackWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackWidgetRemoteViewFactory(this.applicationContext)
}