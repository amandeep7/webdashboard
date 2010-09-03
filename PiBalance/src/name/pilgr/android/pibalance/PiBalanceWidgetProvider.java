package name.pilgr.android.pibalance;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class PiBalanceWidgetProvider extends AppWidgetProvider {

	public static final String PREFS_NAME = "currentData";
	private BalanceModel balModel;

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
		
		RemoteViews views = new RemoteViews(context.getPackageName(),  
                R.layout.pibalance_appwidget);
		String bal = balModel.getCurrentBalance();
		views.setTextViewText(R.id.balance, bal);
		appWidgetManager.updateAppWidget(appWidgetId, views);

	}
	
	private void init(Context context){
		balModel = new BalanceModel(context);
	}
}
