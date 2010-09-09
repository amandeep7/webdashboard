package name.pilgr.android.pibalance;


import java.util.StringTokenizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;

public class BalanceModel {
	
	private final static String PR_RESPONSE = "response";
	private final static String PR_WIDGET_ID = "widget-id";
	
	public Context context;
		
	public BalanceModel(Context ctx){
		context = ctx;
	}
	
	public String getCurrentBalance(){
		SharedPreferences settings = context.getSharedPreferences(C.PREFS_NAME, 0);
        String respMsg = settings.getString(PR_RESPONSE, "nothing");
        return parseMessageLifeUA(respMsg);
	}
	
	public String getLastResponse(){
		SharedPreferences settings = context.getSharedPreferences(C.PREFS_NAME, 0);
        String respMsg = settings.getString(PR_RESPONSE, "nothing");
        return respMsg; 
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
	}
	
	public void storeResponse(String msgBody) {
		SharedPreferences settings = context.getSharedPreferences(C.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(PR_RESPONSE, msgBody);
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
}
