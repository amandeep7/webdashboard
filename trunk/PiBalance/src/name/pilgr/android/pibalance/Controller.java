package name.pilgr.android.pibalance;

import java.util.Calendar;
import java.util.Date;

import name.pilgr.android.pibalance.services.RefreshService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

//Singleton for control some aspect of application
public class Controller {
	private static Controller _instance = null;
	
	private Controller(){}
	
	private static final String TAG = Controller.class.getSimpleName();
	
	public synchronized static Controller getInstance() {
		if (_instance == null)
        _instance = new Controller();
    return _instance;
	}
	
	public void setMidnightRefresh(Context context){
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
	}

}
