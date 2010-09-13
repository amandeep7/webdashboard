package name.pilgr.android.pibalance;

import android.app.AlertDialog;
import android.content.Context;


public class Alerts {
	
	public static void showAlert(String message, Context ctx)    
	{ 
		new AlertDialog.Builder(ctx)
	      .setMessage(message)
	      .show();
	   }
	
	

}
