package name.pilgr.android.pibalance;

import java.math.BigDecimal;
import java.math.RoundingMode;

import name.pilgr.android.pibalance.R;
import name.pilgr.android.pibalance.model.BalanceModel;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PiBalance extends Activity {
	
	private TextView lastResp, opId, opName, reqAddress, reqMessage, respAddress;
	private TextView lblBalance;
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

        opId = (TextView)findViewById(R.id.netwOperatorId);
        opName = (TextView)findViewById(R.id.netwOperatorName);
        
        
        reqAddress = (TextView)findViewById(R.id.ed_request_address);
        reqMessage = (TextView)findViewById(R.id.ed_request_message);
        respAddress = (TextView)findViewById(R.id.ed_response_address);
        reqAddress.setText(bm.getRequestAddress());
        reqMessage.setText(bm.getRequestMessage());
        respAddress.setText(bm.getResponseAddress());
        
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        opId.setText(getString(R.string.lbl_operator_id) + " " + tm.getNetworkOperator());
        opName.setText(getString(R.string.lbl_operator_name) + " " +tm.getNetworkOperatorName());
        
        //Linkify text in about section
        TextView aboutView = (TextView) findViewById(R.id.tvAbout);
        Linkify.addLinks(aboutView, Linkify.ALL);
        
        sendBtn.setOnClickListener(new OnClickListener(){ 
 
            @Override 
            public void onClick(View view) { 
                
                try { 
                    bm.sendSMSRequest(true);
                    Toast.makeText(getCtx(), R.string.not_request_sent, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "SMS Sent");  
                } catch (Exception e) { 
                    Log.e(TAG, "Failed to send SMS"); 
                    e.printStackTrace(); 
                } 
            }}); 
        
        dbgBtn.setOnClickListener(new OnClickListener(){ 
        	 
            @Override 
            public void onClick(View view) { 
                if (bm.getOperatorId() == C.DEBUG_ANDROID_MCC_MNC){
                	bm.saveOperatorId(C.UA_LIFE_MCC_MNC);
                }
                else if (bm.getOperatorId() == C.UA_LIFE_MCC_MNC){
                	bm.saveOperatorId(C.RU_MTS_MCC_MNC);
                } else if (bm.getOperatorId() == C.RU_MTS_MCC_MNC){
                	bm.saveOperatorId(C.RU_MEGAFON_MCC_MNC);
                } else if (bm.getOperatorId() == C.RU_MEGAFON_MCC_MNC){
                	bm.saveOperatorId(C.UA_LIFE_MCC_MNC);
                }
                bm.storeResponse("Ваш баланс 10 тугриков");                
            }}); 
        
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	refreshBalance();
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
    
    private void refreshBalance(){
    	lastResp = (TextView)findViewById(R.id.last_response);        
        lblBalance =  (TextView)findViewById(R.id.lbl_box_balance);
        
        lastResp.setText(bm.getLastResponse());
        
        String balBox = getString(R.string.lbl_box_balance) + ": " + bm.getCurrentBalance();
        float flToday = bm.getTodayChange();
        String strToday = new BigDecimal(flToday).setScale(2, RoundingMode.UP).toString();
        if (flToday > 0){
        	strToday = "+" + flToday;
        }
        balBox += " " + getString(R.string.lbl_box_change_today) + ": " + strToday;
        lblBalance.setText(balBox);        
    }
    
      
}