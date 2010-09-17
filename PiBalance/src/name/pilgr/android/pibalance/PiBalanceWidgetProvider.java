package name.pilgr.android.pibalance;

import name.pilgr.android.pibalance.model.BalanceModel;
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
		if (bm.isInit()){
			bm.sendSMSRequest();
		}
		bm.saveAppWidgetId(appWidgetId);
		notifyWidgets(context);
	}
	
	private void init(Context context){
		bm = new BalanceModel(context);
	}
	
	//Send broadcast to notify widgets about changes
	private void notifyWidgets(Context context){
		Intent i = new Intent();
		i.setAction(C.REFRESH_INTENT);
		context.sendBroadcast(i);
	}
	
}
