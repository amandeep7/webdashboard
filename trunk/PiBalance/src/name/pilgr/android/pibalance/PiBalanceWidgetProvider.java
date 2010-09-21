package name.pilgr.android.pibalance;

import java.util.Date;


import name.pilgr.android.pibalance.model.BalanceModel;
import name.pilgr.android.pibalance.services.RefreshService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

public class PiBalanceWidgetProvider extends AppWidgetProvider {
	private static final String TAG = PiBalanceWidgetProvider.class.getSimpleName();

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
	
	public void onEnabled(Context context){
		setMidnightAlarm(context);//For clearing the today box at midnight
	}
	
	public void onDisabled(Context context){
		//TODO: Disable any alarm and receiver
	}

	public void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {
		if (bm.isInit()){
			bm.sendSMSRequest();
		}
		bm.saveAppWidgetId(appWidgetId);
		setMidnightAlarm(context);
		refreshWidgets(context);		
	}
	
	private void init(Context context){
		bm = new BalanceModel(context);
	}
	
	//Start service to refresh widget
	private void refreshWidgets(Context context){
		 Intent updateIntent = new Intent(context, RefreshService.class);
         context.startService(updateIntent);
	}
	
	public void setMidnightAlarm(Context context){
		AlarmManager mgr =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, RefreshService.class);
		PendingIntent pi=PendingIntent.getService(context, 0, i, 0);
		long wakeTime = new Date().getTime();
		wakeTime =  wakeTime / (1000*60*60); // H
		wakeTime = wakeTime / 24; //Day
		wakeTime = (wakeTime + 1) * 24*60*60*1000 + 1000; //It is the 00:00:01 of the next day
		mgr.cancel(pi);
		mgr.setRepeating( 
                AlarmManager.RTC_WAKEUP, 
                wakeTime, 
                1000*60*60*24, //Repeating every day
                pi);
		Log.d(TAG,"Registered midnight alarm");
	}
}
