package name.pilgr.android.pibalance;

import name.pilgr.android.pibalance.R;
import name.pilgr.android.pibalance.model.BalanceModel;
import name.pilgr.android.pibalance.services.RefreshService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PiBalance extends Activity {
	
	private TextView dbgTxt;
	private BalanceModel bm;
	private static final String TAG = PiBalance.class.getSimpleName(); 
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	bm = new BalanceModel((Context)this);
    	
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
                    bm.sendSMSRequest();
                    Log.d(TAG, "SMS Sent");  
                } catch (Exception e) { 
                    Log.e(TAG, "Failed to send SMS"); 
                    e.printStackTrace(); 
                } 
            }}); 
        
        dbgBtn.setOnClickListener(new OnClickListener(){ 
        	 
            @Override 
            public void onClick(View view) { 
                dbgTxt.setText(bm.getLastResponse());
                bm.storeResponse("1");
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
    
    private Context getCtx(){
    	return (Context)this;
    }
    
      
}