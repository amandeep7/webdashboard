package name.pilgr.android.pibalance;


import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class BalanceModel {
	public static final String PREFS_NAME = "currentData";
	public Context context;
	public final static String PARSE_ERROR = "ERR";
	
	public BalanceModel(Context ctx){
		context = ctx;
	}
	
	public BalanceModel(Activity act){
		context = act;
	}
	
	public String getCurrentBalance(){
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String respMsg = settings.getString("lastResponse", "nothing");
        return parseMessageLifeUA(respMsg);
	}
	
	private String parseMessageLifeUA(String msg){
		String bal;
		StringTokenizer st = new StringTokenizer(msg, " UAH");
		
		while (st.hasMoreTokens()){
			bal = st.nextToken();
			if (isAmount(bal)){
				return bal;
			}			
		}
		return PARSE_ERROR;
		
	}
	
//	private String parseMessage(String msg){
//		String bal = "";
//		String symbol;
//		
//		for (int i=0; i<msg.length(); i++){
//			symbol = msg.substring(i, i+1);
//			if (isPartOfAmount(symbol)){
//				bal += symbol;
//			}
//		}
//		
//		if (bal.length() > 0){
//			return bal;
//		} else {
//			return BALANCE_ERROR;
//		}
//		
//	}
	
	private boolean isAmount(String str){
		try{
			Double.parseDouble(str);
			return true;
		} catch(Exception e){
			return false;						
		}
	}

}
