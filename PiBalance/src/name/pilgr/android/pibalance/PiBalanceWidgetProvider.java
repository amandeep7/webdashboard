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
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;

public class PiBalanceWidgetProvider extends AppWidgetProvider {
	private static final String TAG = PiBalanceWidgetProvider.class.getSimpleName();

	private BalanceModel bm;

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length ;
		init(context);
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}
	
	public void onEnabled(Context context){
		init(context);
		saveOperatorId(context);
		//Controller.getInstance().setMidnightRefresh(context);//To clear the today box at midnight
	}
	
	public void onDisabled(Context context){
		//TODO: Disable any alarm and receiver
	}

	public void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {
		if (bm.isInit()){
			bm.sendSMSRequest(true);
		}
		bm.saveAppWidgetId(appWidgetId);
		saveOperatorId(context);
		//Controller.getInstance().setMidnightRefresh(context);
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
	
	private void saveOperatorId(Context ctx){
	   TelephonyManager tm = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
	   int providerId = Integer.parseInt(tm.getNetworkOperator());
	   bm.saveOperatorId(providerId);
	}
}
