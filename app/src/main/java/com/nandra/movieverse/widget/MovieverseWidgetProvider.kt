package com.nandra.movieverse.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.nandra.movieverse.R

class MovieverseWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == TOAST_ACTION) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
            } else if (intent.action == REFRESH_ITEM) {
                val mgr = AppWidgetManager.getInstance(context)
                val cn = ComponentName(context, MovieverseWidgetProvider::class.java)
                mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widget_stack_view)
            }
        }
    }

    companion object {
        const val EXTRA_ITEM = "com.nandra.movieverse.EXTRA_ITEM"
        const val REFRESH_ITEM = "com.nandra.movieverse.REFRESH"
        private const val TOAST_ACTION = "com.nandra.movieverse.TOAST_ACTION"

        internal fun refreshAppWidget(context: Context) {
            val intent = Intent(REFRESH_ITEM)
            intent.component = ComponentName(context, MovieverseWidgetProvider::class.java)
            context.sendBroadcast(intent)
        }

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            // Construct the RemoteViews object
            val remoteView = RemoteViews(context.packageName, R.layout.movieverse_widget)
            remoteView.setRemoteAdapter(R.id.widget_stack_view, intent)
            remoteView.setEmptyView(R.id.widget_stack_view, R.id.widget_empty_view)

            val toastIntent = Intent(context, MovieverseWidgetProvider::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setPendingIntentTemplate(R.id.widget_stack_view, toastPendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteView)
        }
    }
}