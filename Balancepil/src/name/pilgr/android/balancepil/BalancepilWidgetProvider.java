package name.pilgr.android.balancepil;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

public class BalancepilWidgetProvider extends AppWidgetProvider {
	 public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
	        final int N = appWidgetIds.length;

	        // Perform this loop procedure for each App Widget that belongs to this provider
	        for (int i=0; i<N; i++) {
	            int appWidgetId = appWidgetIds[i];

//	            // Create an Intent to launch ExampleActivity
//	            Intent intent = new Intent(context, ExampleActivity.class);
//	            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//	            // Get the layout for the App Widget and attach an on-click listener to the button
//	            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider_layout);
//	            views.setOnClickPendingIntent(R.id.button, pendingIntent);
//
//	            // Tell the AppWidgetManager to perform an update on the current App Widget
//	            appWidgetManager.updateAppWidget(appWidgetId, views);
	        }
	    }
}