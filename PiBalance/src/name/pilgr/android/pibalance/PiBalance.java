package name.pilgr.android.pibalance;

import name.pilgr.android.pibalance.R;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PiBalance extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button sendBtn = (Button)findViewById(R.id.sendSmsBtn); 
        Button delBtn = (Button)findViewById(R.id.delSmsBtn);
        
        sendBtn.setOnClickListener(new OnClickListener(){ 
 
            @Override 
            public void onClick(View view) { 
                
                try { 
                    sendSmsMessage( 
                        "5016","CHECKBALANCE"); 
                    Toast.makeText(PiBalance.this, "SMS Sent",  
                        Toast.LENGTH_LONG).show(); 
                } catch (Exception e) { 
                    Toast.makeText(PiBalance.this, "Failed to send SMS",  
                        Toast.LENGTH_LONG).show(); 
                    e.printStackTrace(); 
                } 
            }}); 
        
        delBtn.setOnClickListener(new OnClickListener(){ 
        	 
            @Override 
            public void onClick(View view) { 
            	 //Delete last message
        		Uri uriSms = Uri.parse("content://sms");
        		Cursor c = getContentResolver().query(uriSms, null,null,null,null); 
        		//int id = c.getInt(0);
        		int thread_id = c.getInt(0); //get the thread_id
        		getContentResolver().delete(Uri.parse("content://sms/conversations/" + thread_id),null,null);
        		Log.d("MySMSMonitor", "SMS Message Received.");                  
            }}); 
    
     
    }
    
    private void sendSmsMessage(String address,String message)throws Exception 
    { 
        SmsManager smsMgr = SmsManager.getDefault(); 
        smsMgr.sendTextMessage(address, null, message, null, null); 
    }
}