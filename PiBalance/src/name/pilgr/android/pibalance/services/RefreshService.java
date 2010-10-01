package name.pilgr.android.pibalance.services;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import name.pilgr.android.pibalance.C;
import name.pilgr.android.pibalance.Controller;
import name.pilgr.android.pibalance.R;
import name.pilgr.android.pibalance.model.BalanceModel;
import name.pilgr.android.pibalance.utils.WakefulIntentService;

public class RefreshService extends WakefulIntentService {
	private static final String TAG = RefreshService.class.getSimpleName();
	private BalanceModel bm;
	
	public RefreshService() {
		super(RefreshService.class.getSimpleName());		
	}

	@Override
	protected void doWakefulWork(Intent intent) {
		Log.d(TAG, "Really refreshing widget");
		bm = new BalanceModel(getApplicationContext());
		refreshByNewData(getApplicationContext());
	}
	
	private void refreshByNewData(Context ctx){
		AppWidgetManager awm = AppWidgetManager.getInstance(ctx);		
		RemoteViews views = getViewForCurrentProvider(ctx);
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
			Controller.getInstance().setMidnightRefresh(ctx);
		}
		Log.d(TAG, "Widget refreshed by service");
	}

	private RemoteViews getViewForCurrentProvider(Context ctx) {
		int layout_id = 0;
		
        int providerId = bm.getOperatorId();

		switch (providerId) {
		//Life:) UA
		case C.UA_LIFE_MCC_MNC:
			layout_id = R.layout.w_life;
			break;
		//MTS RU
		case C.RU_MTS_MCC_MNC:
			layout_id = R.layout.w_mts;
			break;
		//Megafon RU
		case C.RU_MEGAFON_MCC_MNC:
			layout_id = R.layout.w_megafon;
			break;
		}
		
		//If mobile operator doesn't supported now
		if (layout_id == 0){
			return new RemoteViews(ctx.getPackageName(),R.layout.w_blank);
		}
		return new RemoteViews(ctx.getPackageName(),layout_id);
	}

}
