package name.pilgr.android.pibalance;

//Global constants set
public class C {

	//The DB name for application preferences and states
	public static final String PREFS_NAME = "currentData";
	
	/*At this time other application (SMS/MMS manager) process and store SMS*/
	public static final int SAVE_SMS_TIMEOUT = 6000;
	
	/*Operator specific constants*/
	/*UA LIFE*/
	public static final String REQ_ADDR_UA_LIFE = "5016";
	public static final String REQ_MSG_UA_LIFE = "CHECKBALANCE";
	public static final String RESP_ADDR_UA_LIFE = "5433";
	/*RU MTS*/
	public static final String REQ_ADDR_RU_MTS = "111";
	public static final String REQ_MSG_RU_MTS = "11";
	public static final String RESP_ADDR_RU_MTS = "111";
	/*RU MEGAFON*/
	public static final String REQ_ADDR_RU_MEGA = "000100";
	public static final String REQ_MSG_RU_MEGA = "bal";
	public static final String RESP_ADDR_RU_MEGA_1 = "000100";
	public static final String RESP_ADDR_RU_MEGA_2 = "Balance";
	
	
	//String for display on widget if we have any problem
	public final static String PARSE_ERROR = "ERR";
	
	public final static String WASTE_SYMBOLS = "ÑñSs U:Ðð?";
	public final static String MINUS_STRING = "Ìèíóñ";
	
	public static final String REFRESH_INTENT = "name.pilgr.android.pibalance.intent.action.REFRESH_WIDGET";
	
	public static final int DEBUG_ANDROID_MCC_MNC = 310260;
	public static final int UA_LIFE_MCC_MNC = 25506;
	public static final int RU_MTS_MCC_MNC = 25001;
	public static final int RU_MEGAFON_MCC_MNC = 25002;

	public static final long REQUEST_DELAY = 1*60*60*1000;//1h

}
