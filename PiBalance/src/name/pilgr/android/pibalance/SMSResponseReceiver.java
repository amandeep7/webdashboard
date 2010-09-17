package name.pilgr.android.pibalance;

import name.pilgr.android.pibalance.model.BalanceModel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSResponseReceiver extends BroadcastReceiver {
	
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private BalanceModel bm;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String msgAddress = "";
		String msgBody = "";
		boolean isExpectedMsg = false;
		SmsMessage[] messages = null;
		
		if (intent != null && intent.getAction() != null
				&& ACTION.compareToIgnoreCase(intent.getAction()) == 0) {

			try {
				// Do we really need this message?
				Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
				messages = new SmsMessage[pduArray.length];
				if (pduArray.length > 0) {
					messages[0] = SmsMessage
							.createFromPdu((byte[]) pduArray[0]);
					msgAddress = messages[0].getOriginatingAddress();
					msgBody = messages[0].getMessageBody().toString();
					if (msgAddress.equalsIgnoreCase(C.ADDRESS_RESPONSE)) {
						isExpectedMsg = true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Yes, it's expected message
			if (isExpectedMsg) {

				bm = new BalanceModel(context);
				bm.storeResponse(msgBody);
				// Waiting for processing message by default SMS/MMS Manager
				try {
					Thread.sleep(C.SAVE_SMS_TIMEOUT);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				deleteMessage(context, messages[0]);
				// Dirty tricks (available only on Android 1.5-1.6)
				cancelNotification(context);

				// Only for debug
				Toast.makeText(context, "Responce processed",
						Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	private void cancelNotification(Context context) {
		Context mmsContext;
		try {
			mmsContext = context.createPackageContext("com.android.mms",
					Context.CONTEXT_IGNORE_SECURITY);
			NotificationManager notManager = (NotificationManager) mmsContext
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notManager.cancel(123);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void deleteMessage(Context context, SmsMessage msg) {
		Uri deleteUri = Uri.parse("content://sms");

		String WHERE_CONDITION = "read = 0 and address = " + C.ADDRESS_RESPONSE;
		String SORT_ORDER = "date DESC";
		Cursor c = context.getContentResolver().query(deleteUri,
				new String[] { "_id", "thread_id", "address", "read" },
				WHERE_CONDITION, null, SORT_ORDER);
		while (c.moveToNext()) {
			String pid = c.getString(0); // Get id;
			String delMsgUri = "content://sms/" + pid;
			context.getContentResolver().delete(Uri.parse(delMsgUri), null,
					null);
		}
	}

}
