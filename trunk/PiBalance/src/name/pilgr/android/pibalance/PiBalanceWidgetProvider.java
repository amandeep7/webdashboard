package name.pilgr.android.pibalance;

import java.util.Date;

import name.pilgr.android.pibalance.model.BalanceModel;
import android.app.AlarmManager;
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
		
		AlarmManager mgr =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent i=new Intent(context, PiBalanceWidgetRefresher.class);
		PendingIntent pi=PendingIntent.getBroadcast(context, 0, i, 0);
		long wakeTime = new Date().getTime();
		Log.d("DEBUG", new Date(wakeTime).toString());
		wakeTime =  wakeTime / (1000*60*60); // H
		wakeTime = wakeTime / 24; //Day
		wakeTime = (wakeTime + 1) * 24*60*60*1000 + 1000*60; //It is the 00:01:00 of the next day
		Log.d("DEBUG", new Date(wakeTime).toString());
		mgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, wakeTime, pi);
	}
	
	//Send broadcast to notify widgets about changes
	private void notifyWidgets(Context context){
		Intent i = new Intent();
		i.setAction(C.REFRESH_INTENT);
		context.sendBroadcast(i);
	}
	
}
