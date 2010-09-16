package name.pilgr.android.pibalance;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class PiBalanceWidgetProvider extends AppWidgetProvider {

	private BalanceModel bm;

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		init(context);
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}

	public void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {
		
		RemoteViews views = new RemoteViews(context.getPackageName(),  
                R.layout.pibalance_appwidget);
		String bal = bm.getCurrentBalance();
		views.setTextViewText(R.id.balance, bal);
		
		Intent intent = new Intent(context, ClickWidgetActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		Log.d("DEBUG", "After create intent");
		views.setOnClickPendingIntent(R.id.widget, pendingIntent);
		
		appWidgetManager.updateAppWidget(appWidgetId, views);
		
		bm.saveAppWidgetId(appWidgetId);

	}
	
	private void init(Context context){
		bm = new BalanceModel(context);
	}
	
}
