package name.pilgr.android.pibalance;

import name.pilgr.android.pibalance.model.BalanceModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PiPhoneStateListener extends PhoneStateListener {
   
	private static int prevState = -1;
	Context ctx;
	BalanceModel bm;
	
	public PiPhoneStateListener(Context context){
		ctx = context;
		bm = new BalanceModel(context);
	}
	
	  public void onCallStateChanged(int state,String incomingNumber){

	  switch(state){

	    case TelephonyManager.CALL_STATE_IDLE:

	    	processState(state);
	    	Log.d("DEBUG", "IDLE");

	    break;

	    case TelephonyManager.CALL_STATE_OFFHOOK:

	    	processState(state);
	    	Log.d("DEBUG", "OFFHOOK");

	    break;
	    
	    case TelephonyManager.CALL_STATE_RINGING:

	    	processState(state);
	    	Log.d("DEBUG", "RINGING");

	    break;

	    }

	  }
	  
	  private void processState(int state){
		  //Send SMS request if outgoing call finished\
		  	     
		  if (state == TelephonyManager.CALL_STATE_IDLE && 
				  prevState == TelephonyManager.CALL_STATE_OFFHOOK){
			  Log.d("DEBUG","We can send request");
			  Log.d("DEBUG", ctx.toString());
			  bm.sendSMSRequest();
		  }
		  prevState = new Integer(state);
	  }

	}
