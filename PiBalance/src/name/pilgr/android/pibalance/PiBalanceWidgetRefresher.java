package name.pilgr.android.pibalance;
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
    		String bal = bm.getCurrentBalance();
    		views.setTextViewText(R.id.balance, bal);
    		
    		int awID = bm.getAppWidgetId();
    		if (awID >= 0){
    			awm.updateAppWidget(awID, views);
    		}    		
        }
	}

}
