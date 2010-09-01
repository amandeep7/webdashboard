package name.pilgr.android.pibalance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSResponseMonitor extends BroadcastReceiver {
	
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED"; 
	

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent != null && intent.getAction() != null
				&& ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
			
			//Process message
			Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
			SmsMessage[] messages = new SmsMessage[pduArray.length];
			for (int i = 0; i < pduArray.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
			}		
			
		}

	}

}
