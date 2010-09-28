package name.pilgr.android.pibalance.receivers;

import name.pilgr.android.pibalance.C;
import name.pilgr.android.pibalance.model.BalanceModel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSResponseReceiver extends BroadcastReceiver {
	private static final String TAG = SMSResponseReceiver.class.getSimpleName();
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private BalanceModel bm;
	private Context ctx;
	private SmsMessage currSms;

	@Override
	public void onReceive(Context context, Intent intent) {
		String msgAddress = "";
		String msgBody = "";
		boolean isExpectedMsg = false;
		SmsMessage[] messages = null;
		ctx = context;

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

				Log.d(TAG, "Initial store response");
				bm = new BalanceModel(context);
				bm.storeResponse(msgBody);
				Log.d(TAG, "Response stored");
				// Waiting for processing message by default SMS/MMS Manager
				// try {
				// Thread.sleep(C.SAVE_SMS_TIMEOUT);
				// } catch (InterruptedException e1) {
				// e1.printStackTrace();
				// }
				// Log.d(TAG,"SLeeping done");
				// SLEEP 2 SECONDS HERE ...
				currSms = messages[0];
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						Log.d(TAG, "Start post delayed processing");
						deleteMessage(ctx, currSms);
						// Dirty tricks (available only on Android 1.5-1.6)
						cancelNotification(ctx);
						Log.d(TAG, "Finish post delayed processing");
					}
				}, 5000);

				// Only for debug
				Log.d(TAG, "Responce processed");
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
