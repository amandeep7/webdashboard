package name.pilgr.android.pibalance;

import name.pilgr.android.pibalance.model.BalanceModel;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class PiBalanceWidgetRefresher extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(C.REFRESH_INTENT)) {
			AppWidgetManager awm = AppWidgetManager.getInstance(context);
			BalanceModel bm = new BalanceModel(context);
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.pibalance_appwidget);
			float bal = bm.getCurrentBalance();

			// We add '+' if balance incremented or just display the amount
			// if balance decremented
			int iToday = Math.round(bm.getTodayChange());
			String today = iToday > 0 ? "+" + iToday : "" + Math.abs(iToday);

			views.setTextViewText(R.id.balance, new Float(bal).toString());
			views.setTextViewText(R.id.today, today);

			int awID = bm.getAppWidgetId();
			if (awID >= 0) {
				awm.updateAppWidget(awID, views);
			}
		}
	}

}
