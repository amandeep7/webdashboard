package name.pilgr.android.pibalance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneServiceReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {

		PhoneStateListener phoneListener = new PiPhoneStateListener(context);

		TelephonyManager telephony = 
			(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

		telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		Log.d("DEBUG", "Process outgonig calls");
	}

}
