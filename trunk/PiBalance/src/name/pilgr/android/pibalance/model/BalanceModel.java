package name.pilgr.android.pibalance.model;


import java.util.Date;
import java.util.StringTokenizer;

import name.pilgr.android.pibalance.C;
import name.pilgr.android.pibalance.services.RefreshService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.gsm.SmsManager;
import android.util.Log;

public class BalanceModel {
	private static final String TAG = BalanceModel.class.getSimpleName();
	
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
	//PLMN code of mobile operator
	private final static String PR_OPERATOR_ID = "operator-id";
	//PLMN code of mobile operator
	private final static String PR_WAITING_RESPONSE = "is-request-sent";
	
	private Context context;
	private SharedPreferences s;
		
	public BalanceModel(Context ctx){
		context = ctx;
		s = context.getSharedPreferences(C.PREFS_NAME, 0);
	}
	
	public float getCurrentBalance(){
        float currBal = s.getFloat(PR_CURR_BALANCE, 0);
        return currBal;
	}
	
	public String getLastResponse(){
        String respMsg = s.getString(PR_RESPONSE, "nothing");
        return respMsg; 
	}
	
	public float getTodayChange(){		
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
		
		//If we have old data today we cann't calculate the balance change
		long todayDay = (new Date()).getTime()/(1000*60*60*24);
		if (currDay != todayDay){
			return 0;
		}
			
		return currBalance - beginBalToday;
	}
	
	private String parseMessage(String msg){
		String bal;
		StringTokenizer st = new StringTokenizer(msg, C.WASTE_SYMBOLS);
		
		while (st.hasMoreTokens()){
			bal = st.nextToken();
			bal = bal.replace(',', '.');
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
		String address="", message="";
		
		if (getOperatorId() == C.UA_LIFE_MCC_MNC){
			address = C.REQ_ADDR_UA_LIFE;
			message = C.REQ_MSG_UA_LIFE;
		}else if (getOperatorId() == C.RU_MTS_MCC_MNC){
			address = C.REQ_ADDR_RU_MTS;
			message = C.REQ_MSG_RU_MTS;
		}else if (getOperatorId() == C.RU_MEGAFON_MCC_MNC){
			address = C.REQ_ADDR_RU_MEGA;
			message = C.REQ_MSG_RU_MEGA;
		}else {
			return;
		}
		
		SmsManager smsMgr = SmsManager.getDefault();
		smsMgr.sendTextMessage(address, null, message, null, null);
		
		Editor editor = s.edit();
		editor.putBoolean(PR_WAITING_RESPONSE, true);
		editor.commit();

		
        Log.d(TAG, "Balance request sent");
	}
	
	public void storeResponse(String msgBody) {
		SharedPreferences.Editor editor = s.edit();
		
		//Parse the last balance value
		float currBalance = Float.parseFloat(parseMessage(msgBody));
		//Calculate the number of day of current time
		long currDay = (new Date()).getTime()/(1000*60*60*24);
		
		//At first, store the full response message
		editor.putString(PR_RESPONSE, msgBody);
		
		//Current to previous
		editor.putFloat(PR_PREV_BALANCE, s.getFloat(PR_CURR_BALANCE, currBalance));
		editor.putLong(PR_PREV_DAY, s.getLong(PR_CURR_DAY, 0));
				
		editor.putFloat(PR_CURR_BALANCE, currBalance);		
		editor.putLong(PR_CURR_DAY, currDay);
		
		editor.putBoolean(PR_WAITING_RESPONSE, false);
		
		editor.commit();
		
		notifyWidgets();
	}
	
	//Send broadcast to notify widgets about changes
	private void notifyWidgets(){
		Log.d(TAG, "Sending broadcast to notify widgets about changes"); 
		Intent updateIntent = new Intent(context, RefreshService.class);
         context.startService(updateIntent);
	}
	
	public void saveAppWidgetId(int id){
		SharedPreferences.Editor editor = s.edit();
		editor.putInt(PR_WIDGET_ID, id);
		editor.commit();
	}
	
	public int getAppWidgetId(){
        int awID = s.getInt(PR_WIDGET_ID, -100500);
        return awID;
	}
	
	public boolean isInit(){
		if (s.getInt(PR_WIDGET_ID, -100500) == -100500){
			return true;
		}
		return false;
	}

	public void saveOperatorId(int providerId) {
		SharedPreferences.Editor editor = s.edit();
		editor.putInt(PR_OPERATOR_ID, providerId);
		editor.commit();		
	}

	public int getOperatorId() {
        return s.getInt(PR_OPERATOR_ID, 0);
	}

	public boolean isExpectedResponse(String incNumber) {
		boolean isExpected = false;
		int opId = getOperatorId();
		if (opId == C.UA_LIFE_MCC_MNC && incNumber.equalsIgnoreCase(C.RESP_ADDR_UA_LIFE)){
			isExpected = true;
		}
		if (opId == C.RU_MTS_MCC_MNC && incNumber.equalsIgnoreCase(C.RESP_ADDR_RU_MTS)){
			isExpected = true;
		}
		if (opId == C.RU_MEGAFON_MCC_MNC && 
				incNumber.equalsIgnoreCase(C.RESP_ADDR_RU_MEGA_2)){
			isExpected = true;
		}
		
		//Do we really waiting the response? Or request was sent manually?
		isExpected = isExpected && s.getBoolean(PR_WAITING_RESPONSE, false);
			
		return isExpected;
	}
}
