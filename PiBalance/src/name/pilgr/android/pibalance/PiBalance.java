package name.pilgr.android.pibalance;

import name.pilgr.android.pibalance.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PiBalance extends Activity {
	
	public static final String PREFS_NAME = "currentData";

	TextView dbgTxt;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
            	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button sendBtn = (Button)findViewById(R.id.sendSmsBtn); 
        Button dbgBtn = (Button)findViewById(R.id.dbgBtn);
        Button ussdBtn = (Button)findViewById(R.id.sendUSSDBtn);
        dbgTxt = (TextView)findViewById(R.id.dbgTxt);
        
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
        
        dbgBtn.setOnClickListener(new OnClickListener(){ 
        	 
            @Override 
            public void onClick(View view) { 
            	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                String resp = settings.getString("lastResponse", "nothing");
                dbgTxt.setText(resp);
            	
            }}); 
        
        ussdBtn.setOnClickListener(new OnClickListener(){ 
       	 
            @Override 
            public void onClick(View view) { 
            	 call("*111" + Uri.encode("#"));                   
            }
            });
    
     
    }
    
    private void call(String ussdCode){
    	 
        try { 
            startActivityForResult(new Intent("android.intent.action.CALL", 
                		Uri.parse("tel:" + ussdCode)), 1); 
        } catch (Exception eExcept) { 
        	eExcept.printStackTrace();  
        }
         
    }
    
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
    	Toast.makeText(PiBalance.this, "\nUSSD: " + requestCode + " " + resultCode + " " + data,  
                Toast.LENGTH_LONG).show(); 
    } 
    
    private void sendSmsMessage(String address,String message)throws Exception 
    { 
        SmsManager smsMgr = SmsManager.getDefault(); 
        smsMgr.sendTextMessage(address, null, message, null, null); 
    }
}