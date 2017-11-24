package com.travel.chat;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.widget.*;

public class network4 extends Activity {
	private ConnectivityManager connectManager;
	private NetworkInfo wifiInfo;
	private NetworkInfo mobileInfo;
	
	private TextView wifiTxt;
	private TextView mobileTxt;
	
	private StringBuilder wifiStr;
	private StringBuilder mobileStr;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network4);
        
        wifiTxt = (TextView)findViewById(R.id.wifi_status);
        mobileTxt = (TextView)findViewById(R.id.mobile_status);
        
        connectManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiInfo = connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobileInfo = connectManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);        
        refresh();
   }
    private void refresh(){
    	wifiStr = new StringBuilder();
    	wifiStr.append("사용가능 : ").append(wifiInfo.isAvailable()).append("\nWifi연결 :")
    	.append(wifiInfo.isConnected()).append("\n");
    	
    	mobileStr = new StringBuilder();
    	mobileStr.append("사용가능 :").append(mobileInfo.isAvailable()).append("\n3G 연결 : ")
    	.append(mobileInfo.isConnected()).append("\n로밍 :").append(mobileInfo.isRoaming());
    	wifiTxt.setText(wifiStr);
    	mobileTxt.setText(mobileStr);
    }
}
