package name.pilgr.android.pibalance;

import name.pilgr.android.pibalance.model.BalanceModel;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

public class PiBalanceWidgetRefresher extends BroadcastReceiver {
	Context ctx;

	@Override
	public void onReceive(Context context, Intent intent) {
		ctx = context;
		
		if (intent.getAction().equals(C.REFRESH_INTENT)) {
			refreshByNewData();
		}
		
	}
	
	private void refreshByNewData(){
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
	}
	
	private void hideTodayBox(){
		
	}
}
