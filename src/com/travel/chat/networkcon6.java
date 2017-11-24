package com.travel.chat;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class networkcon6 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.networkcon6);
	}
	
	public void myOnClick(View v){
    	new AlertDialog.Builder(this)
    	.setTitle("네트워크 대화창")
    	.setMessage("네트웍 연결을 하시겠습니까?")
    	.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog, int which){
    			Button btn_conn = (Button)findViewById(R.id.networkcon6);
    			btn_conn.postDelayed(new Runnable(){
    				public void run() {
    					conn();
    				}
    			},10);
    		}
    	})
    	.setNegativeButton("No", null)
    	.show();
    }
    
    void conn() {
    	for(int i =0; i < 20; i++){
    		try {Thread.sleep(100);} catch (InterruptedException e) {;}
    	}
    	Toast.makeText(this, "네트워크에 연결되었습니다.", 0).show();
    }
    
	
}