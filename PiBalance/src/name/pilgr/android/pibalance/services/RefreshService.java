package name.pilgr.android.pibalance.services;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import name.pilgr.android.pibalance.R;
import name.pilgr.android.pibalance.model.BalanceModel;
import name.pilgr.android.pibalance.utils.WakefulIntentService;

public class RefreshService extends WakefulIntentService {
	private static final String TAG = RefreshService.class.getSimpleName();
	
	public RefreshService() {
		super(RefreshService.class.getSimpleName());		
	}

	@Override
	protected void doWakefulWork(Intent intent) {
		Log.d(TAG, "Really refreshing widget");
		refreshByNewData(getApplicationContext());
	}
	
	private void refreshByNewData(Context ctx){
		AppWidgetManager awm = AppWidgetManager.getInstance(ctx);
		BalanceModel bm = new BalanceModel(ctx);
		RemoteViews views = new RemoteViews(ctx.getPackageName(),
				R.layout.pibalance_appwidget);
		float bal = bm.getCurrentBalance();

		// We add '+' if balance incremented or just display the amount
		// if balance decremented
		int iToday = Math.round(bm.getTodayChange());
		String today = iToday > 0 ? "+" + iToday : "" + Math.abs(iToday);

		views.setTextViewText(R.id.balance, new Float(bal).toString());
		views.setTextViewText(R.id.today, today);
		
		//If we haven't any balance change then we hide the today text view
		if (iToday == 0){
			views.setViewVisibility(R.id.today, TextView.INVISIBLE);
		} else {
			views.setViewVisibility(R.id.today, TextView.VISIBLE);
		}		

		int awID = bm.getAppWidgetId();
		if (awID >= 0) {
			awm.updateAppWidget(awID, views);
		}
		Log.d(TAG, "Widget refreshed by service");
	}

}
