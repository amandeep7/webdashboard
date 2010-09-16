package name.pilgr.android.pibalance;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ClickWidgetActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 Log.d("DEBUG", "We must show the last sms response");
		 BalanceModel bm = new BalanceModel(this);
		 Alerts.showAlert(bm.getLastResponse(), this);
	}

}
