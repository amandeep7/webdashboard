package name.pilgr.android.pibalance;

import java.util.Calendar;
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
		Controller.getInstance().setMidnightRefresh(context);//To clear the today box at midnight
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
		Controller.getInstance().setMidnightRefresh(context);
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
	
	/*public void setMidnightRefresh(Context context){
		AlarmManager mgr =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, RefreshService.class);
		PendingIntent pi=PendingIntent.getService(context, 0, i, 0);
		long wakeTime = new Date().getTime();

		Log.d(TAG, "Now:" + new Date(wakeTime).toString());
		wakeTime =  wakeTime / (1000*60*60); // H
		wakeTime = wakeTime / 24; //Day
		wakeTime = (wakeTime + 1) * 24*60*60*1000 + 1000; //It is the 00:00:01 of the next day UTC

		//We need to correct UTC time to local time.
		//Because we want to set alarm at 00:00:01 LOCAL time
		Calendar cal = Calendar.getInstance();
		wakeTime = wakeTime - (cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET)) ;

		Log.d(TAG, "Set alarm at:" + new Date(wakeTime).toString());
		
		mgr.cancel(pi);
		mgr.setRepeating( 
                AlarmManager.RTC_WAKEUP, 
                wakeTime, 
                1000*60*60*24, //Repeating every day
                pi);
		Log.d(TAG,"Registered midnight alarm");
	}*/
}
