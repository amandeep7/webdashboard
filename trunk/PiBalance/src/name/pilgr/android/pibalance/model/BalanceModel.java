package name.pilgr.android.pibalance.model;


import java.util.Date;
import java.util.StringTokenizer;

import name.pilgr.android.pibalance.C;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.SmsManager;
import android.widget.Toast;

public class BalanceModel {
	
	private final static String PR_RESPONSE = "response";
	private final static String PR_WIDGET_ID = "widget-id";
	//The last balance from current response
	private final static String PR_CURR_BALANCE = "curr-balance";
	//The data of current response
	private final static String PR_CURR_DAY = "curr-day";
	//The last balance from previous response
	private final static String PR_PREV_BALANCE = "prev-balance";
	//The data of previous response
	private final static String PR_PREV_DAY = "prev-day";
	//Beginning balance for today
	private final static String PR_BEGINNING_BALANCE_TODAY = "beginning-balance-today";
	
	public Context context;
		
	public BalanceModel(Context ctx){
		context = ctx;
	}
	
	public float getCurrentBalance(){
		SharedPreferences settings = context.getSharedPreferences(C.PREFS_NAME, 0);
        float currBal = settings.getFloat(PR_CURR_BALANCE, 0);
        return currBal;
	}
	
	public String getLastResponse(){
		SharedPreferences settings = context.getSharedPreferences(C.PREFS_NAME, 0);
        String respMsg = settings.getString(PR_RESPONSE, "nothing");
        return respMsg; 
	}
	
	public float getTodayChange(){		
		SharedPreferences s = context.getSharedPreferences(C.PREFS_NAME, 0);
                
		long prevDay = s.getLong(PR_PREV_DAY, 0);
		long currDay = s.getLong(PR_CURR_DAY, 0);
		float prevBalance = s.getFloat(PR_PREV_BALANCE, 0);
		float currBalance = s.getFloat(PR_CURR_BALANCE, 0);
		float beginBalToday = s.getFloat(PR_BEGINNING_BALANCE_TODAY, 0);
				 
		//If data changed we must calculate change from the previous balance 
		if (currDay != prevDay){
			beginBalToday = prevBalance;
			Editor editor = s.edit();
			editor.putFloat(PR_BEGINNING_BALANCE_TODAY, beginBalToday);
			editor.commit();
		}		
		return currBalance - beginBalToday;
	}
	
	private String parseMessageLifeUA(String msg){
		String bal;
		StringTokenizer st = new StringTokenizer(msg, C.WASTE_SYMBOLS);
		
		while (st.hasMoreTokens()){
			bal = st.nextToken();
			if (isAmount(bal)){
				return bal;
			}			
		}
		return C.PARSE_ERROR;
		
	}
	
	private boolean isAmount(String str){
		try{
			Double.parseDouble(str);
			return true;
		} catch(Exception e){
			return false;						
		}
	}
	
	public void sendSMSRequest(){
		
		SmsManager smsMgr = SmsManager.getDefault(); 
        smsMgr.sendTextMessage(C.REQUEST_ADDRESS, null, C.REQUEST_MESSAGE, null, null);
        Toast.makeText(context, "Balance request sent", Toast.LENGTH_LONG).show();
	}
	
	public void storeResponse(String msgBody) {
		SharedPreferences settings = context.getSharedPreferences(C.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		//Parse the last balance value
		float currBalance = Float.parseFloat(parseMessageLifeUA(msgBody));
		//Calculate the number of day of current time
		long currDay = (new Date()).getTime()/(1000*60*60*24);
		
		//At first, store the full response message
		editor.putString(PR_RESPONSE, msgBody);
		
		//Current to previous
		editor.putFloat(PR_PREV_BALANCE, settings.getFloat(PR_CURR_BALANCE, currBalance));
		editor.putLong(PR_PREV_DAY, settings.getLong(PR_CURR_DAY, 0));
				
		editor.putFloat(PR_CURR_BALANCE, currBalance);		
		editor.putLong(PR_CURR_DAY, currDay);
		
		editor.commit();
		
		notifyWidgets();
	}
	
	//Send broadcast to notify widgets about changes
	private void notifyWidgets(){
		Intent i = new Intent();
		i.setAction(C.REFRESH_INTENT);
		context.sendBroadcast(i);
	}
	
	public void saveAppWidgetId(int id){
		SharedPreferences settings = context.getSharedPreferences(C.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(PR_WIDGET_ID, id);
		editor.commit();
	}
	
	public int getAppWidgetId(){
		SharedPreferences settings = context.getSharedPreferences(C.PREFS_NAME, 0);
        int awID = settings.getInt(PR_WIDGET_ID, -1);
        return awID;
	}
	
	public void dbgSetBeginingBalance(float bal){
		SharedPreferences settings = context.getSharedPreferences(C.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(PR_BEGINNING_BALANCE_TODAY, bal);
		editor.commit();
	}
	
	public boolean isInit(){
		SharedPreferences s = context.getSharedPreferences(C.PREFS_NAME, 0);
		if (s.getInt(PR_WIDGET_ID, -100500) == -100500){
			return true;
		}
		return false;
	}
}
