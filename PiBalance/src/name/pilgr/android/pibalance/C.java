package name.pilgr.android.pibalance;

//Global constants set
public class C {

	//The DB name for application preferences and states
	public static final String PREFS_NAME = "currentData";
	
	/*At this time other application (SMS/MMS manager) process and store SMS*/
	public static final int SAVE_SMS_TIMEOUT = 5000;
	
	public static final String REQUEST_ADDRESS = "5016";
	public static final String REQUEST_MESSAGE = "CHECKBALANCE";
	
	//Listen incoming message from their address
	public static final String ADDRESS_RESPONSE = "5433";
	
	//String for display on widget if we have any problem
	public final static String PARSE_ERROR = "ERR";
	
	public final static String WASTE_SYMBOLS = " UAH";
	
	public static final String REFRESH_INTENT = "name.pilgr.android.pibalance.intent.action.REFRESH_WIDGET";

}
