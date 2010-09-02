package name.pilgr.android.pibalance;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSResponseMonitor extends BroadcastReceiver {
	
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	
	/*At this time other application process and store SMS*/
	private static final int SAVE_SMS_TIMEOUT = 5000;
	private static final String ADDRESS_RESPONSE = "123";

	@Override
	public void onReceive(Context context, Intent intent) {
		String str ="";
		String msgAddress="";
		String msgBody="";
		boolean isExpectedMsg = false;
		if (intent != null && intent.getAction() != null
				&& ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
			
			//Process and delete message
			Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
			SmsMessage[] messages = new SmsMessage[pduArray.length];
			if (pduArray.length > 0){
				messages[0] = SmsMessage.createFromPdu((byte[]) pduArray[0]);
				msgAddress = messages[0].getOriginatingAddress();
				msgBody = messages[0].getMessageBody().toString();
				if (msgAddress.equalsIgnoreCase(ADDRESS_RESPONSE)){
                	isExpectedMsg = true;
                }
			}
			
			if (isExpectedMsg){
				
				str += "SMS from " + msgAddress;                     
                str += " :";
                str += msgBody;
                str += "\n";
                
				//Waiting for processing message by SMS/MMS Manager
				try {
					Thread.sleep(SAVE_SMS_TIMEOUT);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				//Only for debug
				Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
				
				deleteMessage(context, messages[0]); 
				cancelNotification(context);                
			}
		}

	}
	
	private void cancelNotification(Context context){
		Context mmsContext;
		try {
			mmsContext = context.createPackageContext(
			        "com.android.mms", Context.CONTEXT_IGNORE_SECURITY);
			NotificationManager notManager = (NotificationManager) mmsContext
	        .getSystemService(Context.NOTIFICATION_SERVICE);
			notManager.cancel(123);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void deleteMessage(Context context, SmsMessage msg) {
	    Uri deleteUri = Uri.parse("content://sms");
	    /*int count = 0;
	    Cursor c = context.getContentResolver().query(deleteUri, null, null,
	            null, null);*/
	    
	    //context.getContentResolver().delete(deleteUri, "address=? and date=?", new String[] {msg.getOriginatingAddress(), String.valueOf(msg.getTimestampMillis())});
	    String SMS_READ_COLUMN = "read";
	    String WHERE_CONDITION = SMS_READ_COLUMN + " = 0";
	    String SORT_ORDER = "date DESC";
	    Cursor c = context.getContentResolver().query(deleteUri,new String[] {"_id", "thread_id","address",SMS_READ_COLUMN},WHERE_CONDITION,null,SORT_ORDER);
	   while (c.moveToNext()) {
		   String pid = c.getString(0); // Get id;
		   String col1 = c.getString(1);
		   String col2 = c.getString(2);
		   String col3 = c.getString(3);
		   String col4 = c.getString(4);
		   String col5 = c.getString(5);
	   }
	   
	   
	    /*while (c.moveToNext()) {
	        try {
	            // Delete the SMS
	            String pid = c.getString(0); // Get id;
	            String uri = "content://sms/" + pid;
	            count = context.getContentResolver().delete(Uri.parse(uri),
	                    null, null);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }
	    return count;*/
	}

}
